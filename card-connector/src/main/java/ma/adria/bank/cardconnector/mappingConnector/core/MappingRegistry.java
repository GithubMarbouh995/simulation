//package ma.adria.bank.cardconnector.mappingConnector.core;
//
//import ma.adria.bank.cardconnector.mappingConnector.core.definition.MappingDefinition;
//import ma.adria.bank.cardconnector.mappingConnector.override.MappingOverride;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
//
//@Component
//public class MappingRegistry {
//    private final Map<String, MappingDefinition> mappings = new HashMap<>();
//    // Structure : mappingId -> (bankId -> liste d’overrides)
//    private final Map<String, Map<String, List<MappingOverride>>> overrides = new HashMap<>();
//
//    /**
//     * Recherche un mapping correspondant aux types source et cible.
//     */
//    public MappingDefinition findMapping(String sourceType, String targetType) {
//        return mappings.values().stream()
//                .filter(m -> m.getSourceType().equals(sourceType) && m.getTargetType().equals(targetType))
//                .sorted(Comparator.comparingInt(MappingDefinition::getPriority))
//                .findFirst()
//                .orElse(null);
//    }
//
//    public void registerMapping(MappingDefinition mapping) {
//        mappings.put(mapping.getId(), mapping);
//    }
//
//    public void addOverride(String mappingId, MappingOverride override) {
//        String bankId = override.getBankId() != null ? override.getBankId() : "global";
//        overrides.putIfAbsent(mappingId, new HashMap<>());
//        Map<String, List<MappingOverride>> bankOverrides = overrides.get(mappingId);
//        bankOverrides.putIfAbsent(bankId, new ArrayList<>());
//        bankOverrides.get(bankId).add(override);
//    }
//
//    /**
//     * Récupère la liste des surcharges applicables pour un mapping et une banque donnée.
//     */
//    public List<MappingOverride> getOverrides(String mappingId, String bankId) {
//        Map<String, List<MappingOverride>> bankOverrides = overrides.get(mappingId);
//        if (bankOverrides == null) return null;
//        List<MappingOverride> result = new ArrayList<>();
//        if (bankOverrides.containsKey("global")) {
//            result.addAll(bankOverrides.get("global"));
//        }
//        if (bankId != null && bankOverrides.containsKey(bankId)) {
//            result.addAll(bankOverrides.get(bankId));
//        }
//        result.sort(Comparator.comparingInt(MappingOverride::getPriority));
//        return result;
//    }
//}