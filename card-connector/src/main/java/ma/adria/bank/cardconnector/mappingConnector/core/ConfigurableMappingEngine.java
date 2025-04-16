//package ma.adria.bank.cardconnector.mappingConnector.core;
//
//import jakarta.annotation.PostConstruct;
//import ma.adria.bank.cardconnector.mappingConnector.config.MappingConfigLoader;
//import ma.adria.bank.cardconnector.mappingConnector.core.definition.MappingDefinition;
//import ma.adria.bank.cardconnector.mappingConnector.exception.MappingException;
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingContext;
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingService;
//import ma.adria.bank.cardconnector.mappingConnector.override.MappingOverride;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ConfigurableMappingEngine implements MappingService {
//    private final MappingRegistry mappingRegistry;
//    private final MappingConfigLoader configLoader;
//    private final BankConfigurationService bankConfigService;
//
//    public ConfigurableMappingEngine(
//            MappingRegistry mappingRegistry,
//            MappingConfigLoader configLoader,
//            BankConfigurationService bankConfigService) {
//        this.mappingRegistry = mappingRegistry;
//        this.configLoader = configLoader;
//        this.bankConfigService = bankConfigService;
//    }
//
//    @PostConstruct
//    public void initialize() {
//        // Charger les définitions de mapping de base
//
//        List<MappingDefinition> definitions = configLoader.loadMappingDefinitions();
//        for (MappingDefinition def : definitions) {
//            mappingRegistry.registerMapping(def);
//        }
//
//
//
//        // Charger les surcharges globales (non spécifiques à une banque)
//        List<MappingOverride> globalOverrides = configLoader.loadMappingOverrides(null);
//        for (MappingOverride override : globalOverrides) {
//            mappingRegistry.addOverride(override.getMappingId(), override);
//        }
//    }
//
//    @Override
//    public <S, T> T transform(S source, Class<T> targetType) {
//
//        if (source == null) return null;
//
//        // Récupérer la définition de mapping appropriée
//        MappingDefinition definition = mappingRegistry.findMapping(
//                source.getClass().getName(),
//                targetType.getName()
//        );
//
//        if (definition == null) {
//            throw new MappingException("Aucun mapping trouvé de " +
//                    source.getClass().getName() + " vers " + targetType.getName());
//        }
//
//        // Effectuer la transformation
//        return (T) definition.apply(source, null);
//    }
//
//    @Override
//    public <S, T> List<T> transformList(List<S> sources, Class<T> targetType) {
//        List<T> results = new ArrayList<>(sources.size());
//        for (S source : sources) {
//            results.add(transform(source, targetType));
//        }
//        return results;
//    }
//
//    @Override
//    public <S, T> T transformWithContext(S source, Class<T> targetType, MappingContext context) {
//        if (source == null) return null;
//
//        loadBankMappingOverrides(context.getBankId());
//
//        // Récupérer la définition de mapping appropriée
//        MappingDefinition definition = mappingRegistry.findMapping(
//                source.getClass().getName(),
//                targetType.getName()
//        );
//
//        if (definition == null) {
//            throw new MappingException("Aucun mapping trouvé de " +
//                    source.getClass().getName() + " vers " + targetType.getName());
//        }
//
//        // Appliquer les surcharges spécifiques à la banque si un bankId est présent dans le contexte
//        String bankId = context.getBankId();
//
//        if (bankId != null) {
//            definition = applyBankSpecificOverrides(definition, bankId);
//
//        }
//
//        // Effectuer la transformation
//        return (T) definition.apply(source, context);
//    }
//
//    /**
//     * Applique les surcharges spécifiques à une banque sur une définition de mapping
//     */
//    private MappingDefinition applyBankSpecificOverrides(MappingDefinition definition, String bankId) {
//        // Créer une copie de la définition pour ne pas modifier l'originale
//        MappingDefinition result = definition.copy();
//
//        // Récupérer et appliquer les surcharges spécifiques à cette banque
//        List<MappingOverride> overrides = mappingRegistry.getOverrides(definition.getId(), bankId);
//        if (overrides != null) {
//            for (MappingOverride override : overrides) {
//                result = override.applyTo(result);
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * Charge et enregistre les surcharges pour une banque spécifique
//     */
//    public void loadBankMappingOverrides(String bankId) {
//        List<MappingOverride> bankOverrides = configLoader.loadMappingOverrides(bankId);
//        for (MappingOverride override : bankOverrides) {
//            mappingRegistry.addOverride(override.getMappingId(), override);
//        }
//    }
//
//    /**
//     * Permet l'enregistrement dynamique d'une surcharge via une interface web.
//     */
//    public void registerMappingOverride(MappingOverride override) {
//        mappingRegistry.addOverride(override.getMappingId(), override);
//    }
//}
