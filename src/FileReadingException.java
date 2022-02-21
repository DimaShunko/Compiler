import java.io.IOException;

public class FileReadingException extends IOException {
    FileReadingException(String message, TextReader reader) {
        super(makeMessage(message, reader));
    }

    FileReadingException(String message, TextReader reader, Throwable cause) {
        super(makeMessage(message, reader), cause);
    }

    static String makeMessage(String message, TextReader reader){
        StringBuilder msg = new StringBuilder(reader.getLine(reader.getLineIndex()));
        msg.append('\n');
        for (int i = 1; i < reader.getCharIndex(); ++i) {
            msg.append('~');
        }
        msg.append('^');
        msg.append('\n');

        msg.append(message).append('\n');
        return msg.toString();
    }
}