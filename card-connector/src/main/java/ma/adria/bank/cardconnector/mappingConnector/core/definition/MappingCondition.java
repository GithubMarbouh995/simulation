//package ma.adria.bank.cardconnector.mappingConnector.core.definition;
//
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingContext;
//
//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.CLASS,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "@class"
//)
//public interface MappingCondition {
//    boolean evaluate(Object source, Object target, MappingContext context);
//    MappingCondition copy();
//}