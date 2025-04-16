package ma.adria.bank.cardconnector.simulationConnector.util;

import ma.adria.bank.dto.monetique.CarteDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CardDtoConverter {

    public static CarteDto convertToCarteDto(Map<String, Object> cardData) {
        if (cardData == null) {
            return null;
        }

        CarteDto carteDto = new CarteDto();
        carteDto.setId((String) cardData.get("id"));
        carteDto.setNumeroCompte((String) cardData.get("cardNumber"));
        carteDto.setTypeCarte((String) cardData.get("cardType"));
        carteDto.setStatut((String) cardData.get("status"));

        // Conversion de la date d'expiration
        String expiryDateStr = (String) cardData.get("expiryDate");
        if (expiryDateStr != null) {
            LocalDate expiryDate = LocalDate.parse(expiryDateStr);
            carteDto.setDateExpiration(expiryDate.format(DateTimeFormatter.ofPattern("MM/yy")));
        }

        return carteDto;
    }

}