package ma.adria.bank.cardconnector.simulationConnector;

import lombok.NoArgsConstructor;
import ma.adria.bank.cardconnector.simulationConnector.util.CardDtoConverter;
import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IGetCardListClient;
import ma.adria.bank.dto.monetique.CarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@ConditionalOnMissingBean(IGetCardListClient.class)
@Component
//@NoArgsConstructor(force = true)
public class cardListClientSimulation implements IGetCardListClient {

    private final CardsDataHelper cardsDataHelper;

    public cardListClientSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public List<CarteDto> getGetCardList(String clientId) throws Exception {
        List<Map<String, Object>> cards = cardsDataHelper.getCardsByCustomerId(clientId);

        return cards.stream()
                .map(CardDtoConverter::convertToCarteDto)
                .collect(Collectors.toList());
    }
}