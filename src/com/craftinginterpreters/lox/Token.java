package com.craftinginterpreters.lox;

class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String tokenTypeToString(TokenType tokentype) {
        return switch (tokentype) {
            case LEFT_PAREN ->    "LEFT_PAREN";
            case RIGHT_PAREN ->   "RIGHT_PAREN";
            case LEFT_BRACE ->    "LEFT_BRACE";
            case RIGHT_BRACE ->   "RIGHT_BRACE";
            case COMMA ->         "COMMA";
            case DOT ->           "DOT";
            case MINUS ->         "MINUS";
            case PLUS ->          "PLUS";
            case SEMICOLON ->     "SEMICOLON";
            case SLASH ->         "SLASH";
            case STAR ->          "STAR";
            case BANG ->          "BANG";
            case BANG_EQUAL ->    "BANG_EQUAL";
            case EQUAL ->         "EQUAL";
            case EQUAL_EQUAL ->   "EQUAL_EQUAL";
            case LESS ->          "LESS";
            case LESS_EQUAL ->    "LESS_EQUAL";
            case GREATER ->       "GREATER";
            case GREATER_EQUAL -> "GREATER_EQUAL";
            case IDENTIFIER ->    "IDENTIFIER";
            case STRING ->        "STRING";
            case NUMBER ->        "NUMBER";
            case AND ->           "AND";
            case OR ->            "OR";
            case IF ->            "IF";
            case ELSE ->          "ELSE";
            case FOR ->           "FOR";
            case WHILE ->         "WHILE";
            case TRUE ->          "TRUE";
            case FALSE ->         "FALSE";
            case FUN ->           "FUN";
            case RETURN ->        "RETURN";
            case CLASS ->         "CLASS";
            case THIS ->          "THIS";
            case VAR ->           "VAR";
            case PRINT ->         "PRINT";
            case SUPER ->         "SUPER";
            case NIL ->           "NIL";
            case EOF ->           "EOF";
            default -> "???";
        };
    }

    public void print() {
        System.out.printf("Line %d: Token-type = %s", line, tokenTypeToString(type));
        if(literal != null) {
            System.out.print(", Literal-value = ");
            System.out.println(literal);
        } else {
            System.out.println();
        }
    }
}
