package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IGetTypesCarte;
import ma.adria.bank.dto.monetique.TypeCarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@ConditionalOnMissingBean(IGetTypesCarte.class)
@Component
public class getTypesCarteSimulation implements IGetTypesCarte {

    private final CardsDataHelper cardsDataHelper;

    public getTypesCarteSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public Map<String, Object> getTypesCarte(TypeCarteDto typeCarteDto) throws Exception {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> typesList = new ArrayList<>();

        // Ajouter les différents types de cartes
        typesList.add(createTypeCarteInfo("VISA_CLASSIC", "Visa Classic", "Carte de paiement et de retrait standard"));
        typesList.add(createTypeCarteInfo("VISA_GOLD", "Visa Gold", "Carte premium avec plafonds plus élevés"));
        typesList.add(createTypeCarteInfo("VISA_PLATINUM", "Visa Platinum", "Carte haut de gamme avec services exclusifs"));
        typesList.add(createTypeCarteInfo("MASTERCARD_STANDARD", "MasterCard Standard", "Carte de paiement et de retrait standard"));
        typesList.add(createTypeCarteInfo("MASTERCARD_GOLD", "MasterCard Gold", "Carte premium avec plafonds plus élevés"));
        typesList.add(createTypeCarteInfo("CARTE_PREPAYEE", "Carte Prépayée", "Carte rechargeable à plafond limité"));
        typesList.add(createTypeCarteInfo("CARTE_JEUNE", "Carte Jeune", "Carte adaptée aux besoins des jeunes de 18 à 25 ans"));
        typesList.add(createTypeCarteInfo("CARTE_PRO", "Carte Professionnelle", "Carte dédiée aux professionnels et entreprises"));

        response.put("status", "SUCCESS");
        response.put("typesCarte", typesList);
        response.put("total", typesList.size());

        return response;
    }

    private Map<String, Object> createTypeCarteInfo(String code, String label, String description) {
        Map<String, Object> typeInfo = new HashMap<>();
        typeInfo.put("code", code);
        typeInfo.put("libelle", label);
        typeInfo.put("description", description);
        typeInfo.put("actif", true);

        // Informations additionnelles pour chaque type de carte
        Map<String, Object> details = new HashMap<>();
        details.put("plafondParDefaut", code.contains("GOLD") || code.contains("PLATINUM") ? 10000 : 5000);
        details.put("fraisAnnuels", code.contains("PLATINUM") ? 500 : code.contains("GOLD") ? 300 : 150);
        details.put("disponibiliteInternationale", !code.equals("CARTE_JEUNE"));

        typeInfo.put("details", details);

        return typeInfo;
    }
}