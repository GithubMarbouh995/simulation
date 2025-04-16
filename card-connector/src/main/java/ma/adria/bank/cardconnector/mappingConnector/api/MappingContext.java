//package ma.adria.bank.cardconnector.mappingConnector.api;
//
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//public class MappingContext {
//    private String bankId;
//    private Locale locale;
//    private Map<String, Object> additionalProperties = new HashMap<>();
//
//    public MappingContext() {
//    }
//
//    // Getters et setters pour bankId
//    public String getBankId() {
//        return bankId;
//    }
//
//    public void setBankId(String bankId) {
//        this.bankId = bankId;
//    }
//
//    // Getters et setters pour locale
//    public Locale getLocale() {
//        return locale;
//    }
//
//    public void setLocale(Locale locale) {
//        this.locale = locale;
//    }
//
//    // Getters et setters pour additionalProperties
//    public Map<String, Object> getAdditionalProperties() {
//        return additionalProperties;
//    }
//
//    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
//        this.additionalProperties = additionalProperties;
//    }
//
//    // Méthodes de convenance pour ajouter ou récupérer une propriété
//    public void putProperty(String key, Object value) {
//        additionalProperties.put(key, value);
//    }
//
//    public Object getProperty(String key) {
//        return additionalProperties.get(key);
//    }
//}
