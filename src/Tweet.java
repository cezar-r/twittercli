import java.util.Date;
import java.text.SimpleDateFormat;

public class Tweet {

    private final String content;
    private final String createdBy;
    private final Date createdAt;

    public Tweet(String tweetContent, String author, Date date) {
        content = tweetContent;
        createdBy = author;
        createdAt = date;
    }

    public String content() {
        return content;
    }

    public String createdBy() {
        return createdBy;
    }

    public Date createdAt() {
        return createdAt;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("@").append(createdBy).append("\n");
        int n = content.length();
        StringBuilder curWord = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i % 40 == 0) {
                str.append("\n");
            }
            char c = content.charAt(i);
            curWord.append(c);
            if (c == ' ') {
                str.append(curWord);
                curWord = new StringBuilder();
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        str.append(curWord).append("\n\n").append(sdf.format(createdAt)).append("\n");
        return str.toString();
    }

    public String toJson() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(createdAt);
        return String.format("{\"content\": \"%s\", \"createdBy\": \"%s\", \"createdAt\": \"%s\"}", content, createdBy, formattedDate);
    }

}
