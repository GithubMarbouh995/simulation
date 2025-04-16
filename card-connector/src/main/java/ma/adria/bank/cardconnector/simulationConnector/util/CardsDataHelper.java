package ma.adria.bank.cardconnector.simulationConnector.util;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class CardsDataHelper {
//
//    private final JsonDataLoader jsonDataLoader;
//
//    public CardsDataHelper(JsonDataLoader jsonDataLoader) {
//        this.jsonDataLoader = jsonDataLoader;
//    }
//
//    public List<Map<String, Object>> getAllCards() {
//        Map<String, Object> data = (Map<String, Object>) jsonDataLoader.loadJsonData("data/cards.json", Map.class);
//        System.out.println("Data loaded: " + data);
//        if (data != null && data.containsKey("cards")) {
//            return (List<Map<String, Object>>) data.get("cards");
//        }
//        return Collections.emptyList();
//    }
//
//    public Map<String, Object> getCardById(String cardId) {
//        List<Map<String, Object>> cards = getAllCards();
//        return cards.stream()
//                .filter(card -> cardId.equals(card.get("id")))
//                .findFirst()
//                .orElse(null);
//    }


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ma.adria.bank.cardconnector.simulationConnector.util.JsonDataLoader;

////    // Dans CardsDataHelper.getCardById
////    public Map<String, Object> getCardById(String cardId) {
////        // Afficher l'ID recherché pour déboguer
////        logger.info("Recherche de carte avec ID: {}", cardId);
////
////        // Récupérer toutes les cartes
////        List<Map<String, Object>> cards = getAllCards();
////
////        // Vérifier si nous avons des cartes
////        logger.info("Nombre de cartes chargées: {}", cards.size());
////
////        // Chercher avec l'ID exact
////        return cards.stream()
////                .filter(card -> cardId.equals(card.get("id")))
////                .findFirst()
////                .orElse(null);
////    }
//
//    public List<Map<String, Object>> getCardsByCustomerId(String customerId) {
//        List<Map<String, Object>> cards = getAllCards();
//        return cards.stream()
//                .filter(card -> customerId.equals(card.get("customerId")))
//                .toList();
//    }
//    /**
//     * Recherche une carte par sa référence de commande
//     * @param referenceCommande la référence de commande à rechercher
//     * @return les données de la carte ou null si aucune carte ne correspond
//     */
//    public Map<String, Object> getCardByReference(String referenceCommande) {
//        List<Map<String, Object>> cards = getAllCards();
//        return cards.stream()
//                .filter(card -> card.containsKey("referenceCommande") &&
//                        referenceCommande.equals(card.get("referenceCommande")))
//                .findFirst()
//                .orElse(null);
//    }
//
//    // Dans CardsDataHelper
//    public Map<String, Object> getCardByLastFourDigits(String lastFourDigits) {
//        List<Map<String, Object>> cards = getAllCards();
//        return cards.stream()
//                .filter(card -> {
//                    String cardNumber = (String) card.get("cardNumber");
//                    return cardNumber != null && cardNumber.endsWith(lastFourDigits);
//                })
//                .findFirst()
//                .orElse(null);
//    }
//}


//-----version 2
@Component
public class CardsDataHelper {

    @Value("${app.cards.file.path:data/cards.json}")
    private String cardsFilePath;

    @Value("${app.cards.file.external:false}")
    private boolean isExternalPath;

    private final JsonDataLoader jsonDataLoader;

    public CardsDataHelper(JsonDataLoader jsonDataLoader) {
        this.jsonDataLoader = jsonDataLoader;
    }

    public List<Map<String, Object>> getAllCards() {
        Map<String, Object> data = jsonDataLoader.loadJsonData(cardsFilePath, Map.class, isExternalPath);
        System.out.println("Data loaded: " + data);
        if (data != null && data.containsKey("cards")) {
            return (List<Map<String, Object>>) data.get("cards");
        }
        return Collections.emptyList();
    }

    public Map<String, Object> getCardById(String cardId) {
        List<Map<String, Object>> cards = getAllCards();
        return cards.stream()
                .filter(card -> cardId.equals(card.get("id")))
                .findFirst()
                .orElse(null);
    }
//    // Dans CardsDataHelper.getCardById
//    public Map<String, Object> getCardById(String cardId) {
//        // Afficher l'ID recherché pour déboguer
//        logger.info("Recherche de carte avec ID: {}", cardId);
//
//        // Récupérer toutes les cartes
//        List<Map<String, Object>> cards = getAllCards();
//
//        // Vérifier si nous avons des cartes
//        logger.info("Nombre de cartes chargées: {}", cards.size());
//
//        // Chercher avec l'ID exact
//        return cards.stream()
//                .filter(card -> cardId.equals(card.get("id")))
//                .findFirst()
//                .orElse(null);
//    }

    public List<Map<String, Object>> getCardsByCustomerId(String customerId) {
        List<Map<String, Object>> cards = getAllCards();
        return cards.stream()
                .filter(card -> customerId.equals(card.get("customerId")))
                .toList();
    }
    /**
     * Recherche une carte par sa référence de commande
     * @param referenceCommande la référence de commande à rechercher
     * @return les données de la carte ou null si aucune carte ne correspond
     */
    public Map<String, Object> getCardByReference(String referenceCommande) {
        List<Map<String, Object>> cards = getAllCards();
        return cards.stream()
                .filter(card -> card.containsKey("referenceCommande") &&
                        referenceCommande.equals(card.get("referenceCommande")))
                .findFirst()
                .orElse(null);
    }

    // Dans CardsDataHelper
    public Map<String, Object> getCardByLastFourDigits(String lastFourDigits) {
        List<Map<String, Object>> cards = getAllCards();
        return cards.stream()
                .filter(card -> {
                    String cardNumber = (String) card.get("cardNumber");
                    return cardNumber != null && cardNumber.endsWith(lastFourDigits);
                })
                .findFirst()
                .orElse(null);
    }
}