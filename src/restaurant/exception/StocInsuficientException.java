package restaurant.exception;

public class StocInsuficientException extends Exception {
    public StocInsuficientException(String mesaj) {
        super(mesaj);
    }
}