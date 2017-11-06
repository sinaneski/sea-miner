package repository.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class FileDiff {

    public static final FileDiff empty = new FileDiff(0, 0 , "");
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private int addedLineCount;
    private int deletedLineCount;
    private String content;

    private FileDiff(int addedLineCount, int deletedLineCount, String content) {
        this.addedLineCount = addedLineCount;
        this.deletedLineCount = deletedLineCount;
        this.content = content;
    }

    public static FileDiff fromContent(String content) {
        if(content == null) return empty;
        if(content.isEmpty()) return empty;

        int added = 0;
        int deleted = 0;
        for (String line : content.split(LINE_SEPARATOR)) {
            if (isLineAdded(line)) {
                added++;
            } else if (isLineDeleted(line)) {
                deleted++;
            }
        }
        return new FileDiff(added, deleted, content);
    }

    private static boolean isLineAdded(String line) {
        return line.startsWith("+") && !line.startsWith("+++");
    }

    private static boolean isLineDeleted(String line) {
        return line.startsWith("-") && !line.startsWith("---");
    }
}
