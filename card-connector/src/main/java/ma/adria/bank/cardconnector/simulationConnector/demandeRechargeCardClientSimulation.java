package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IDemandeRechargeCardClient;
import ma.adria.bank.dto.DemandeRechargeCarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@ConditionalOnMissingBean(IDemandeRechargeCardClient.class)
@Component
public class demandeRechargeCardClientSimulation implements IDemandeRechargeCardClient {

    private final CardsDataHelper cardsDataHelper;

    public demandeRechargeCardClientSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public Map<String, Object> demandeRechargeCard(DemandeRechargeCarteDto demandeRechargeCarteDto) throws Exception {
        // Récupérer les données de la carte
        Map<String, Object> cardData = cardsDataHelper.getCardById(demandeRechargeCarteDto.getIdCarte());

        Map<String, Object> response = new HashMap<>();

        // Vérifier si la carte existe
        if (cardData == null) {
            response.put("status", "ERROR");
            response.put("message", "Carte non trouvée");
            return response;
        }

        // Générer un identifiant unique pour cette transaction
        String transactionId = UUID.randomUUID().toString();

        // Simuler la recharge de carte
        response.put("status", "SUCCESS");
        response.put("message", "Demande de recharge effectuée avec succès");
        response.put("transactionId", transactionId);
        response.put("cardId", cardData.get("id"));
        response.put("cardNumber", cardData.get("cardNumber"));
        response.put("amount", demandeRechargeCarteDto.getMontantRecharge());
        response.put("currency", "MAD");
        response.put("requestDate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("accountId", demandeRechargeCarteDto.getNumeroCompteCrediter());

        return response;
    }
}