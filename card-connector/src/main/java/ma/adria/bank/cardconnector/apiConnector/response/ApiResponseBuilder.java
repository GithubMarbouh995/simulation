package ma.adria.bank.cardconnector.apiConnector.response;

import ma.adria.bank.dto.response.ResponseStatusEnum;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

public class ApiResponseBuilder {
    public static  <T> ResponseEntity<ResponseWrapper<T>> buildSuccessResponse(String message, T data) {
        ResponseWrapper<T> response = new ResponseWrapper<>();
        response.setStatus(ResponseStatusEnum.SUCCESS);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.ok(response);
    }

    public static  <T> ResponseEntity<ResponseWrapper<T>> buildResponse(boolean success, String message, T data) {
        ResponseWrapper<T> response = new ResponseWrapper<>();
        response.setStatus(success ? ResponseStatusEnum.SUCCESS : ResponseStatusEnum.ERROR);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.ok(response);
    }
}
