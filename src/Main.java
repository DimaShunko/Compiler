import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import java.util.*;

class Main {
    static Parser parser = new Parser();
    static Compiler compiler = new Compiler();

    public static void compileFile(String filename) {
        ArrayList<Triade> triades = null;
        File file;
        FileReader fr;
        BufferedReader reader = null;
        try {
            file = new File(filename);
            fr = new FileReader(file);
            reader = new BufferedReader(fr);

            try {
                ArrayList<Lex> list = parser.parse(reader);
                compiler.setLexList(list);
                triades = compiler.compile();

                System.out.println("ТРИАДЫ");
               
                for (Triade triade: triades) {
                    System.out.println(triade.toString());
                }
            }
            catch (FileReadingException error) {
                System.out.println(error.getMessage());
            }
            catch (CompilationError error) {
                System.out.println(error.getMessage());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }        

    }

    public static void main(String[] args) {
        String fileName = args[0];
        compileFile(fileName);
        Code code = new Code(compiler);
        Interpreter inter = new Interpreter(code);
        inter.run();

    }
}