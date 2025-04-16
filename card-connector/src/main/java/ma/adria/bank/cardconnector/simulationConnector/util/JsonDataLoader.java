package ma.adria.bank.cardconnector.simulationConnector.util;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.tomcat.util.file.ConfigurationSource;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class JsonDataLoader {
//
//    private final ObjectMapper objectMapper;
//
//    public JsonDataLoader(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    public <T> T loadJsonData(String jsonFilePath, Class<T> targetClass) {
//        try {
//            ClassPathResource resource = new ClassPathResource(jsonFilePath);
//            InputStream inputStream = resource.getInputStream();
//            return objectMapper.readValue(inputStream, targetClass);
//        } catch (IOException e) {
//            throw new RuntimeException("Erreur lors du chargement du fichier JSON: " + jsonFilePath, e);
//        }
//    }
//
////    public List<Map<String, Object>> loadJsonData(String resourcePath, Class<Map> mapClass) {
////        try {
////            ConfigurationSource.Resource resource = resourceLoader.getResource("classpath:" + resourcePath);
////
////            if (!resource.exists()) {
////                logger.error("Ressource non trouvée : {}", resourcePath);
////                throw new RuntimeException("Fichier non trouvé : " + resourcePath);
////            }
////
////            ObjectMapper mapper = new ObjectMapper();
////            JsonNode rootNode = mapper.readTree(resource.getInputStream());
////            JsonNode cardsNode = rootNode.get("cards");
////
////            if (cardsNode == null || !cardsNode.isArray()) {
////                logger.error("Structure JSON invalide : propriété 'cards' manquante ou pas un tableau");
////                return Collections.emptyList();
////            }
////
////            return mapper.convertValue(cardsNode,
////                    new TypeReference<List<Map<String, Object>>>() {});
////        } catch (IOException e) {
////            logger.error("Erreur lors du chargement du fichier JSON: {}", resourcePath, e);
////            throw new RuntimeException("Erreur lors du chargement du fichier JSON: " + resourcePath, e);
////        }
////    }
//}

//---version externe
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

@Component
public class JsonDataLoader {

    private final ObjectMapper objectMapper;

    public JsonDataLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T loadJsonData(String jsonFilePath, Class<T> targetClass, boolean isExternalPath) {
        try {
            Resource resource;
            if (isExternalPath) {
                resource = new FileSystemResource(jsonFilePath);
            } else {
                resource = new ClassPathResource(jsonFilePath);
            }

            if (!resource.exists()) {
                throw new RuntimeException("Fichier non trouvé: " + jsonFilePath);
            }

            return objectMapper.readValue(resource.getInputStream(), targetClass);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du fichier JSON: " + jsonFilePath, e);
        }
    }
}