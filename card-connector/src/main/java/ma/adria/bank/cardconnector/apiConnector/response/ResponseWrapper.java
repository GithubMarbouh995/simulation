package ma.adria.bank.cardconnector.apiConnector.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.adria.bank.dto.response.ResponseStatusEnum;
import ma.adria.bank.dto.response.error.ErrorResponse;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper <T>{
    private  ResponseStatusEnum status;
    private  String message;
    private  String codeMessage;
    private  T data;
    private  ErrorResponse error;
    private OffsetDateTime timestamp;
}
