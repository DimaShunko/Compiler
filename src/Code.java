import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

public class Code {
    public int memory;
    public ArrayList<Triade> triades;
    public HashMap<String, Integer> vars;

    Code(Compiler compiler) {
        this.memory = compiler.ident;
        this.triades = (ArrayList<Triade>) compiler.code.clone();
        vars = (HashMap<String, Integer>) compiler.map.clone();
    }
}