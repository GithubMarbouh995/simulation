package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.ISuivicommandeCarte;
import ma.adria.bank.dto.PlafondsCarteGeneriqueDto;
import ma.adria.bank.dto.monetique.CarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//@ConditionalOnMissingBean(ISuivicommandeCarte.class)
@Component
public class suivicommandeCarteSimulation implements ISuivicommandeCarte {

    private final CardsDataHelper cardsDataHelper;
    private final Random random = new Random();

    public suivicommandeCarteSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public CarteDto suiviCommandeCarte(String referenceCommande) throws Exception {
        CarteDto carteDto = new CarteDto();

        // Vérifier si la référence existe
        Map<String, Object> cardData = cardsDataHelper.getCardByReference(referenceCommande);

        if (cardData == null) {
            carteDto.setStatut("ERROR");
            carteDto.setTypeCarte("Commande non trouvée");
            return carteDto;
        }

        // Détermine l'état aléatoire de la commande
        String[] etats = {"DEMANDEE", "EN_COURS_DE_FABRICATION", "EN_COURS_D_EXPEDITION", "EXPEDIEE", "LIVREE"};
        int indexEtat = random.nextInt(etats.length);
        String etatCommande = etats[indexEtat];

        // Informations de la carte
        carteDto.setStatut("SUCCESS");
        carteDto.setRefPorteurCarte(referenceCommande);
        carteDto.setDateCreation(LocalDate.now().minusDays(random.nextInt(15) + 1).format(DateTimeFormatter.ISO_LOCAL_DATE));
        carteDto.setIdCarte(maskCardNumber((String) cardData.get("cardNumber")));
        carteDto.setTypeCarte((String) cardData.get("cardType"));
        carteDto.setNomPorteur((String) cardData.get("cardHolderName"));
        carteDto.setEtat(etatCommande);

        // Date de livraison estimée ou effective
        LocalDate dateLivraison = LocalDate.now().plusDays(indexEtat < 4 ? random.nextInt(10) + 1 : 0);
        carteDto.setDateActivation(dateLivraison.format(DateTimeFormatter.ISO_LOCAL_DATE));

        // Historique des étapes de la commande
        List<Map<String, Object>> etapesCommande = generateEtapesCommande(indexEtat);
        carteDto.setPlafondsCarteGeneriqueList(convertToPlafondsDto(etapesCommande));

        // Informations complémentaires
        carteDto.setCodeAgencePorteur((String) cardData.get("agence"));
        carteDto.setCompteFacturation((String) cardData.get("accountId"));

        return carteDto;
    }

    private List<PlafondsCarteGeneriqueDto> convertToPlafondsDto(List<Map<String, Object>> etapesCommande) {
        List<PlafondsCarteGeneriqueDto> plafondsList = new ArrayList<>();
        for (Map<String, Object> etape : etapesCommande) {
            PlafondsCarteGeneriqueDto plafond = new PlafondsCarteGeneriqueDto();
            plafond.setServiceCode((String) etape.get("description"));
            plafond.setServiceLibelle((String) etape.get("etat"));
            plafondsList.add(plafond);
        }
        return plafondsList;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 16) {
            return "************1234";
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }

    private List<Map<String, Object>> generateEtapesCommande(int indexEtatActuel) {
        List<Map<String, Object>> etapes = new ArrayList<>();
        String[] etats = {"DEMANDEE", "EN_COURS_DE_FABRICATION", "EN_COURS_D_EXPEDITION", "EXPEDIEE", "LIVREE"};

        LocalDate dateBase = LocalDate.now().minusDays(indexEtatActuel * 2 + 10);

        for (int i = 0; i <= indexEtatActuel; i++) {
            Map<String, Object> etape = new HashMap<>();
            etape.put("etat", etats[i]);
            etape.put("date", dateBase.plusDays(i * 2).format(DateTimeFormatter.ISO_LOCAL_DATE));
            etape.put("description", getDescriptionEtape(etats[i]));
            etapes.add(etape);
        }

        return etapes;
    }

    private String getDescriptionEtape(String etat) {
        switch (etat) {
            case "DEMANDEE":
                return "Demande de carte enregistrée";
            case "EN_COURS_DE_FABRICATION":
                return "Carte en cours de fabrication";
            case "EN_COURS_D_EXPEDITION":
                return "Carte en cours d'acheminement";
            case "EXPEDIEE":
                return "Carte expédiée vers votre agence";
            case "LIVREE":
                return "Carte disponible en agence";
            default:
                return "";
        }
    }
}