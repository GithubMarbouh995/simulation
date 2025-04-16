package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IGetCardPlafonds;
import ma.adria.bank.dto.PlafondsCarteDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

//@ConditionalOnMissingBean(IGetCardPlafonds.class)
@Component
public class getCardPlafondsSimulation implements IGetCardPlafonds {

    private final CardsDataHelper cardsDataHelper;
    private final Random random = new Random();

    public getCardPlafondsSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public List<PlafondsCarteDto> GetCardPlafonds(String cardId, String clientId) throws Exception {
        // Vérifier si la carte existe
        Map<String, Object> cardData = cardsDataHelper.getCardById(cardId);

        if (cardData == null) {
            return new ArrayList<>();
        }

        // Créer une liste des différents types de plafonds
        List<PlafondsCarteDto> plafondsList = new ArrayList<>();

        // Ajouter plafond de retrait journalier
        plafondsList.add(createPlafond(cardId, "RETRAIT_JOURNALIER", 2000.0, 5000.0));

        // Ajouter plafond de retrait hebdomadaire
        plafondsList.add(createPlafond(cardId, "RETRAIT_HEBDOMADAIRE", 10000.0, 25000.0));

        // Ajouter plafond de paiement journalier
        plafondsList.add(createPlafond(cardId, "PAIEMENT_JOURNALIER", 5000.0, 20000.0));

        // Ajouter plafond de paiement hebdomadaire
        plafondsList.add(createPlafond(cardId, "PAIEMENT_HEBDOMADAIRE", 20000.0, 50000.0));

        // Ajouter plafond mensuel global
        plafondsList.add(createPlafond(cardId, "GLOBAL_MENSUEL", 50000.0, 100000.0));

        return plafondsList;
    }

    private PlafondsCarteDto createPlafond(String cardId, String type, double defaultValue, double maxValue) {
        PlafondsCarteDto plafond = new PlafondsCarteDto();

        plafond.setCodePlafond(cardId);
        plafond.setPlafondNombre(type);
        plafond.setPlafondRetrait(BigDecimal.valueOf(defaultValue));
        plafond.setPlafondECommerce(BigDecimal.valueOf(maxValue));

        // Simuler une utilisation aléatoire du plafond
        double utilisationPercentage = random.nextDouble();
        BigDecimal consommation = plafond.getPlafondInternationnalQuotidien()
                .multiply(BigDecimal.valueOf(utilisationPercentage))
                .setScale(2, BigDecimal.ROUND_HALF_UP);

        plafond.setPlafondInternationnalPeriodique(consommation);
        plafond.setDevise("MAD");

        return plafond;
    }
}