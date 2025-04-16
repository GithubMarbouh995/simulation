package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardDtoConverter;
import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IClientCardRequest;
import ma.adria.bank.dto.UserDetailsDto;
import ma.adria.bank.dto.monetique.CarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@ConditionalOnMissingBean(IClientCardRequest.class)
@Component
public class clientCardRequestSimulation implements IClientCardRequest {

    private final CardsDataHelper cardsDataHelper;

    public clientCardRequestSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public List<CarteDto> clientCardsRequestList(UserDetailsDto userDetailsDto) throws Exception {
        // Récupérer l'identifiant du client depuis UserDetailsDto
        String clientId = userDetailsDto.getId();

        if (clientId == null || clientId.isEmpty()) {
            return new ArrayList<>();
        }

        // Récupérer les cartes pour ce client
        List<Map<String, Object>> cards = cardsDataHelper.getCardsByCustomerId(clientId);

        if (cards == null || cards.isEmpty()) {
            return new ArrayList<>();
        }

        // Convertir les données brutes en objets CarteDto
        return cards.stream()
                .map(CardDtoConverter::convertToCarteDto)
                .collect(Collectors.toList());
    }
}