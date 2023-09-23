package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and",    TokenType.AND);
        keywords.put("class",  TokenType.CLASS);
        keywords.put("else",   TokenType.ELSE);
        keywords.put("false",  TokenType.FALSE);
        keywords.put("for",    TokenType.FOR);
        keywords.put("fun",    TokenType.FUN);
        keywords.put("if",     TokenType.IF);
        keywords.put("nil",    TokenType.NIL);
        keywords.put("or",     TokenType.OR);
        keywords.put("print",  TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super",  TokenType.SUPER);
        keywords.put("this",   TokenType.THIS);
        keywords.put("true",   TokenType.TRUE);
        keywords.put("var",    TokenType.VAR);
        keywords.put("while",  TokenType.WHILE);
    }

    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch(c) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;

            case '!':
                addToken(match('=')? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=')? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=')? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=')? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;

            case '/':
                if(match('/')) {
                    while(peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else if(match('*')) {
                    advance();
                    while(!(peek() == '*' && peekNext() == '/') && !isAtEnd()) {
                        advance();
                    }
                    if(!isAtEnd()) {
                        advance();
                        advance();
                    }
                }
                else {
                    addToken(TokenType.SLASH);
                }
                break;

            // Ignore whitespace
            case ' ':
            case '\r':
            case '\t':
                break;

            case '\n':
                line++;
                break;

            case '"':
                scanString();
                break;

            default:
                if(isDigit(c)) {
                    scanNumber();
                } else if(isAlpha(c)) {
                    scanIdentifier();
                } else {
                    Lox.error(line, "Unexpected character.");
                }
                break;
        }
    }

    private void scanString() {
        while(peek() != '"' && !isAtEnd()) {
            if(peek() == '\n') { line++; }
            advance();
        }

        if(isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        String str = source.substring(start + 1, current);
        addToken(TokenType.STRING, str);

        advance();
    }

    private void scanNumber() {
        while(isDigit(peek())) {
            advance();
        }

        if(peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) {
                advance();
            }
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void scanIdentifier() {
        while(isAlphaNumeric(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if(type == null) { type = TokenType.IDENTIFIER; }

        addToken(type);
    }

    // Checks if the current token being processed matches the succeeding char
    // In this implementation, the "current" counter is one step ahead of the current char being processed
    private boolean match(char expected) {
        if(isAtEnd()) { return false; }
        if(source.charAt(current) != expected) { return false; }

        current++;
        return true;
    }

    // Fetches the current char in the source string, '\0' if at file's end
    private char peek() {
            if(isAtEnd()) { return '\0'; }
            return source.charAt(current);
    }

    private char peekNext() {
        if(current + 1 >= source.length()) { return '\0'; }
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    // Checks if scanner is at the end of the file
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Returns the current char in the source string, and iterates the current counter
    private char advance() {
        return source.charAt(current++);
    }

    // Overloaded addToken function for non-literal tokens
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // Helper function to add tokens to the list of tokens
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}
