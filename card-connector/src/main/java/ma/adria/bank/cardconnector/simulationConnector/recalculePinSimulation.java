package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IRecalculePin;
import ma.adria.bank.dto.DemandeRecalculPinDto;
import ma.adria.bank.dto.monetique.RecalculPinResponseDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

//@ConditionalOnMissingBean(IRecalculePin.class)
@Component
public class recalculePinSimulation implements IRecalculePin {

    private final CardsDataHelper cardsDataHelper;
    private final Random random = new Random();

    public recalculePinSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public RecalculPinResponseDTO recalculePin(DemandeRecalculPinDto demandeRecalculPinDto) {
        RecalculPinResponseDTO response = new RecalculPinResponseDTO();

        // Vérifier si la carte existe
        Map<String, Object> cardData = cardsDataHelper.getCardById(demandeRecalculPinDto.getIdCarte());

        if (cardData == null) {
            response.setErrorCode(Integer.parseInt("ERROR"));
            response.setReturnMessage("Carte non trouvée");
            return response;
        }

        // Générer un identifiant unique pour l'opération
        String operationId = UUID.randomUUID().toString();

        // Générer un code PIN aléatoire à 4 chiffres (pour simulation uniquement)
        // Dans un environnement réel, le PIN ne serait jamais stocké ou retourné en clair
        StringBuilder pinBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pinBuilder.append(random.nextInt(10));
        }
        String nouveauPin = pinBuilder.toString();

        // Simuler le recalcul du PIN
        response.setErrorCode(Integer.parseInt("SUCCESS"));
        response.setReturnMessage("Code PIN recalculé avec succès");
        response.setNumdemande(operationId);
        response.setIdCarte(demandeRecalculPinDto.getIdCarte());
        response.setReturnCode(Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        // Note: Dans un cas réel, le PIN ne serait jamais renvoyé directement
        // Il serait imprimé sur un support sécurisé ou envoyé par un canal sécurisé
        response.setCodeOperation(nouveauPin);

        return response;
    }
}