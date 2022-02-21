import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;


public class Compiler {
    int lexcount;
    int ident = 0;
    ArrayList<Lex> parsed;
    ArrayList<Triade> code;
    HashMap<String, Integer> map;

    public Compiler() {
        lexcount = 0;
        parsed = new ArrayList<Lex>();
        code = new ArrayList<Triade>();
        map = new HashMap<>();
    }
    public void reset() {
        lexcount = 0;
        parsed.clear();
        code.clear();
        map.clear();
        ident = 0;
    }
    
    public void setLexList(ArrayList<Lex> lexList) {
        parsed  = lexList;
    }
    public Lex current() {
        return parsed.get(lexcount);
    }
    public boolean isEnd() {
        return lexcount == parsed.size();
    }

    private boolean is(int id) {
        return current().type == id;
    }
    private void require(int id) {
        if (!is(id)) {
            throw new CompilationError("Ожидалось: " + Lex.typeToString(id));
        }
    }

    private void next() {
        if (isEnd())
            throw new CompilationError("Неожиданный конец потока лексем");
            
        ++lexcount;
        
    }
    public int ReadIdent(){
        require(Parser.LEX_IDENT);
        if (map.containsKey(current().desc)) {
            int value = map.get(current().desc);
            next();
            
            return value;
        }
        map.put(current().desc, ident);
        next();
        return ident++;
    }


    public int newTriade(int com, int left, int right) {
        code.add(new Triade(com, left, right));
        return code.size() - 1;
    }

    public int EXPR() {
        if (is(Parser.LEX_IDENT)) {
            return newTriade(Triade.VAR, ReadIdent(), 0);
        } else if (is(Parser.LEX_CONST)) {
            boolean value = current().desc.equals("T");
            next();
            return newTriade(Triade.CONST, value ? 1 : 0, 0);
        } else if (is(Parser.LEX_NOT)) {
            next();
            return newTriade(Triade.NOT, EXPR(), 0);
        } else if (is(Parser.LEX_LPAR)) {
            next();

            int value = OR();
            
            require(Parser.LEX_RPAR);
            next();
            return value;
        }
        throw new CompilationError("Неожиданная лексема");
    }

    public int AND() {
        int left = EXPR();
        if (is(Parser.LEX_AND)) {
            next();
            return newTriade(Triade.AND, left, AND());
        }
        return left; 
    }

    public int OR() {
        int left = AND();
        if (is(Parser.LEX_OR) || is(Parser.LEX_XOR)) {
            if (is(Parser.LEX_OR)) {
                next();
                return newTriade(Triade.OR, left, OR());
            } else if (is(Parser.LEX_XOR)) {
                next();
                return newTriade(Triade.XOR, left, OR());
            }
        }
        return left;
    }

    public void mov() {
        int left = ReadIdent();
        
        require(Parser.LEX_EQ);
        next();

        int right = OR(); // EXPR and EXPR or xor EXPR

        newTriade(Triade.MOV, left, right);
        
        require(Parser.LEX_SEPARATOR);
        next();
    }


    public ArrayList<Triade> compile() {
        while (!isEnd()) {
            mov(); // IDENT := EXPR;
        }  
        return code;  
    }

}