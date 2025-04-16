//package ma.adria.bank.cardconnector.mappingConnector.core.definition;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import ma.adria.bank.cardconnector.mappingConnector.exception.MappingException;
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingContext;
//import ma.adria.bank.cardconnector.mappingConnector.core.transform.ValueTransformer;
//import org.springframework.expression.Expression;
//import org.springframework.expression.ExpressionParser;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class FieldMapping {
//    private String sourcePath;
//    private String targetPath;
//    private ValueTransformer transformer;
//    private MappingCondition condition;
//
//    private transient Expression sourceExpression;
//    private static final ExpressionParser PARSER = new SpelExpressionParser();
//
//    /**
//     * Applique ce mapping de champ entre l'objet source et l'objet cible.
//     */
//    public void apply(Object source, Object target, MappingContext context) {
//        if (condition != null && !condition.evaluate(source, target, context)) {
//            return;
//        }
//
//
//        try {
//            Object sourceValue = getSourceValue(source);
//            System.out.println("xxxxxxxxxxxxxxxxx");
//            System.out.println(sourceValue);
//            System.out.println("xxxxxxxxxxxxxxxxx");
//            // Transformation éventuelle
//            if (transformer != null) {
//                sourceValue = transformer.transform(sourceValue, context);
//            }
//            // Affectation de la valeur dans l'objet cible
//            setTargetValue(target, sourceValue);
//            System.out.println(sourceValue);
//        } catch (Exception e) {
//            throw new MappingException("Échec du mapping du champ : " + sourcePath + " -> " + targetPath, e);
//        }
//    }
//
//    /**
//     * Extrait la valeur depuis l'objet source en utilisant une expression SpEL.
//     */
//    private Object getSourceValue(Object source) {
//        if (sourceExpression == null) {
//            sourceExpression = PARSER.parseExpression(sourcePath);
//        }
//        return sourceExpression.getValue(source);
//    }
//
//    /**
//     * Affecte la valeur dans l'objet cible en utilisant la réflexion.
//     */
//    private void setTargetValue(Object target, Object value) {
//        try {
//            String setterName = "set" + Character.toUpperCase(targetPath.charAt(0)) + targetPath.substring(1);
//            for (java.lang.reflect.Method method : target.getClass().getMethods()) {
//                if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
//                    method.invoke(target, value);
//                    return;
//                }
//            }
//            throw new NoSuchMethodException("Setter introuvable pour la propriété " + targetPath);
//        } catch (Exception e) {
//            throw new MappingException("Impossible d'affecter la valeur à " + targetPath, e);
//        }
//    }
//
//    /**
//     * Crée une copie de ce mapping de champ.
//     */
//    public FieldMapping copy() {
//        FieldMapping copy = new FieldMapping();
//        copy.sourcePath = this.sourcePath;
//        copy.targetPath = this.targetPath;
//        copy.transformer = this.transformer; // supposé immuable
//        copy.condition = (this.condition != null) ? this.condition.copy() : null;
//        return copy;
//    }
//
//    // Getters et setters
//    public String getSourcePath() { return sourcePath; }
//    public void setSourcePath(String sourcePath) { this.sourcePath = sourcePath; }
//
//    public String getTargetPath() { return targetPath; }
//    public void setTargetPath(String targetPath) { this.targetPath = targetPath; }
//
//    public ValueTransformer getTransformer() { return transformer; }
//    public void setTransformer(ValueTransformer transformer) { this.transformer = transformer; }
//
//    public MappingCondition getCondition() { return condition; }
//    public void setCondition(MappingCondition condition) { this.condition = condition; }
//
//    @Override
//    public String toString() {
//        return "FieldMapping{" +
//                "sourcePath='" + sourcePath + '\'' +
//                ", targetPath='" + targetPath + '\'' +
//                ", transformer=" + transformer +
//                ", condition=" + condition +
//                ", sourceExpression=" + sourceExpression +
//                '}';
//    }
//}
