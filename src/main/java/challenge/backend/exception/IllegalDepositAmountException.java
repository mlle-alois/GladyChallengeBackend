package challenge.backend.exception;

public class IllegalDepositAmountException extends RuntimeException {
    public IllegalDepositAmountException(final String message) {
        super(message);
    }
}
