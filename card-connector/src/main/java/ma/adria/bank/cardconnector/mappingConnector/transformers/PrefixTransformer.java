//package ma.adria.bank.cardconnector.mappingConnector.transformers;
//
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingContext;
//import ma.adria.bank.cardconnector.mappingConnector.core.transform.ValueTransformer;
//
//public class PrefixTransformer implements ValueTransformer {
//    private String prefix;
//
//    public PrefixTransformer() {}
//
//    public PrefixTransformer(String prefix) {
//        this.prefix = prefix;
//    }
//
//    @Override
//    public Object transform(Object value, MappingContext context) {
//
//        if (value == null) return null;
//
//        return prefix + value.toString();
//    }
//
//    public String getPrefix() { return prefix; }
//    public void setPrefix(String prefix) { this.prefix = prefix; }
//
//
//    @Override
//    public String toString() {
//        return "PrefixTransformer{" +
//                "prefix='" + prefix + '\'' +
//                '}';
//    }
//}