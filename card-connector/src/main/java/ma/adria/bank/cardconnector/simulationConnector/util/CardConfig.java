package ma.adria.bank.cardconnector.simulationConnector.util;

import ma.adria.bank.cardconnector.simulationConnector.*;
import ma.adria.bank.clients.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardConfig {

    private final CardsDataHelper cardsDataHelper;

    public CardConfig(CardsDataHelper cardsDataHelper) {
        this.cardsDataHelper = cardsDataHelper;
    }

    @Bean
    @ConditionalOnMissingBean(IGetCardListClient.class)
    public IGetCardListClient cardListClient() {
        return new cardListClientSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IGetCardMovementsClient.class)
    public IGetCardMovementsClient cardMovementsClient() {
        return new cardMovementsClientSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IBlocageDeblocageCarte.class)
    public IBlocageDeblocageCarte blocageDeblocageCarteClient() {
        return new blocageDeblocageCarteSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IModifyPlafondClient.class)
    public IModifyPlafondClient modifyPlafondClient() {
        return new modifyPlafondClient(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IActivateUnsecurePaymentCardClient.class)
    public IActivateUnsecurePaymentCardClient activateUnsecurePaymentCardClient() {
        return new activateUnsecurePaymentCardClientSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IEligibilityPlafondClient.class)
    public IEligibilityPlafondClient eligibilityPlafondClient() {
        return new eligibilityPlafondClientSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IGetTypesCarte.class)
    public IGetTypesCarte getTypesCarteClient() {
        return new getTypesCarteSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IGetInfoCarte.class)
    public IGetInfoCarte getInfoCarteClient() {
        return new getInfoCarteSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IRecalculePin.class)
    public IRecalculePin recalculePinClient() {
        return new recalculePinSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IDemandeRechargeCardClient.class)
    public IDemandeRechargeCardClient demandeRechargeCardClient() {
        return new demandeRechargeCardClientSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IClientCardRequest.class)
    public IClientCardRequest clientCardRequestClient() {
        return new clientCardRequestSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(ICommandeCarte.class)
    public ICommandeCarte commandeCarteClient() {
        return new commandeCarteSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IGetEncoursCarteClient.class)
    public IGetEncoursCarteClient getEncoursCarteClient() {
        return new getEncoursCarteClientSimulation(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(IOpposerCardClient.class)
    public IOpposerCardClient opposerCardClient() {
        return new opposerCardClient(cardsDataHelper);
    }

    @Bean
    @ConditionalOnMissingBean(ISuivicommandeCarte.class)
    public ISuivicommandeCarte suivicommandeCarteClient() {
        return new suivicommandeCarteSimulation(cardsDataHelper);
    }
}