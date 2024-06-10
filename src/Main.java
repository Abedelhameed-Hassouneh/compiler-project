import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {


        Scanner scanner = new Scanner();
        List<Token> tokens;
        try {
            tokens = scanner.generateTokensList(new File("x.txt"));
        } catch (UndefinedTokenException e) {
            System.out.println("Error in line " + e.token.getLine() + ": Unexpected token " + e.token.getContent() + " during scanning process");
            return;
        }

        Parser parser = new Parser(tokens);
        parser.parse();
        System.out.println("hello");

    }

}
