//package ma.adria.bank.cardconnector.mappingConnector.domain;
//
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingContext;
//import ma.adria.bank.cardconnector.mappingConnector.api.MappingService;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TransferMappingService {
//    private final MappingService mappingService;
//
//    public TransferMappingService(MappingService mappingService) {
//        this.mappingService = mappingService;
//    }
//
//    /**
//     * Transforme de manière générique un objet source en un objet cible.
//     *
//     * @param source l'objet source à transformer
//     * @param targetType la classe du type cible
//     * @param bankId identifiant de la banque pour appliquer les overrides spécifiques
//     * @param <S> type source
//     * @param <T> type cible
//     * @return l'objet transformé en type cible
//     */
//    public <S, T> T map(S source, Class<T> targetType, String bankId) {
//        MappingContext context = new MappingContext();
//        context.setBankId(bankId);
//        return mappingService.transform(source, targetType);
//    }
//}
