package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardDtoConverter;
import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.cardconnector.simulationConnector.util.CardDtoConverter;
import ma.adria.bank.clients.IBlocageDeblocageCarte;
import ma.adria.bank.dto.monetique.CarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

//@ConditionalOnMissingBean(IBlocageDeblocageCarte.class)
@Component
public class blocageDeblocageCarteSimulation implements IBlocageDeblocageCarte {

    private final CardsDataHelper cardsDataHelper;

    public blocageDeblocageCarteSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public CarteDto blocageDeblocageCarte(String cardId, String action, String reason) throws Exception {
        Map<String, Object> cardData = cardsDataHelper.getCardById(cardId);
        if (cardData == null) {
            return null;
        }

        CarteDto carteDto = CardDtoConverter.convertToCarteDto(cardData);

        // Simuler le changement de statut
        if ("BLOCK".equalsIgnoreCase(action)) {
            carteDto.setStatut("BLOCKED");
        } else if ("UNBLOCK".equalsIgnoreCase(action)) {
            carteDto.setStatut("ACTIVE");
        }

        return carteDto;
    }


}