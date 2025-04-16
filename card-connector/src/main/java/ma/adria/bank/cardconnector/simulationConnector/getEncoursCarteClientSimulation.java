package ma.adria.bank.cardconnector.simulationConnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IGetEncoursCarteClient;
import ma.adria.bank.dto.MouvementCarteRecherchFormDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//@ConditionalOnMissingBean(IGetEncoursCarteClient.class)
@Component
public class getEncoursCarteClientSimulation implements IGetEncoursCarteClient {

    private final CardsDataHelper cardsDataHelper;
    private final Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public getEncoursCarteClientSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public String getGetEncoursCarte(MouvementCarteRecherchFormDto mouvementCarteRecherchFormDto) {
        try {
            // Vérifier si la carte existe
            Map<String, Object> cardData = cardsDataHelper.getCardById(mouvementCarteRecherchFormDto.getNumeroCarte());

            if (cardData == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "ERROR");
                errorResponse.put("message", "Carte non trouvée");
                return objectMapper.writeValueAsString(errorResponse);
            }

            // Créer la réponse avec les informations d'encours
            Map<String, Object> encoursData = new HashMap<>();

            // Informations de la carte
            encoursData.put("numeroCarte", cardData.get("cardNumber"));
            encoursData.put("nomPorteur", cardData.get("cardHolderName"));
            encoursData.put("typeCompte", cardData.get("accountId"));

            // Simuler un montant d'encours aléatoire
            BigDecimal montantEncours = BigDecimal.valueOf(random.nextDouble() * 5000).setScale(2, BigDecimal.ROUND_HALF_UP);
            encoursData.put("montantEncours", montantEncours);
            encoursData.put("devise", "MAD");

            // Simuler les détails des transactions en encours
            List<Map<String, Object>> transactions = generateRandomTransactions(
                    mouvementCarteRecherchFormDto.getNumeroCarte(),
                    montantEncours
            );
            encoursData.put("transactions", transactions);

            // Informations complémentaires
            encoursData.put("dateConsultation", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            encoursData.put("status", "SUCCESS");

            return objectMapper.writeValueAsString(encoursData);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Erreur lors de la récupération de l'encours: " + e.getMessage());

            try {
                return objectMapper.writeValueAsString(errorResponse);
            } catch (Exception ex) {
                return "{\"status\":\"ERROR\",\"message\":\"Erreur technique\"}";
            }
        }
    }

    private List<Map<String, Object>> generateRandomTransactions(String cardId, BigDecimal totalAmount) {
        List<Map<String, Object>> transactions = new ArrayList<>();

        // Nombre aléatoire de transactions (entre 1 et 5)
        int transactionCount = random.nextInt(5) + 1;

        // Répartir le montant total en transactions individuelles
        BigDecimal remainingAmount = totalAmount;

        for (int i = 0; i < transactionCount; i++) {
            Map<String, Object> transaction = new HashMap<>();

            // Pour la dernière transaction, utiliser le montant restant
            BigDecimal transactionAmount;
            if (i == transactionCount - 1) {
                transactionAmount = remainingAmount;
            } else {
                // Montant aléatoire mais pas supérieur au montant restant
                double percentage = random.nextDouble() * 0.7; // Max 70% du restant
                transactionAmount = remainingAmount.multiply(BigDecimal.valueOf(percentage)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            remainingAmount = remainingAmount.subtract(transactionAmount);

            // Générer une date de transaction récente (dans les 5 derniers jours)
            LocalDate transactionDate = LocalDate.now().minusDays(random.nextInt(5));

            transaction.put("id", UUID.randomUUID().toString());
            transaction.put("montant", transactionAmount);
            transaction.put("dateTransaction", transactionDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            transaction.put("commercant", getRandomMerchant());
            transaction.put("status", "EN_COURS");
            transaction.put("devise", "MAD");

            transactions.add(transaction);
        }

        return transactions;
    }

    private String getRandomMerchant() {
        String[] merchants = {
                "Supermarché Carrefour",
                "Restaurant La Scala",
                "Amazon",
                "Station Total",
                "Pharmacie Centrale",
                "Boutique Zara",
                "Café Starbucks",
                "Cinéma Mégarama"
        };

        return merchants[random.nextInt(merchants.length)];
    }
}