/**
 * @file: Main.java
 * @author: Cezar Rata, Connor Olive, Alankrit Moses
 * @description: Main file that runs the Twitter CLI app.
 */

// TODO
// add some variance to # of tweets, rts, follower/ing a user has
// add variance to their retweet topics

public class Main {
    public static void main(String[] args) {
        TwitterCLIViewerController app = new TwitterCLIViewerController();
        app.run();
    }
}