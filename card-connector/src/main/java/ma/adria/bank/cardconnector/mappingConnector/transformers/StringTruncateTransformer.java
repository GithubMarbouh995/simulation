//package ma.adria.bank.cardconnector.mappingConnector.transformers;
//
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingContext;
//import ma.adria.bank.cardconnector.mappingConnector.core.transform.ValueTransformer;
//
//public class StringTruncateTransformer implements ValueTransformer {
//    private int maxLength;
//
//    public StringTruncateTransformer() {}
//
//    public StringTruncateTransformer(int maxLength) {
//        this.maxLength = maxLength;
//    }
//
//    @Override
//    public Object transform(Object value, MappingContext context) {
//        if (value == null) return null;
//        String str = value.toString();
//        return (str.length() <= maxLength) ? str : str.substring(0, maxLength);
//    }
//
//    public int getMaxLength() { return maxLength; }
//    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
//
//
//    @Override
//    public String toString() {
//        return "StringTruncateTransformer{" +
//                "maxLength=" + maxLength +
//                '}';
//    }
//}