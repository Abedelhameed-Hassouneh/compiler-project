public class Token {
    String content;
    TokenType type;

    int line;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Token(String content, int line) {
        this.content = content;
        this.line = line;
    }

    public Token(String content, TokenType type) {
        this.content = content;
        this.type = type;
    }

    // Override equals and hashCode for proper functionality in HashMap
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        if (this.type == TokenType.INTEGER_VALUE && token.type == TokenType.INTEGER_VALUE) {
            return true;
        } else if (this.type == TokenType.REAL_VALUE && token.type == TokenType.REAL_VALUE) {
            return true;
        } else if (this.type == TokenType.USER_DEFINED && token.type == TokenType.USER_DEFINED) {
            return true;
        }
        return content.equals(token.content) && type == token.type;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "tokens.Token{" +
                "content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}


