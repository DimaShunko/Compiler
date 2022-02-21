import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private TextReader input;
    
    //ТИПЫ ЛЕКСЕМ ДЛЯ ПАРСЕРА
    private static final int IGNORE = -1;
    public static final int LEX_IDENT = 0;
    public static final int LEX_LPAR = 1; //Левая скобка
    public static final int LEX_RPAR = 2; //Правая скобка

    public static final int LEX_OR = 3;
    public static final int LEX_AND = 4;
    public static final int LEX_NOT = 5;
    public static final int LEX_XOR = 6;

    public static final int LEX_SEPARATOR = 7; // ;
    public static final int LEX_EQ = 8; // :=

    public static final int LEX_CONST = 9;

    public Parser() {
        input = new TextReader(" \t\n");
    }

    public Parser(String spaces) {
        input = new TextReader(spaces);
    }

    public TextReader getReader() {
        return input;
    }

    private boolean isLetter() {
        return (('a' <= input.get()) && (input.get() <= 'z'))
        ||  (('A' <= input.get()) && (input.get() <= 'Z'));
    }

    private String ident() throws FileReadingException {
        StringBuilder builder = new StringBuilder();
        while (isLetter()) {
            builder.append(input.get());
            input.read();
        }
        return builder.toString();
    }
    

    //Считать 1 лексему
    private Lex readLex() throws FileReadingException {
        while (input.skip('#')) { //Комментарии
            while (input.read() != '\n') {}
            return new Lex(IGNORE, null);
        }

        if (input.get()== '\r') {
            if (input.read() == '\n') {
                return new Lex(IGNORE, null);
            }
        }

        if (input.skip('('))
            return new Lex(LEX_LPAR, "(");
        if (input.skip(')'))
            return new Lex(LEX_RPAR, ")");
        if (input.skip(';'))
            return new Lex(LEX_SEPARATOR, ";");
        if (input.skip(':')) {
            if (input.skip('='))
                return new Lex(LEX_EQ, ":=");

            throw new FileReadingException("Ожидалось '='", input);
        }
            

        if (isLetter()) {
            String line = ident();
            if (line.equals("T") || line.equals("F"))
                return new Lex(LEX_CONST, line);
            if (line.equals("and")) {
                return new Lex(LEX_AND, line);
            }
            if (line.equals("or")) {
                return new Lex(LEX_OR, line);
            }
            if (line.equals("xor")) {
                return new Lex(LEX_XOR, line);
            }
            if (line.equals("not")) {
                return new Lex(LEX_NOT, line);
            }

            return new Lex(LEX_IDENT, line);
        }
        
        throw new FileReadingException("Неизвестный символ", input);
    }




    public ArrayList<Lex> parse(BufferedReader reader) throws FileReadingException {
        input.readInput(reader);
        ArrayList<Lex> output = new ArrayList<Lex>();
        
        input.sread();
        while (!input.isEof()) {
            Lex lex = readLex();
            if (lex.type != IGNORE) {
                output.add(lex);
            }
            input.skipSpaces();
        }

        return output;
    }
}