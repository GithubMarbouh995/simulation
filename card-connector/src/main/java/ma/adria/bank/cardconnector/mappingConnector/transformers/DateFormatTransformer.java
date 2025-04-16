//package ma.adria.bank.cardconnector.mappingConnector.transformers;
//
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingContext;
//import ma.adria.bank.cardconnector.mappingConnector.core.transform.ValueTransformer;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class DateFormatTransformer implements ValueTransformer {
//    private String sourceFormat;
//    private String targetFormat;
//
//    public DateFormatTransformer() {}
//
//    public DateFormatTransformer(String sourceFormat, String targetFormat) {
//        this.sourceFormat = sourceFormat;
//        this.targetFormat = targetFormat;
//    }
//
//    @Override
//    public Object transform(Object value, MappingContext context) {
//        if (value == null) return null;
//        try {
//            SimpleDateFormat srcFormat = new SimpleDateFormat(sourceFormat);
//            SimpleDateFormat tgtFormat = new SimpleDateFormat(targetFormat);
//            Date date = srcFormat.parse(value.toString());
//            return tgtFormat.format(date);
//        } catch (Exception e) {
//            throw new RuntimeException("Erreur dans la transformation de date", e);
//        }
//    }
//
//    // Getters et setters
//    public String getSourceFormat() { return sourceFormat; }
//    public void setSourceFormat(String sourceFormat) { this.sourceFormat = sourceFormat; }
//    public String getTargetFormat() { return targetFormat; }
//    public void setTargetFormat(String targetFormat) { this.targetFormat = targetFormat; }
//
//    @Override
//    public String toString() {
//        return "DateFormatTransformer{" +
//                "sourceFormat='" + sourceFormat + '\'' +
//                ", targetFormat='" + targetFormat + '\'' +
//                '}';
//    }
//}
