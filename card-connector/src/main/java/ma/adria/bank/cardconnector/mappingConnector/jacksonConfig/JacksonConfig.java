//package ma.adria.bank.cardconnector.mappingConnector.jacksonConfig;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
//import ma.adria.bank.cardconnector.mappingConnector.transformers.DateFormatTransformer;
//import ma.adria.bank.cardconnector.mappingConnector.transformers.EnumMappingTransformer;
//import ma.adria.bank.cardconnector.mappingConnector.transformers.PrefixTransformer;
//import ma.adria.bank.cardconnector.mappingConnector.transformers.StringTruncateTransformer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class JacksonConfig {
//    @Bean
//    public ObjectMapper yamlMapper() {
//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//
//        // Activer la sérialisation/désérialisation polymorphe pour comprendre le @class dans les YAML
//        mapper.registerModule(new SimpleModule() {
//            @Override
//            public void setupModule(SetupContext context) {
//                super.setupModule(context);
//
//                // Enregistrer tous vos transformateursn:ackson doit savoir à l'avance quelles classes il est autorisé à créer
//                context.registerSubtypes(
//                        DateFormatTransformer.class,
//                        EnumMappingTransformer.class,
//                        PrefixTransformer.class,
//                        StringTruncateTransformer.class
//                );
//            }
//        });
//
//        return mapper;
//    }
//}
