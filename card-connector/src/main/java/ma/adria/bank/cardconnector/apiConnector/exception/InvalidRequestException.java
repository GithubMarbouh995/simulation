package ma.adria.bank.cardconnector.apiConnector.exception;

public class InvalidRequestException extends RuntimeException{

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
