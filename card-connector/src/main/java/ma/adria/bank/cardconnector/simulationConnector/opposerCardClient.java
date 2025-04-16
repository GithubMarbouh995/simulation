package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IOpposerCardClient;
import ma.adria.bank.dto.DemandeOppositionCarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@ConditionalOnMissingBean(IOpposerCardClient.class)
@Component
public class opposerCardClient implements IOpposerCardClient {

    private final CardsDataHelper cardsDataHelper;

    public opposerCardClient(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public Map<String, Object> opposerCarte(String numeroCard) {
        Map<String, Object> response = new HashMap<>();

        // Vérifier si la carte existe
        Map<String, Object> cardData = cardsDataHelper.getCardById(numeroCard);

        if (cardData == null) {
            response.put("status", "ERROR");
            response.put("message", "Carte non trouvée");
            return response;
        }

        // Générer un identifiant unique pour l'opération
        String operationId = UUID.randomUUID().toString();

        // Simuler l'opposition de la carte
        response.put("status", "SUCCESS");
        response.put("message", "Carte mise en opposition avec succès");
        response.put("operationId", operationId);
        response.put("numeroCarte", numeroCard);
        response.put("dateOpposition", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("statutCarte", "OPPOSEE");
        response.put("motifOpposition", "PERTE");

        return response;
    }

    @Override
    public Map<String, Object> opposerCarte(DemandeOppositionCarteDto demandeOppositionCarteDto) {
        Map<String, Object> response = new HashMap<>();

        // Vérifier si la carte existe
        String numeroCarte = demandeOppositionCarteDto.getNumeroCarte();
        Map<String, Object> cardData = cardsDataHelper.getCardById(numeroCarte);

        if (cardData == null) {
            response.put("status", "ERROR");
            response.put("message", "Carte non trouvée");
            return response;
        }

        // Générer un identifiant unique pour l'opération
        String operationId = UUID.randomUUID().toString();

        // Simuler l'opposition de la carte
        response.put("status", "SUCCESS");
        response.put("message", "Carte mise en opposition avec succès");
        response.put("operationId", operationId);
        response.put("numeroCarte", numeroCarte);
        response.put("nomPorteur", cardData.get("cardHolderName"));
        response.put("dateOpposition", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("motifOpposition", demandeOppositionCarteDto.getMotifOpposition());
        response.put("statutCarte", "OPPOSEE");
        response.put("codeClient", demandeOppositionCarteDto.getIdCarte());

        return response;
    }
}