

public class UndefinedTokenException extends Exception {
    public Token token;

    public UndefinedTokenException(Token token) {
        super();
        this.token = token;
    }
}