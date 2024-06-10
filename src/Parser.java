import java.util.*;

public class Parser {

    private List<Token> tokens;
    private HashMap<Token, HashMap<Token, Token[]>> table;

    int index = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        HashMap<Token, HashMap<Token, Token[]>> table = new HashMap<>();
        table.put(new Token("module-decl", TokenType.RULE), buildModuleDecl());
        table.put(new Token("module-heading", TokenType.RULE), buildModuleHeading());
        table.put(new Token("block", TokenType.RULE), buildBlock());
        table.put(new Token("declarations", TokenType.RULE), buildDeclarations());
        table.put(new Token("const-decl", TokenType.RULE), buildConstDecl());
        table.put(new Token("const-list", TokenType.RULE), buildConstList());
        table.put(new Token("var-decl", TokenType.RULE), buildVarDecl());
        table.put(new Token("var-list", TokenType.RULE), buildVarList());
        table.put(new Token("var-item", TokenType.RULE), buildVarItem());
        table.put(new Token("name-list", TokenType.RULE), buildNameList());
        table.put(new Token("more-names", TokenType.RULE), buildMoreNames());
        table.put(new Token("data-type", TokenType.RULE), buildDataType());
        table.put(new Token("procedure-decl", TokenType.RULE), buildProcedureDecl());
        table.put(new Token("procedure-heading", TokenType.RULE), buildProcedureHeading());
        table.put(new Token("stmt-list", TokenType.RULE), buildStmtList());
        table.put(new Token("statement", TokenType.RULE), buildStatement());
        table.put(new Token("ass-stmt", TokenType.RULE), buildAssStatement());
        table.put(new Token("exp", TokenType.RULE), buildExp());
        table.put(new Token("exp-prime", TokenType.RULE), buildExpPrime());
        table.put(new Token("term", TokenType.RULE), buildTerm());
        table.put(new Token("factor", TokenType.RULE), buildfactor());
        table.put(new Token("term-prime", TokenType.RULE), buildtermprime());
        table.put(new Token("add-oper", TokenType.RULE), buildaddoper());
        table.put(new Token("mul-oper", TokenType.RULE), buildmuloper());
        table.put(new Token("read-stmt", TokenType.RULE), buildreadstmt());
        table.put(new Token("write-stmt", TokenType.RULE), buildwritestmt());
        table.put(new Token("write-list", TokenType.RULE), buildwritelist());
        table.put(new Token("more-write-value", TokenType.RULE), buildmorewritevalue());
        table.put(new Token("write-item", TokenType.RULE), buildwriteitem());
        table.put(new Token("if-stmt", TokenType.RULE), buildifstmt());
        table.put(new Token("else-part", TokenType.RULE), buildelsepart());
        table.put(new Token("while-stmt", TokenType.RULE), buildswhilestmt());
        table.put(new Token("loop-stmt", TokenType.RULE), buildloopstmt());
        table.put(new Token("exit-stmt", TokenType.RULE), buildwxitstmt());
        table.put(new Token("call-stmt", TokenType.RULE), buildcallstmt());
        table.put(new Token("condition", TokenType.RULE), buildcondition());
        table.put(new Token("relational-oper", TokenType.RULE), buildrelationaloper());
        table.put(new Token("name-value", TokenType.RULE), buildnamevalue());
        table.put(new Token("value", TokenType.RULE), buildvalue());
        this.table = table;
    }

    public void parse() {
        Stack<Token> stack = new Stack<>();
        stack.push(new Token("module-decl", TokenType.RULE));

        while (true) {
            Token currentToken = tokens.get(index);
            Token tokenFromStack = stack.peek();

            if (stack.isEmpty() && isEmptyTokensList()) {
                System.out.println("Parse done succesfuly");
                return;
                //success
            } else if (!stack.isEmpty() && isEmptyTokensList()) {
                System.out.println("Error");
                System.out.println(stack.peek().content);
                return;
                //error
            } else if (stack.isEmpty() && !isEmptyTokensList()) {
                System.out.println("Error");
                System.out.println(currentToken);

                return;
                //error
            } else if (tokenFromStack.type == TokenType.RULE) {
                if (currentToken.type == TokenType.USER_DEFINED) {
                    currentToken.content = "name";
                } else if (currentToken.type == TokenType.INTEGER_VALUE || currentToken.type == TokenType.REAL_VALUE) {
                    currentToken.content = "";
                }
                Token[] tokensFromIntersection = table.get(tokenFromStack).get(currentToken);


                if (tokensFromIntersection == null) {
                    System.out.println("Error");
                    System.out.println("no intersection");
                    return;
                    //error
                } else {
                    if (tokensFromIntersection.length == 1 && tokensFromIntersection[0].type == TokenType.LAMBDA) {
                        stack.pop();
                        continue;
                    }
                    reverseArray(tokensFromIntersection);
                    stack.pop();
                    for (Token tokenInIntersection : tokensFromIntersection) {
                        stack.push(tokenInIntersection);
                    }
                }
            } else if (currentToken.type == TokenType.RESERVED_WORD && tokenFromStack.type == TokenType.RESERVED_WORD && currentToken.content.equals(tokenFromStack.content)) {
                index++;
                stack.pop();
            } else if (currentToken.type == TokenType.INTEGER_VALUE && tokenFromStack.type == TokenType.INTEGER_VALUE) {
                index++;
                stack.pop();
            } else if (currentToken.type == TokenType.REAL_VALUE && tokenFromStack.type == TokenType.REAL_VALUE) {
                index++;
                stack.pop();
            } else if (currentToken.type == TokenType.SPECIAL_SYMBOL && tokenFromStack.type == TokenType.SPECIAL_SYMBOL && currentToken.content.equals(tokenFromStack.content)) {
                index++;
                stack.pop();
            } else if (currentToken.type == TokenType.USER_DEFINED && tokenFromStack.type == TokenType.USER_DEFINED) {
                index++;
                stack.pop();
            } else {

                System.out.println();
            }
            System.out.println("end of loop");
        }
    }

    private boolean isEmptyTokensList() {
        return index + 1 == tokens.size();
    }

    public static void reverseArray(Token[] array) {
        List<Token> list = Arrays.asList(array);
        Collections.reverse(list);
        list.toArray(array);
    }


    private static HashMap<Token, Token[]> buildModuleDecl() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("module", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("module-heading", TokenType.RULE),
                        new Token("declarations", TokenType.RULE),
                        new Token("block", TokenType.RULE),
                        new Token("name", TokenType.USER_DEFINED),
                });


        return map;
    }

    private static HashMap<Token, Token[]> buildModuleHeading() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("module", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("module", TokenType.RESERVED_WORD),
                        new Token("name", TokenType.USER_DEFINED),
                        new Token(";", TokenType.SPECIAL_SYMBOL)
                });
        return map;
    }

    private static HashMap<Token, Token[]> buildBlock() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("begin", TokenType.RESERVED_WORD),
                        new Token("stmt-list", TokenType.RULE),
                        new Token("end", TokenType.RESERVED_WORD)
                });
        return map;
    }

    private static HashMap<Token, Token[]> buildDeclarations() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("const-decl", TokenType.RULE),
                        new Token("var-decl", TokenType.RULE),
                        new Token("procedure-decl", TokenType.RULE)
                });

        map.put(new Token("const", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("const-decl", TokenType.RULE),
                        new Token("var-decl", TokenType.RULE),
                        new Token("procedure-decl", TokenType.RULE)
                });

        map.put(new Token("var", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("const-decl", TokenType.RULE),
                        new Token("var-decl", TokenType.RULE),
                        new Token("procedure-decl", TokenType.RULE)
                });

        map.put(new Token("procedure", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("const-decl", TokenType.RULE),
                        new Token("var-decl", TokenType.RULE),
                        new Token("procedure-decl", TokenType.RULE)
                });


        return map;
    }

    private static HashMap<Token, Token[]> buildConstDecl() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token("const", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("const", TokenType.RESERVED_WORD),
                        new Token("const-list", TokenType.RULE)
                });


        map.put(new Token("var", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token("procedure", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });
        return map;
    }

    private static HashMap<Token, Token[]> buildConstList() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("name", TokenType.USER_DEFINED),
                        new Token("=", TokenType.SPECIAL_SYMBOL),
                        new Token("value", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("const-list", TokenType.RULE),
                });

        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token("var", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });
        map.put(new Token("procedure", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        return map;
    }

    private static HashMap<Token, Token[]> buildVarDecl() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token("var", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("var", TokenType.RESERVED_WORD),
                        new Token("var-list", TokenType.RULE),
                });

        map.put(new Token("procedure", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });
        return map;
    }

    private static HashMap<Token, Token[]> buildVarList() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("var-item", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("var-list", TokenType.RULE),
                });

        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token("procedure", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        return map;
    }

    private static HashMap<Token, Token[]> buildVarItem() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("name-list", TokenType.RULE),
                        new Token(":", TokenType.SPECIAL_SYMBOL),
                        new Token("data-type", TokenType.RULE),
                });

        return map;
    }

    private static HashMap<Token, Token[]> buildNameList() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("name", TokenType.USER_DEFINED),
                        new Token("more-names", TokenType.RULE),
                });
        return map;
    }

    private static HashMap<Token, Token[]> buildMoreNames() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token(":", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });
        map.put(new Token(",", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token(",", TokenType.SPECIAL_SYMBOL),
                        new Token("name-list", TokenType.RULE),

                });

        map.put(new Token(")", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });
        return map;
    }

    private static HashMap<Token, Token[]> buildDataType() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("integer", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("integer", TokenType.RESERVED_WORD),
                });

        map.put(new Token("real", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("real", TokenType.RESERVED_WORD),
                });

        map.put(new Token("real", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("real", TokenType.RESERVED_WORD),
                });

        return map;
    }

    private static HashMap<Token, Token[]> buildProcedureDecl() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });


        map.put(new Token("procedure", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("procedure-heading", TokenType.RULE),
                        new Token("declarations", TokenType.RULE),
                        new Token("block", TokenType.RULE),
                        new Token("name", TokenType.USER_DEFINED),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("procedure-decl", TokenType.RULE)

                }
        );
        return map;
    }

    private static HashMap<Token, Token[]> buildProcedureHeading() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("procedure", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("procedure", TokenType.RESERVED_WORD),
                        new Token("name", TokenType.USER_DEFINED),
                        new Token(";", TokenType.SPECIAL_SYMBOL)
                }
        );
        return map;
    }

    private static HashMap<Token, Token[]> buildStmtList() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token(";", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );
        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );


        map.put(new Token("end", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token("readint", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("readreal", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("readchar", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("readln", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("writeint", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );
        map.put(new Token("writereal", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );
        map.put(new Token("writechar", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("writeln", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("if", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("else", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token("while", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );
        map.put(new Token("loop", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("until", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });


        map.put(new Token("exit", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );

        map.put(new Token("call", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("statement", TokenType.RULE),
                        new Token(";", TokenType.SPECIAL_SYMBOL),
                        new Token("stmt-list", TokenType.RULE)
                }
        );


        return map;
    }

    private static HashMap<Token, Token[]> buildStatement() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("ass-stmt", TokenType.RULE)
                }
        );


        map.put(new Token(";", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token("begin", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("block", TokenType.RULE),
                });


        map.put(new Token("readint", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("read-stmt", TokenType.RULE),
                });

        map.put(new Token("readreal", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("read-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("readchar", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("read-stmt", TokenType.RULE),
                }
        );


        map.put(new Token("readln", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("read-stmt", TokenType.RULE),
                }
        );


        map.put(new Token("writeint", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("write-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("writereal", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("write-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("writechar", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("write-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("writeln", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("write-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("if", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("if-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("while", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("while-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("loop", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("loop-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("exit", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("exit-stmt", TokenType.RULE),
                }
        );

        map.put(new Token("call", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("call-stmt", TokenType.RULE),
                }
        );
        return map;
    }

    private static HashMap<Token, Token[]> buildAssStatement() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("name", TokenType.USER_DEFINED),
                        new Token(":=", TokenType.SPECIAL_SYMBOL),
                        new Token("exp", TokenType.RULE)
                }


        );
        return map;
    }

    private static HashMap<Token, Token[]> buildExp() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("term", TokenType.RULE),
                        new Token("exp-prime", TokenType.RULE)
                }
        );

        map.put(new Token("(", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("term", TokenType.RULE),
                        new Token("exp-prime", TokenType.RULE)
                }
        );
        map.put(new Token("(", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("term", TokenType.RULE),
                        new Token("exp-prime", TokenType.RULE)
                }
        );

        map.put(new Token("", TokenType.INTEGER_VALUE),
                new Token[]{
                        new Token("term", TokenType.RULE),
                        new Token("exp-prime", TokenType.RULE)
                }
        );

        map.put(new Token("", TokenType.REAL_VALUE),
                new Token[]{
                        new Token("term", TokenType.RULE),
                        new Token("exp-prime", TokenType.RULE)
                }
        );
        return map;
    }

    private static HashMap<Token, Token[]> buildExpPrime() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token(";", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });

        map.put(new Token(")", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA),
                });


        map.put(new Token("+", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("add-oper", TokenType.RULE),
                        new Token("term", TokenType.RULE),
                        new Token("exp-oper", TokenType.RULE),
                });

        map.put(new Token("-", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("add-oper", TokenType.RULE),
                        new Token("term", TokenType.RULE),
                        new Token("exp-oper", TokenType.RULE),
                });


        return map;
    }

    private static HashMap<Token, Token[]> buildTerm() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("factor", TokenType.RULE),
                        new Token("term-prime", TokenType.RULE)
                }
        );

        map.put(new Token("(", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("factor", TokenType.RULE),
                        new Token("term-prime", TokenType.RULE)
                }
        );

        map.put(new Token("", TokenType.INTEGER_VALUE),
                new Token[]{
                        new Token("factor", TokenType.RULE),
                        new Token("term-prime", TokenType.RULE)
                }
        );

        map.put(new Token("", TokenType.REAL_VALUE),
                new Token[]{
                        new Token("factor", TokenType.RULE),
                        new Token("term-prime", TokenType.RULE)
                }
        );

        return map;
    }

    public static HashMap<Token, Token[]> buildtermprime() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token(";", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA)
                });
        map.put(new Token(")", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA)
                });
        map.put(new Token("+", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA)
                });
        map.put(new Token("-", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA)
                });
        map.put(new Token("*", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("mul-oper", TokenType.RULE),
                        new Token("factor", TokenType.RULE),
                        new Token("term-prime", TokenType.RULE)
                });
        map.put(new Token("/", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("mul-oper", TokenType.RULE),
                        new Token("factor", TokenType.RULE),
                        new Token("term-prime", TokenType.RULE)
                });
        map.put(new Token("mod", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("mul-oper", TokenType.RULE),
                        new Token("factor", TokenType.RULE),
                        new Token("term-prime", TokenType.RULE)
                });
        map.put(new Token("div", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("mul-oper", TokenType.RULE),
                        new Token("factor", TokenType.RULE),
                        new Token("term-prime", TokenType.RULE)
                });
        return map;

    }

    public static HashMap<Token, Token[]> buildfactor() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("name-value", TokenType.RULE)
                });
        map.put(new Token("(", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("(", TokenType.SPECIAL_SYMBOL),
                        new Token("exp", TokenType.RULE),
                        new Token(")", TokenType.SPECIAL_SYMBOL)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildaddoper() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("+", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("+", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("-", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("-", TokenType.SPECIAL_SYMBOL)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildmuloper() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("*", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("*", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("/", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("/", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("mod", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("mod", TokenType.RESERVED_WORD)
                });
        map.put(new Token("div", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("div", TokenType.RESERVED_WORD)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildreadstmt() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("readint", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("readint", TokenType.RESERVED_WORD),
                        new Token("(", TokenType.SPECIAL_SYMBOL),
                        new Token("name-list", TokenType.RULE),
                        new Token(")", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("readreal", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("readreal", TokenType.RESERVED_WORD),
                        new Token("(", TokenType.SPECIAL_SYMBOL),
                        new Token("name-list", TokenType.RULE),
                        new Token(")", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("readchar", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("readchar", TokenType.RESERVED_WORD),
                        new Token("(", TokenType.SPECIAL_SYMBOL),
                        new Token("name-list", TokenType.RULE),
                        new Token(")", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("readln", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("readln", TokenType.RESERVED_WORD)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildwritestmt() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("writeint", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("writeint", TokenType.RESERVED_WORD),
                        new Token("(", TokenType.SPECIAL_SYMBOL),
                        new Token("write-list", TokenType.RULE),
                        new Token(")", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("writereal", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("writereal", TokenType.RESERVED_WORD),
                        new Token("(", TokenType.SPECIAL_SYMBOL),
                        new Token("write-list", TokenType.RULE),
                        new Token(")", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("writechar", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("writechar", TokenType.RESERVED_WORD),
                        new Token("(", TokenType.SPECIAL_SYMBOL),
                        new Token("write-list", TokenType.RULE),
                        new Token(")", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("writeln", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("writeln", TokenType.RESERVED_WORD),
                        new Token("(", TokenType.SPECIAL_SYMBOL),
                        new Token("write-list", TokenType.RULE),
                        new Token(")", TokenType.SPECIAL_SYMBOL)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildwritelist() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("write-item", TokenType.RULE),
                        new Token("more-write-value", TokenType.RULE)

                });
        map.put(new Token("", TokenType.INTEGER_VALUE),
                new Token[]{
                        new Token("write-item", TokenType.RULE),
                        new Token("more-write-value", TokenType.RULE)
                });
        map.put(new Token("", TokenType.REAL_VALUE),
                new Token[]{
                        new Token("write-item", TokenType.RULE),
                        new Token("more-write-value", TokenType.RULE)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildmorewritevalue() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token(",", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token(",", TokenType.SPECIAL_SYMBOL),
                        new Token("write-list", TokenType.RULE)
                });
        map.put(new Token(")", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("", TokenType.LAMBDA)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildwriteitem() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("name", TokenType.USER_DEFINED)
                });
        map.put(new Token("", TokenType.REAL_VALUE),
                new Token[]{
                        new Token("value", TokenType.RULE)
                });
        map.put(new Token("", TokenType.INTEGER_VALUE),
                new Token[]{
                        new Token("value", TokenType.RULE)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildifstmt() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("if", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("if", TokenType.RESERVED_WORD),
                        new Token("condition", TokenType.RULE),
                        new Token("then", TokenType.RESERVED_WORD),
                        new Token("stmt-list", TokenType.RULE),
                        new Token("else-part", TokenType.RULE),
                        new Token("end", TokenType.RESERVED_WORD)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildelsepart() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("end", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("", TokenType.LAMBDA)
                });
        map.put(new Token("end", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("else", TokenType.RESERVED_WORD),
                        new Token("stmt-list", TokenType.RULE)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildswhilestmt() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("while", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("while", TokenType.RESERVED_WORD),
                        new Token("condition", TokenType.RULE),
                        new Token("do", TokenType.RESERVED_WORD),
                        new Token("stmt-list", TokenType.RULE),
                        new Token("end", TokenType.RESERVED_WORD)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildloopstmt() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("loop", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("loop", TokenType.RESERVED_WORD),
                        new Token("stmt-list", TokenType.RULE),
                        new Token("until", TokenType.RESERVED_WORD),
                        new Token("condition", TokenType.RULE)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildwxitstmt() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("exit", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("exit", TokenType.RESERVED_WORD)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildcallstmt() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("call", TokenType.RESERVED_WORD),
                new Token[]{
                        new Token("call", TokenType.RESERVED_WORD),
                        new Token("name", TokenType.USER_DEFINED)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildcondition() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("name-value", TokenType.RULE),
                        new Token("relational-oper", TokenType.RULE),
                        new Token("name-value", TokenType.RULE)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildrelationaloper() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("=", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("=", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("|=", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("|=", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("<", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("<", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token("<=", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token("<=", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token(">", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token(">", TokenType.SPECIAL_SYMBOL)
                });
        map.put(new Token(">=", TokenType.SPECIAL_SYMBOL),
                new Token[]{
                        new Token(">=", TokenType.SPECIAL_SYMBOL)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildnamevalue() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("name", TokenType.USER_DEFINED),
                new Token[]{
                        new Token("name", TokenType.USER_DEFINED)
                });
        map.put(new Token("", TokenType.INTEGER_VALUE),
                new Token[]{
                        new Token("value", TokenType.RULE)
                });
        map.put(new Token("", TokenType.REAL_VALUE),
                new Token[]{
                        new Token("value", TokenType.RULE)
                });
        return map;
    }

    public static HashMap<Token, Token[]> buildvalue() {
        HashMap<Token, Token[]> map = new HashMap<>();
        map.put(new Token("", TokenType.INTEGER_VALUE),
                new Token[]{
                        new Token("", TokenType.INTEGER_VALUE)
                });
        map.put(new Token("", TokenType.REAL_VALUE),
                new Token[]{
                        new Token("", TokenType.REAL_VALUE)
                });
        return map;
    }
}
