package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardDtoConverter;
import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IActivateUnsecurePaymentCardClient;
import ma.adria.bank.dto.AUPaymentRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@ConditionalOnMissingBean(IActivateUnsecurePaymentCardClient.class)
@Component
public class activateUnsecurePaymentCardClientSimulation implements IActivateUnsecurePaymentCardClient {

    private final CardsDataHelper cardsDataHelper;

    public activateUnsecurePaymentCardClientSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public Map<String, Object> activateUnsecurePaymentCard(AUPaymentRequest auPaymentRequest) throws Exception {
        // Récupérer les données de la carte
        Map<String, Object> cardData = cardsDataHelper.getCardById(auPaymentRequest.getNumeroCarte());

        // Créer et remplir la réponse
        Map<String, Object> response = new HashMap<>();

        if (cardData == null) {
            response.put("status", "ERROR");
            response.put("message", "Carte non trouvée");
            return response;
        }

        // Simuler l'activation du paiement non sécurisé
        response.put("status", "SUCCESS");
        response.put("message", "Activation du paiement non sécurisé réussie");
        response.put("cardId", cardData.get("id"));
        response.put("cardNumber", cardData.get("cardNumber"));
        response.put("activationDate", java.time.LocalDate.now().toString());
        response.put("expiryDate", cardData.get("expiryDate"));

        return response;
    }
}