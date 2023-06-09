/**
 * @file: Tweet.java
 * @description: This file contains a class that encapsulates a tweet
 * @author: Cezar Rata
 */

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * This class encapsulates a tweet. It holds the tweet content, author, and creation date. It also
 * comes with a toString() method to display the tweet.
 */
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

    /**
     * Checks if this tweet equals another
     * @param otherTweet -> tweet to compare to
     * @return true if every attribute is the same, otherwise false
     */
    public boolean equals(Tweet otherTweet) {
        return content.equals(otherTweet.content()) &&
                createdBy.equals(otherTweet.createdBy()) &&
                createdAt.equals(otherTweet.createdAt());
    }

    /**
     * Returns a string representation of tweet
     * @return String -> String representation of tweet
     */
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
        str.append(curWord).append("\n\n").append(sdf.format(createdAt));
        return str.toString();
    }

    public String toJson() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(createdAt);
        return String.format("{\"content\": \"%s\", \"createdBy\": \"%s\", \"createdAt\": \"%s\"}", content, createdBy, formattedDate);
    }

}
