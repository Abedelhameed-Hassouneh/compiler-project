
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scanner {
    HashSet specialSymbolsSet = new HashSet<String>();
    HashSet reservedWordsSet = new HashSet<String>();
    String integersRegex = "\\d+";

    String realValuesRegex = "(\\d+\\.\\d*|\\.\\d+|\\d+)";

    String userDefinedRegex = "[a-zA-Z][a-zA-Z0-9]*";
    List<Line> linesList;


    public Scanner() {
        initSpecialSymbolsSet();
        initReservedWordsSet();
    }


    public List<Token> generateTokensList(File file) throws UndefinedTokenException {
        linesList = getLines(file);
        String input = addLines();
        ArrayList tokensList = new ArrayList<Token>();
        int line = 1;


        // Regular expression patterns for special tokens and reserved words
        Pattern pattern = Pattern.compile(
                "(\\+)|(-)|[+-]?\\d+|(=)|(<>)|(<=)|(>=)|(<)|(>)|(\\()|(\\))|(\\*)|(/)|(%)|(,)|(:=)|(:)|(;)|(\\.)|\\b(project|const|var|int|routine|start|end|input|output|if|then|endif|else|loop|do)\\b|[a-zA-Z][a-zA-Z0-9]*|\\r?\\n"

        );
        Matcher matcher = pattern.matcher(input);

        int currentIndex = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String matchedToken = matcher.group();


            // if the token wasn't recognized by the matcher then it's an error
            if (start > currentIndex) {
                handleScannerError(input.substring(currentIndex, start), line);
            }

            // add the matched token to the tokens list if it isn't a new line
            if (matchedToken.equals("\n")) {
                line++;
            } else {
                Token token = checkToken(matchedToken, line);
                tokensList.add(token);
            }

            currentIndex = end;
        }

        // if the remaining text wasn't recognized by the matcher then it's an error
        if (currentIndex < input.length()) {
            handleScannerError(input.substring(currentIndex), line);
        }

        return tokensList;
    }


    static List<Line> getLines(File file) {
        List lines = new ArrayList<Line>();
        try (FileReader fileReader = new FileReader(file.getPath());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            int start = 0;


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line + "\n";
                lines.add(new Line(line, start));
                start = (line.length() + start);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    /**
     * Initialize the special symbol set
     */
    private void initSpecialSymbolsSet() {
        specialSymbolsSet.addAll(Arrays.asList("=", "<>", "<", "<=", ">", ">=", "(", ")", "*", "/", "%", "+", "-", ":=", ";", ".", ":", ","));
    }

    /**
     * Initialize the reserved word set
     */
    private void initReservedWordsSet() {
        reservedWordsSet.addAll(Arrays.asList("module","begin","end","const","var","integer","real","char","procedure","mod","div","readint","readreal","readchar","readln","writeint","writereal","writechar","writeln","if","then","while","else","do","loop","until","exit","call"));
    }

    /**
     * Check the token type
     * The token can be a reserved word or a special symbol or an integer or a name
     *
     * @param str
     * @param index
     * @return
     */
    private Token checkToken(String str, int index) {
        Token token = new Token(str, index);
        if (specialSymbolsSet.contains(str)) {
            token.setType(TokenType.SPECIAL_SYMBOL);
        } else if (reservedWordsSet.contains(str)) {
            token.setType(TokenType.RESERVED_WORD);
        } else if (str.matches(integersRegex)) {
            token.setType(TokenType.INTEGER_VALUE);
        } else if (str.matches(realValuesRegex)) {
            token.setType(TokenType.REAL_VALUE);
        } else if (str.matches(userDefinedRegex)) {
            token.setType(TokenType.USER_DEFINED);
        }
        return token;
    }

    /**
     * This method is responsible for adding all the lines together in 1 string
     *
     * @return
     */
    private String addLines() {
        String output = "";

        for (Line l : linesList) {
            output += l.str;
        }

        return output;
    }

    private void handleScannerError(String token, int index) throws UndefinedTokenException {
        if (!token.matches("\\s*")) {
            throw new UndefinedTokenException(new Token(token, index));
        }
    }
}
