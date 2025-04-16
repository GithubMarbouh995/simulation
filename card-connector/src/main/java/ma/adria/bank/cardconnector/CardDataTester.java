package ma.adria.bank.cardconnector;

import ma.adria.bank.cardconnector.simulationConnector.activateUnsecurePaymentCardClientSimulation;
import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.dto.AUPaymentRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CardDataTester implements CommandLineRunner {

    private final CardsDataHelper cardsDataHelper;
    private final activateUnsecurePaymentCardClientSimulation cardClient;

    public CardDataTester(CardsDataHelper cardsDataHelper,
                          activateUnsecurePaymentCardClientSimulation cardClient) {
        this.cardsDataHelper = cardsDataHelper;
        this.cardClient = cardClient;
    }

    @Override
    public void run(String... args) throws Exception {
        // Afficher toutes les cartes
        System.out.println("Liste complète des cartes :");
        cardsDataHelper.getAllCards().forEach(card ->
                System.out.println("ID: " + card.get("id") + ", Numéro: " + card.get("cardNumber")));
        // Insérez ici un identifiant de carte qui existe dans votre fichier
        String testCardNumber = "card-001";

        System.out.println("Test de récupération des données");
        Map<String, Object> cardData = cardsDataHelper.getCardById(testCardNumber);
        System.out.println("Données de la carte: " + cardData);

        if (cardData != null) {
            System.out.println("La carte a été trouvée avec succès!");
            AUPaymentRequest request = new AUPaymentRequest();
            request.setNumeroCarte(testCardNumber);

            Map<String, Object> response = cardClient.activateUnsecurePaymentCard(request);
            System.out.println("Réponse d'activation: " + response);
        } else {
            System.out.println("Aucune carte trouvée avec cet identifiant!");
        }
    }
}