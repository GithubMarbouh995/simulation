package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardDtoConverter;
import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IGetInfoCarte;
import ma.adria.bank.dto.InfoCarteMiddleRequestDto;
import ma.adria.bank.dto.monetique.CarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

//@ConditionalOnMissingBean(IGetInfoCarte.class)
@Component
public class getInfoCarteSimulation implements IGetInfoCarte {

    private final CardsDataHelper cardsDataHelper;

    public getInfoCarteSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;

    }
//    setCodeOperation
@Override
public CarteDto getInfoCarte(InfoCarteMiddleRequestDto infoCarteMiddleRequestDto) {
    // Vérifiez l'attribut correct dans InfoCarteMiddleRequestDto (par exemple, numCarte ou id)
    Map<String, Object> cardData = cardsDataHelper.getCardById(infoCarteMiddleRequestDto.getCodeOperation());
    if (cardData == null) {
        return null;
    }

    return CardDtoConverter.convertToCarteDto(cardData);
}


}