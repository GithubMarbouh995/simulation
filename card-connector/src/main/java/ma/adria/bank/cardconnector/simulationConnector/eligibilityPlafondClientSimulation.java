package ma.adria.bank.cardconnector.simulationConnector;

import ma.adria.bank.cardconnector.simulationConnector.util.CardsDataHelper;
import ma.adria.bank.clients.IEligibilityPlafondClient;
import ma.adria.bank.dto.EligibilityRequestDTO;
import ma.adria.bank.dto.EligibilityResponseDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//@ConditionalOnMissingBean(IEligibilityPlafondClient.class)
@Component
public class eligibilityPlafondClientSimulation implements IEligibilityPlafondClient {

    private final CardsDataHelper cardsDataHelper;
    private final Random random = new Random();

    public eligibilityPlafondClientSimulation(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Override
    public EligibilityResponseDto checkEligibility(EligibilityRequestDTO eligibilityRequestDTO) throws Exception {
        EligibilityResponseDto response = new EligibilityResponseDto();

        // Vérifier l'identifiant client/carte
        String clientId = eligibilityRequestDTO.getCardReference();

        // Simulation de la vérification d'éligibilité avec une décision aléatoire
        boolean isEligible = random.nextBoolean();

        if (isEligible) {
            // Client éligible
            response.setSuccess(true);
            response.setLimitDate("Client éligible pour modification de plafond");

            // Calcul du plafond maximal autorisé (exemple de simulation)
            BigDecimal plafondMaximal = calculateMaxPlafond(eligibilityRequestDTO);
            response.setMaximum(plafondMaximal);

//            // Autres informations pertinentes
//            Map<String, Object> additionalInfo = new HashMap<>();
//            additionalInfo.put("scoreClient", random.nextInt(900) + 100); // Score entre 100 et 999
//            additionalInfo.put("historiqueTransactions", random.nextInt(50) + 10); // Nombre de transactions
//            additionalInfo.put("ancienneteClient", random.nextInt(10) + 1); // Années d'ancienneté
//
//            response.set(additionalInfo);
        } else {
            // Client non éligible
            response.setSuccess(false);
            response.setLimitDate("Client non éligible pour modification de plafond");
            response.setLimitDate("Score insuffisant ou limite atteinte");
        }

        return response;
    }

    private BigDecimal calculateMaxPlafond(EligibilityRequestDTO request) {
        // Simulation de calcul de plafond basée sur des critères fictifs
        BigDecimal baseAmount = new BigDecimal(5000);

        // Facteur multiplicateur aléatoire entre 1 et 10
        int factor = random.nextInt(10) + 1;

        return baseAmount.multiply(new BigDecimal(factor));
    }
}