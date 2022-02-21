import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TextReader {
    private BufferedReader input; //Файл откуда читаем

    private char readed; //Последний считанный символ
    private StringBuilder buffer; //Сюда читаем
    private ArrayList<String> lines; //Сюда записываем считанные лини

   
    //Положение в файле
    private int line_index; //Номер линии
    private int char_index; //Номер символа в линии

    //Показывает, что достигнут конец файла
    private boolean eofFlag;

    //Список символов для пробелов
    private Set<Character> spaces;

    //Инициализация
    private final void init() {
        buffer =  new StringBuilder();
        lines = new ArrayList<>();
        spaces = new HashSet<>();
    }

    public TextReader() {
        init();
        try {
            readInput(null);
        } catch(FileReadingException ignored) {}
    }

    public TextReader(String spaces) {
        init();
        try {
            readInput(null);
        } catch(FileReadingException ignored) {}

        for (int i = 0; i < spaces.length(); ++i)
            registerSpace(spaces.charAt(i));
    }

    public TextReader(BufferedReader openFile) throws FileReadingException {
        init();
        readInput(openFile);
    }

    public TextReader(String spaces, BufferedReader openFile) throws FileReadingException {
        init();
        for (int i = 0; i < spaces.length(); ++i)
            registerSpace(spaces.charAt(i));

        readInput(openFile);

    }

    //Говорит, что переданный символ считается пробелом
    public void registerSpace(char space) {
        spaces.add(space);
    }

    //Сброс состояния
    public void reset() throws FileReadingException {
        //Состояние буфферов
        buffer.setLength(0);
        lines.clear();

        //Считанный символ = ничего
        readed = 0;

        //Сбрасываем положение
        char_index = 0;
        line_index = 0;
        
        if (input != null) {
            try {
                //Обновляем флаг конца файла
                eofFlag = !input.ready();
            } catch (IOException exception) {
                throw new FileReadingException(exception.getMessage(), this, exception);
            }
        }
    }

    //Установка нового файла
    public void setInput(BufferedReader openFile) throws FileReadingException {
        input = openFile;
        
        if (input != null) {
            try {
                //Обновляем флаг конца файла
               eofFlag = isReading() && !input.ready();
            } catch (IOException exception) {
                throw new FileReadingException(exception.getMessage(), this, exception);
            }
        }
    }

    //Сбрасывает состояние и указывает файл как ввод
    public void readInput(BufferedReader openFile) throws FileReadingException {
        reset();
        setInput(openFile);
    }    

    //Возвращает true, если считывание завершено
    public boolean isEof() {
        return eofFlag;
    }
    //Возвращает true если начало файла
    public boolean isSof() {
        return (line_index == 0) && (char_index == 0);
    }

    //Возвращает номер текущего символа
    public int getCharIndex() {
        return char_index;
    }
    //Возвращает номер линии
    public int getLineIndex() {
        return line_index;
    }

    //Текущий символ
    public char get() {
        return readed;
    }

    //Происходит ли сейчас считывание из файла
    private boolean isReading() {
        return (line_index == lines.size()) && (char_index == buffer.length());
    }

    //Считать символ (возможно из буффера), возвращает считанное
    public char read() throws FileReadingException {
        try {
            if (isReading()) {
                boolean ready = (input != null) && input.ready();
                if (isEof())
                    throw new FileReadingException("Попытка читать закончившийся файл", this);

                //Достигнут конец файла, добавляем конец строки
                if (!ready) {
                    readed = '\n';
                    buffer.append(readed);
                    eofFlag = true;
                }

                //Достигнут конец строки, переводим строку в буффере
                if (readed == '\n') {
                    lines.add(buffer.toString());
                    ++line_index;
                    
                    buffer.setLength(0);
                    char_index = 0;
                }

                //Сейчас не конец файла, читаем символ
                if (ready) {
                    readed = (char)input.read();
                    buffer.append(readed);
                    ++char_index;
                }
            } else {
                //Если мы достигли конца строки в буффере - переводим строку
                if (char_index == buffer.length()) {
                    buffer.setLength(0);
                    buffer.append(lines.get(line_index++));
                    //Если это последняя строка - она добавлена туда, временно
                    if (line_index == lines.size()) {
                        lines.remove(--line_index);
                    }
                }
                //читаем следующий символ из буффера
                readed = buffer.charAt(char_index++);
                //Если достигли конца считанного, проверяем можем ли читать дальше
                if (isReading())
                    eofFlag = (input == null) || !input.ready();
            }
        } catch (IOException exception) {
            throw new FileReadingException("Ошибка, при чтении", this, exception);
        }
        return readed;
    }



    //Возвращает линию с указанным номером
    public String getLine(int line) {
        if (line == lines.size())
            return buffer.toString();
        else
            return lines.get(line);
    }

    //Возвращает символ с номером index в текущей линии
    public char getChar(int index) {
        return buffer.charAt(index);
    }

    //Возвращает символ с номером index в линии line
    public char getChar(int line, int index) {
        if (line == lines.size())
            return getChar(index);
        else
            return lines.get(line).charAt(index);
    }

    //Вернуться на предыдущий символ
    public char back() throws FileReadingException {
        if (isSof())
            throw new FileReadingException("Попытка читать закончившийся файл", this);
        
        eofFlag = false;

        if (char_index == buffer.length()) {
            --char_index;
        }

        //Мы находимся в начале строки - переводим назад
        if (char_index == 0) {
            //Текущая строка - последняя строка, то временно записываем её в конец, чтобы потом продолжить чтение
            if (line_index == lines.size()) {
                lines.add(buffer.toString());
                ++line_index;
            } 

            buffer.setLength(0);
            buffer.append(lines.get(--line_index));
            char_index = buffer.length();
        }
        
        readed = buffer.charAt(--char_index);

        return readed;
    }

    //Возврат до тех пор, пока символы пробельные
    public void backSkipSpaces() throws FileReadingException {
        while (!isSof() && spaces.contains(readed))
            back();
    }
    //Считывать до тех пор, пока символы пробельные
    public void skipSpaces() throws FileReadingException {
        while (!isEof() && spaces.contains(readed)) 
            read();
    }
    
    //Возврат игнорируя пробелы
    public char sback() throws FileReadingException {
        back();
        backSkipSpaces();
        return readed;
    }

    //Считывать игнорируя пробелы
    public char sread() throws FileReadingException{
        read();
        skipSpaces();
        return readed;
    }

    //Если текущий символ это c, читает следующий и возвращает true
    public boolean skip(char c) throws FileReadingException {
        if (readed == c) {
            read();
            return true;
        }
        return false;
    }
}