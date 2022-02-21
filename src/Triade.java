public class Triade {
    static public final int VAR = 0;
    static public final int CONST = 1;
    static public final int MOV = 2;
    static public final int OR = 3;
    static public final int XOR = 4;
    static public final int AND = 5;
    static public final int NOT = 6;

    public int command;
    public int left;
    public int right;

    public Triade(int com, int left, int right) {
        this.command = com;
        this.left = left;
        this.right = right;
    }

    
    public String toString() {
        String msg = "error";
        if (command == VAR)
            msg = "VAR ";
        else if (command == CONST)
            msg = "CONST";
        else if (command == MOV)
            msg = "MOV ";
        else if (command == OR)
            msg = "OR  ";
        else if (command == XOR)
            msg = "XOR ";
        else if (command == AND)
            msg = "AND ";
        else if (command == NOT)
            msg = "NOT ";

        return msg + "\t \t" + left + "\t" + right;
    }


}