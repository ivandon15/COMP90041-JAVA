package ethicalengine;

/**
 * A new exception for output the invalid characteristic
 * <p>
 * StudentID: 1150027
 * Username: Ivandon15
 *
 * @author YiFan Deng
 */
public class InvalidCharacteristicException extends RuntimeException {

    /**
     * Constructor with one parameter
     * @param message the error message
     */
    public InvalidCharacteristicException(String message) {
        super(message);
    }
}
