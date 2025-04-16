package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardDtoConverter;
import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.ICommandeCarte;
import ma.adria.bank.dto.CarteRequestDto;
import ma.adria.bank.dto.monetique.CarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@ConditionalOnMissingBean(ICommandeCarte.class)
@Component
public class commandeCarteSimulation implements ICommandeCarte {

    private final CardsDataHelper cardsDataHelper;

    public commandeCarteSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public CarteDto commandeCarte(CarteRequestDto carteRequestDto) throws Exception {
        // Créer une simulation de nouvelle carte
        Map<String, Object> newCard = new HashMap<>();

        // Générer un ID unique pour la carte
        String cardId = UUID.randomUUID().toString();

        // Date d'expiration (3 ans à partir d'aujourd'hui)
        LocalDate expiryDate = LocalDate.now().plusYears(3);
        String formattedExpiryDate = expiryDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // Remplir les données de la carte avec les informations de la demande
        newCard.put("id", cardId);
        newCard.put("customerId", carteRequestDto.getMatriculeClient());
        newCard.put("cardNumber", "5555" + generateRandomDigits(12));
        newCard.put("cardType", carteRequestDto.getTypeProduitCarte());
        newCard.put("accountId", carteRequestDto.getCompteFacturation());
        newCard.put("status", "COMMANDE_EN_COURS");
        newCard.put("expiryDate", formattedExpiryDate);
        newCard.put("cardHolderName", carteRequestDto.getIdentifiantRC());
        newCard.put("creationDate", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        // Utiliser le convertisseur pour obtenir un objet CarteDto
        return CardDtoConverter.convertToCarteDto(newCard);
    }

    // Méthode utilitaire pour générer une séquence aléatoire de chiffres
    private String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}