package dungeonmania.exceptions;

public class PlayerDeadException extends RuntimeException {
    public PlayerDeadException(String message) {
        super(message);
    }
}
