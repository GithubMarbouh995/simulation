package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IGetCardMovementsClient;
import ma.adria.bank.dto.MouvementCarteRecherchFormDto;
import ma.adria.bank.dto.monetique.MouvementCarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.LocalDate.*;

//@ConditionalOnMissingBean(IGetCardMovementsClient.class)
@Component
public class cardMovementsClientSimulation implements IGetCardMovementsClient {

    private final CardsDataHelper cardsDataHelper;

    public cardMovementsClientSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public List<MouvementCarteDto> getGetCardMovements(MouvementCarteRecherchFormDto criteria) throws Exception {
        // Récupérer les données de la carte spécifiée
        Map<String, Object> cardData = cardsDataHelper.getCardById(criteria.getNumeroCarte());

        if (cardData == null || !cardData.containsKey("movements")) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> movements = (List<Map<String, Object>>) cardData.get("movements");

        // Filtrer les mouvements selon les critères de recherche
        return movements.stream()
                .filter(movement -> matchesCriteria(movement, criteria))
                .map(this::convertToMouvementCarteDto)
                .collect(Collectors.toList());
    }

    private boolean matchesCriteria(Map<String, Object> movement, MouvementCarteRecherchFormDto criteria) {
        // Implémenter la logique de filtrage selon les critères (dates, montants, etc.)
        if (criteria.getDateOperationDebut() != null && criteria.getDateOperationFin() != null) {
            LocalDate movementDate = parse((String) movement.get("date"));
            LocalDate dateDebut = parse(criteria.getDateOperationDebut());
            LocalDate dateFin = parse(criteria.getDateOperationFin());

            if (movementDate.isBefore(dateDebut) || movementDate.isAfter(dateFin)) {
                return false;
            }
        }

        // Autres critères de filtrage si nécessaires
        return true;
    }


    private MouvementCarteDto convertToMouvementCarteDto(Map<String, Object> movement) {
        MouvementCarteDto dto = new MouvementCarteDto();

        dto.setId(Long.valueOf((String) movement.get("id")));

        // Conversion de LocalDate vers java.util.Date
        LocalDate localDate = parse((String) movement.get("date"));
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        dto.setDateOperation(date);

        dto.setMontantOperationLoc(BigDecimal.valueOf(Double.parseDouble(movement.get("amount").toString())));
        dto.setDevise((String) movement.get("currency"));
        dto.setMontantOperationLocLibelle((String) movement.get("description"));
        dto.setTypeOperation((String) movement.get("operationType"));
        dto.setCommercant((String) movement.get("merchant"));

        return dto;
    }



}