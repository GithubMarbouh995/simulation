//package ma.adria.bank.cardconnector.mappingConnector.override;
//
//import ma.adria.bank.cardconnector.mappingConnector.core.definition.FieldMapping;
//import ma.adria.bank.cardconnector.mappingConnector.core.definition.MappingDefinition;
//import ma.adria.bank.cardconnector.mappingConnector.core.override.FieldOverride;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MappingOverride {
//
//    private String mappingId;
//    private String bankId;
//    private Map<String, FieldOverride> fieldOverrides = new HashMap<>();
//    private int priority;
//
//    /**
//     * Applique cette surcharge à une définition de mapping.
//     */
//    public MappingDefinition applyTo(MappingDefinition definition) {
//        if (!definition.getId().equals(mappingId)) {
//            throw new IllegalArgumentException("L'override (mappingId) ne correspond pas à la définition");
//        }
//        // On part du principe que la définition est déjà une copie
//        for (Map.Entry<String, FieldOverride> entry : fieldOverrides.entrySet()) {
//            String targetPath = entry.getKey();
//            FieldOverride override = entry.getValue();
//            FieldMapping newMapping = override.createFieldMapping();
//            definition.replaceFieldMapping(targetPath, newMapping);
//        }
//        return definition;
//    }
//
//    // Getters et setters
//    public String getMappingId() { return mappingId; }
//    public void setMappingId(String mappingId) { this.mappingId = mappingId; }
//
//    public String getBankId() { return bankId; }
//    public void setBankId(String bankId) { this.bankId = bankId; }
//
//    public Map<String, FieldOverride> getFieldOverrides() { return fieldOverrides; }
//    public void setFieldOverrides(Map<String, FieldOverride> fieldOverrides) { this.fieldOverrides = fieldOverrides; }
//
//    public int getPriority() { return priority; }
//    public void setPriority(int priority) { this.priority = priority; }
//
//    /**
//     * Ajoute une surcharge de champ.
//     */
//    public void addFieldOverride(String targetPath, FieldOverride override) {
//        fieldOverrides.put(targetPath, override);
//    }
//
//
//}
