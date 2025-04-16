package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IModifyPlafondClient;
import ma.adria.bank.dto.ModifyPlafondRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@ConditionalOnMissingBean(IModifyPlafondClient.class)
@Component
public class modifyPlafondClient implements IModifyPlafondClient {

    private final CardsDataHelper cardsDataHelper;

    public modifyPlafondClient(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public Map<String, Object> modify(ModifyPlafondRequest modifyPlafondRequest) throws Exception {
        Map<String, Object> response = new HashMap<>();

        // Vérifier si la carte existe
        Map<String, Object> cardData = cardsDataHelper.getCardById(modifyPlafondRequest.getCodPlafond());

        if (cardData == null) {
            response.put("status", "ERROR");
            response.put("message", "Carte non trouvée");
            return response;
        }

        // Générer un identifiant de transaction unique
        String transactionId = UUID.randomUUID().toString();

        // Récupérer les informations du plafond à modifier
        String typePlafond = modifyPlafondRequest.getMntPlafond();
        String nouveauPlafond = modifyPlafondRequest.getMntPlafond();

        // Simuler l'ancien plafond
        BigDecimal ancienPlafond = BigDecimal.valueOf(5000);

        // Vérifier si le nouveau plafond est valide
        if (nouveauPlafond.compareTo(String.valueOf(BigDecimal.ZERO)) <= 0) {
            response.put("status", "ERROR");
            response.put("message", "Le nouveau plafond doit être supérieur à zéro");
            return response;
        }

        // Simuler la modification du plafond
        response.put("status", "SUCCESS");
        response.put("message", "Modification du plafond effectuée avec succès");
        response.put("transactionId", transactionId);
        response.put("idCarte", modifyPlafondRequest.getCodPlafond());
        response.put("numeroPorteur", cardData.get("cardNumber"));
        response.put("typePlafond", typePlafond);
        response.put("ancienPlafond", ancienPlafond);
        response.put("nouveauPlafond", nouveauPlafond);
        response.put("dateModification", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("devise", "MAD");

        return response;
    }
}