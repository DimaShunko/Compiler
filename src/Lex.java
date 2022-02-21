public class Lex {
    public final int type;
    public final String desc;

    public Lex(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String typeToString(int tp) {
        if (tp == Parser.LEX_IDENT)
            return ("Идентификатор");
        else if (tp == Parser.LEX_LPAR)
            return ("Откр. круглая скобка");
        else if (tp == Parser.LEX_RPAR)
            return ("Закр. круглая скобка");
        else if (tp == Parser.LEX_OR)
            return ("Ключевое слово ИЛИ");
        else if (tp == Parser.LEX_AND)
            return ("Ключевое слово И");
        else if (tp == Parser.LEX_NOT)
            return ("Ключевое слово НЕ");
        else if (tp == Parser.LEX_XOR)
            return ("Ключевое слово ИСКЛ ИЛИ");
        else if (tp == Parser.LEX_SEPARATOR)
            return("Разделитель");
        else if (tp == Parser.LEX_EQ)
            return ("Присваивание");
        else if (tp == Parser.LEX_CONST)
            return ("Константа");
        else
            return "error";
    }
}