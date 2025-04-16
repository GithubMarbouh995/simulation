//package ma.adria.bank.cardconnector.mappingConnector.transformers;
//
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingContext;
//import ma.adria.bank.cardconnector.mappingConnector.core.transform.ValueTransformer;
//
//import java.util.Map;
//
//public class EnumMappingTransformer implements ValueTransformer {
//    private Map<String, String> mappings;
//
//    public EnumMappingTransformer() {}
//
//    public EnumMappingTransformer(Map<String, String> mappings) {
//
//        this.mappings = mappings;
//    }
//
//    @Override
//    public Object transform(Object value, MappingContext context) {
//        if (value == null) return null;
//        String key = value.toString();
//        return mappings.getOrDefault(key, key);
//    }
//
//    public Map<String, String> getMappings() {
//        return mappings;
//    }
//    public void setMappings(Map<String, String> mappings) {
//        this.mappings = mappings;
//    }
//
//    @Override
//    public String toString() {
//        return "EnumMappingTransformer{" +
//                "mappings=" + mappings +
//                '}';
//    }
//}
