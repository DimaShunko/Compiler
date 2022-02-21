import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

import java.io.*;
import java.util.*;


class Interpreter {
    private final Code code;
    private final ArrayList<Boolean> memory;


    public Interpreter(Code code) {
        this.code = code;
        memory = new ArrayList<Boolean>(code.memory);
        for(int i = 0; i < code.memory; ++i)
            memory.add(false);
    }

    boolean isCom(int code, int com) {
        return tri(code).command == com;
    }
    Triade tri(int i) {
        return code.triades.get(i);
    }

    boolean toBool(int va) {
        return va == 1;
    }

    int toInt(boolean b) {
        return b ? 1 : 0;
    }

    void run() {
        ArrayList<Triade> tc = code.triades;
        for (int i = 0; i < tc.size(); ++i) {
            if (isCom(i, Triade.VAR)) {
                tri(i).command = Triade.CONST;
                tri(i).left = toInt(memory.get(tri(i).left));
            }

            if (isCom(i, Triade.NOT)) {
                tri(i).command = Triade.CONST;
                tri(i).left = toInt(!toBool(tri(tri(i).left).left));
            }

            if (isCom(i, Triade.AND)) {
                tri(i).command = Triade.CONST;
                tri(i).left = toInt(toBool(tri(tri(i).left).left) && toBool(tri(tri(i).right).left));
            }
            if (isCom(i, Triade.OR)) {
                tri(i).command = Triade.CONST;
                tri(i).left = toInt(toBool(tri(tri(i).left).left) || toBool(tri(tri(i).right).left));
            }
            if (isCom(i, Triade.XOR)) {
                tri(i).command = Triade.CONST;
                tri(i).left = toInt(toBool(tri(tri(i).left).left) ^ toBool(tri(tri(i).right).left));
            }
            if (isCom(i, Triade.MOV)) {
                memory.set(tri(i).left, toBool(tri(tri(i).right).left));
            }
        }

        for (HashMap.Entry entry: code.vars.entrySet()) {
            System.out.print(entry.getKey());
            System.out.print("\t\t = ");
            System.out.println((boolean)memory.get((Integer)entry.getValue()));
        }

    }





}

