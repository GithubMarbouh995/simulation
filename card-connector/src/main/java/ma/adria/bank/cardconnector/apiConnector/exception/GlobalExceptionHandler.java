package ma.adria.bank.cardconnector.apiConnector.exception;

import lombok.extern.slf4j.Slf4j;
import ma.adria.bank.cardconnector.apiConnector.response.ResponseWrapper;
import ma.adria.bank.dto.response.ResponseStatusEnum;
import ma.adria.bank.dto.response.error.ErrorDetail;
import ma.adria.bank.dto.response.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleInvalidRequest(InvalidRequestException ex) {
        log.warn("Erreur fonctionnelle capturée : {}", ex.getMessage());

        ResponseWrapper<Void> response = new ResponseWrapper<>();
        response.setStatus(ResponseStatusEnum.ERROR);
        response.setMessage(ex.getMessage());
        response.setCodeMessage("VAL-001");
        response.setTimestamp(OffsetDateTime.now());
        response.setError(new ErrorResponse("BAD_REQUEST", ex.getMessage(), null, null, null));

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorDetail("FIELD_VALIDATION", err.getDefaultMessage(), err.getField()))
                .collect(Collectors.toList());

        ResponseWrapper<Void> response = new ResponseWrapper<>();
        response.setStatus(ResponseStatusEnum.ERROR);
        response.setMessage("Erreur de validation");
        response.setCodeMessage("VAL-002");
        response.setTimestamp(OffsetDateTime.now());
        response.setError(new ErrorResponse("VALIDATION_ERROR", "Champs invalides", details, null, null));

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<Void>> handleGenericException(Exception ex) {
        log.error("Erreur inattendue : ", ex);

        ResponseWrapper<Void> response = new ResponseWrapper<>();
        response.setStatus(ResponseStatusEnum.ERROR);
        response.setMessage("Une erreur interne est survenue");
        response.setCodeMessage("INT-500");
        response.setTimestamp(OffsetDateTime.now());
        response.setError(new ErrorResponse("INTERNAL_ERROR", ex.getMessage(), null, null, null));

        return ResponseEntity.internalServerError().body(response);
    }
}
