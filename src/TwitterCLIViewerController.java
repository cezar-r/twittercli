/**
 * @file: TwitterCLIViewerController.java
 * @description: Viewer/controller that is used to display and manipulate the UI.
 * Starts by allowing the user to login or signup. If they sign up, they will be prompted
 * to follow accounts based on their interests.
 *
 * Once they are signed in, the user can view tweets on the home page. They can control actions
 * on a certain page with keyboard inputs (usually numerical)
 *
 * The user can also navigate around the app with keyboard inputs.
 *
 * They may also exit at anytime by pressing any key.
 * @author: Cezar Rata
 */

import java.util.Scanner;
import java.util.*;


/**
 * Class that controls the display
 */
public class TwitterCLIViewerController {

    private final static String LINESEPARATOR = "----------------------------------------";
    private final static String NAVBAR = "Home[a]  Search[s]  Tweet[d]  Profile[f]";
    private static final UserData userData = new UserData();

    private static String curUsername;
    private static Scanner scanner;
    private static final ArrayList<Tweet> userFeed = new ArrayList<>();
    private static int curTweetIndex = 0;

    public TwitterCLIViewerController() {
        scanner = new Scanner(System.in);
    }

    /**
     * Starting point of the app that directly navigates user to authentication
     */
    public void run() {
        authPage();
    }

    /**
     * Exits the program
     */
    private static void exit() {
        System.out.println("Exiting Twitter CLI! See ya around");
        System.exit(1);
    }

    /**
     * Displays the authentication page; lets the user pick if they want to log in or signup.
     */
    private static void authPage() {
        System.out.println(LINESEPARATOR + "\n" + AsciiArt.ascii + "\n\nSign in [1]\nSign up [2]\nExit [any key at any time]\n" + LINESEPARATOR);
        int choice = intChoice();
        switch (choice) {
            case 1 -> loginPage();
            case 2-> signupPage();
            default -> exit();
        }
        findRelevantTweets();
        homePage();
    }

    /**
     * Displays the login page; lets the user type in a username and password which is then authenticated.
     */
    private static void loginPage() {
        System.out.println(LINESEPARATOR + "\nEnter username:");
        String username = scanner.nextLine();

        // give the user 3 attempts to type the right password
        int passwordAttempts = 3;
        int attempts = 0;
        while (attempts < passwordAttempts) {
            System.out.println("\nEnter password:");
            String password = scanner.nextLine();
            if (userData.passwordEquals(username, password)) {
                curUsername = username;
                return;
            }
            attempts++;
            System.out.println("\nIncorrect username or password! Try again");
            System.out.println("Attempts: " + attempts + "/" + passwordAttempts);
        }

        // if they fail to type in the password, exit the program
        System.out.println("Too many attempts!");
        exit();

    }

    /**
     * Displays the signup page; lets the user create an account by creating a username and password
     */
    private static void signupPage() {
        System.out.println(LINESEPARATOR + "\nEnter username:");
        String username = scanner.nextLine();
        System.out.println("\nEnter password:");
        String password = scanner.nextLine();
        userData.registerUser(username, password);
        curUsername = username;
        signUpPreferencesPage();
    }

    /**
     * Allows the user to pick from preferences, which will automatically follow accounts based on those interests
     */
    private static void signUpPreferencesPage() {
        System.out.println(LINESEPARATOR + "\nChoose one of the following preferences below::");
        System.out.println("[1] Coding\n[2] Sports\n[3] Gaming\n[4] Music\n" + LINESEPARATOR);
        int choice = intChoice();
        switch (choice) {
            case 1 -> followAccounts(Arrays.asList("coding_master", "bob_the_dev", "coding_noob123", "frontend_wiz"));
            case 2 -> followAccounts(Arrays.asList("fitness_is_everything", "chris_da_hooper420"));
            case 3 -> followAccounts(Arrays.asList("yasuo_goat", "Elden Ring Final Boss"));
            case 4 -> followAccounts(Arrays.asList("DJ Alex", "guitar fanatic"));
            default -> exit();
        }
    }

    /**
     * Follows a list of usernames
     * @param usernames -> list of usernames
     */
    private static void followAccounts(List<String> usernames) {
        for (String username: usernames) {
            userData.addFollowing(curUsername, username);
        }
    }

    /**
     * Displays the home page; Home page is made up of the tweet at the current index,
     * with controls to go back and forth between tweets, as well as a navigation bar
     */
    private static void homePage() {
        if (userFeed.size() == 0) {
            System.out.println("No tweets to display!\n");
        } else {
            System.out.println(LINESEPARATOR + "\n" + userFeed.get(curTweetIndex).toString() + " \n" +LINESEPARATOR);
            System.out.println("Prev[1]        Next[2]        Retweet[3]\n");
        }
        System.out.println(NAVBAR.replaceFirst("\\[a]", "[#]"));

        String input = scanner.nextLine();

        // see if input can be converted to int
        try {
            int tweetViewChoice = Integer.parseInt(input);
            switch (tweetViewChoice) {

                // go to previous tweet
                case 1 -> curTweetIndex = Math.max(0, curTweetIndex-1);

                // go to next tweet
                case 2 -> curTweetIndex = Math.min(userFeed.size()-1, curTweetIndex+1);

                // retweet tweet
                case 3 -> userData.addRetweet(curUsername, userFeed.get(curTweetIndex));

                // exit program
                default -> exit();
            }

        // if it can't be converted to a number, navigate
        } catch (Exception e) {
            navigate(input);
        }
        homePage();
    }

    /**
     * Lets the user type in a username, which then displays the profile for that user, if they exist
     */
    private static void searchPage() {
    	System.out.println(LINESEPARATOR + "\nEnter username to search for:");
        String username = scanner.nextLine();
        HashTable<String, Object> user = userData.getUser(username);
        if (user == null) {
            System.out.println("User \"" + username + "\" not found.\n");
            searchPage();
        }
        searchResult(username);
    }

    /**
     * Displays the profile of the username that was typed in
     * @param username -> username whose profile should be displayed
     */
    private static void searchResult(String username) {
        profilePage(username);
        System.out.println(NAVBAR.replaceFirst("\\[s]", "[#]"));

        String input = scanner.nextLine();

        // see if input can be converted to int
        try {
            int profileChoice = Integer.parseInt(input);
            switch (profileChoice) {

                // follow/unfollow
                case 1 -> {
                    if (userData.getFollowing(curUsername).contains(username)) {
                        userData.unfollow(curUsername, username);
                    } else {
                        userData.addFollowing(curUsername, username);
                    }
                }

                // display tweets
                case 2 -> displayList(userData.getTweets(username), username, "tweets");

                //display followers
                case 3 -> displayList(userData.getFollowers(username), username, "followers");

                // display following
                case 4 -> displayList(userData.getFollowing(username), username, "following");

                // exit program
                default -> exit();
            }

        // if it can't be converted to a number, navigate
        } catch (Exception e) {
            navigate(input);
        }
        searchResult(username);
    }

    /**
     * Displays a list's content. Mainly used to display tweets, followers, and following of a user
     * @param array -> array of contents
     * @param username -> user who owns contents
     * @param content -> type of content (tweets, followers, following)
     */
    private static void displayList(ArrayList array, String username, String content) {
        System.out.println(LINESEPARATOR + "\n@" + username + "'s " + content + ":\n");
        System.out.println(LINESEPARATOR);
        if (array.size() == 0) {
            System.out.println("No " + content + " yet");
            return;
        }
        for (Object obj: array) {
            System.out.println(obj.toString() + "\n" + LINESEPARATOR);
        }
        System.out.println("\n");
    }

    /**
     * Lets the user type in a tweet. After this the user is navigated to the home
     * page where they can see their tweet.
     */
    private static void tweetPage() {
    	Date currentDate = new Date();
    	System.out.println(LINESEPARATOR + "\nCompose new tweet:\n");
        String content = scanner.nextLine();
        Tweet newTweet = new Tweet(content, curUsername, currentDate); 
        userFeed.add(0, newTweet);
        curTweetIndex = 0;
        userData.addTweet(curUsername, newTweet);
        System.out.println("\nTweet posted!\n");
        homePage();
    }

    /**
     * Displays the profile page of a given username
     * @param username -> username whose profile should be shown
     */
    private static void profilePage(String username) {
        int numTweets = userData.getTweets(username).size();
        int numFollowers = userData.getFollowers(username).size();
        int numFollowing = userData.getFollowing(username).size();

        System.out.println(LINESEPARATOR);
        System.out.printf("Profile - @%s\n\n", username);

        // formatting spacing
        int n = String.valueOf(numTweets).length() + String.valueOf(numFollowers).length() + String.valueOf(numFollowing).length() + 30;
        int space1 = (80 - n) / 2;
        int space2 = 80 - n - space1;

        System.out.printf("%d Tweets%" + space1/4 + "s%d Followers%" + space2/4 + "s%d Following\n\n", numTweets, "", numFollowers, "", numFollowing);

        // adjust profile page options
        String followOption = "Follow";
        ArrayList<String> following = userData.getFollowing(curUsername);
        if (following.contains(username)) {
            followOption = "Unfollow";
        }
        String[] vals = { followOption, "View Tweets", "View Followers", "View Following" };

        // set profile page options depending on if we are looking at our profile or someone else's
        int choice = 1;
        for (String val: vals) {
            if (username.equals(curUsername)) {
                if (choice == 4) {
                    break;
                }
                System.out.println(vals[choice] + " [" + choice + "]");
            } else {
                System.out.println(vals[choice-1] + " [" + choice + "]");
            }
            choice++;
        }
        System.out.println(LINESEPARATOR);
    }

    /**
     * Displays the profile page; lets the user see how many tweets, followers, and following an account has.
     * Also has controls that lets the user view each of these.
     */
    private static void profilePage() {
        profilePage(curUsername);
        System.out.println(NAVBAR.replaceFirst("\\[f]", "[#]"));

        String input = scanner.nextLine();
        try {
            int profileChoice = Integer.parseInt(input);
            switch (profileChoice) {
                case 1 -> displayList(userData.getTweets(curUsername), curUsername, "tweets");
                case 2 -> displayList(userData.getFollowers(curUsername), curUsername, "followers");
                case 3 -> displayList(userData.getFollowing(curUsername), curUsername, "following");
                default -> exit();
            }
        } catch (Exception e) {
            navigate(input);
        }
        profilePage();
    }

    /**
     * Navigates the user between pages on the app
     * @param page -> string representation of page
     */
    private static void navigate(String page) {
        switch (page) {
            case "a" -> homePage();
            case "s" -> searchPage();
            case "d" -> tweetPage();
            case "f" -> profilePage();
            default -> exit();
        }
    }

    /**
     * Finds tweets to fill the home page with
     */
    private static void findRelevantTweets() {
        ArrayList<Tweet> tweets = new ArrayList<>();
        ArrayList<Float> scores = new ArrayList<>();

        // first, adds tweets from people user follows
        ArrayList<String> following = userData.getFollowing(curUsername);
        for (String username: following) {
            for (Tweet tweet: userData.getTweets(username)) {
                tweets.add(tweet);
                scores.add(1.0f);
            }
        }

        // next, find most relevant users we don't follow
        HashTable<String, Float> mostRelevantUnknowns = mostRelevantUnknowns();

        // get 1 tweet from each
        for (String username: mostRelevantUnknowns.keySet()) {
            tweets.add(userData.getTweets(username).get(0));
            scores.add(mostRelevantUnknowns.get(username));
        }

        // get 1 tweet where someone you followed retweeted their tweet
        for (String username: following) {
            for (String relevantUsername: mostRelevantUnknowns.keySet()) {
                ArrayList<Tweet> retweets = userData.getRetweetsWhereAuthorIs(username, relevantUsername);
                if (retweets.size() > 0) {
                    Tweet retweet = retweets.get(0);
                    if (!containsTweet(tweets, retweet)) {
                        tweets.add(retweet);
                        scores.add(mostRelevantUnknowns.get(relevantUsername) / 2);
                    }
                }
            }
        }

        // sort by rating
        CoupleSorting.sortBy(tweets, scores);

        // slightly shuffle the list
        smartShuffle(tweets);

        userFeed.addAll(tweets);
    }

    /**
     * Shuffles the array, but each element moves no more than 2 indices from its starting index
     * @param array -> array to be shuffled
     */
    private static void smartShuffle(ArrayList array) {
        Random random = new Random();

        for (int i = 0; i < array.size(); i++) {

            // find a random index within the allowed range (i - 2 to i + 2)
            int minIndex = Math.max(0, i - 2);
            int maxIndex = Math.min(array.size() - 1, i + 2);
            int newIndex = random.nextInt(maxIndex - minIndex + 1) + minIndex;

            // Swap the current element with the randomly selected index
            Object temp = array.get(i);
            array.set(i, array.get(newIndex));
            array.set(newIndex, temp);
        }
    }

    /**
     * Checks if an array contains a tweet, returns true if it does otherwise false
     * @param tweets -> array of tweets
     * @param tweet -> tweet to look for
     * @return -> true if tweet in array, otherwise false
     */
    private static boolean containsTweet(ArrayList<Tweet> tweets, Tweet tweet) {
        for (Tweet otherTweet: tweets) {
            if (tweet.equals(otherTweet)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the most relevant users that aren't followed by the current user
     *
     * @return mostRelevant -> hashtable of each user and their corresponding score
     */
    private static HashTable<String, Float> mostRelevantUnknowns() {
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<Float> scores = new ArrayList<>();

        // number of total possible mutual connections (minus us and the mutual friend)
        int n = userData.getAllUsernames().size() - 2;

        // brute force search through every user
        for (String username: userData.getAllUsernames()) {
            if (!username.equals(curUsername) && !userData.getFollowing(curUsername).contains(username)) {

                // score based on number of mutual connections / total possible number of mutual connections
                float score = (float) intersection(userData.getFollowers(username), userData.getFollowing(curUsername)).size() / n;
                usernames.add(username);
                scores.add(score);
            }
        }

        // sort by rating
        CoupleSorting.sortBy(usernames, scores);

        // get the 5 users with the highest scores
        HashTable<String, Float> mostRelevant = new HashTable<>();
        for (int i = 0; i < 5; i++) {
            mostRelevant.put(usernames.get(i), scores.get(i));
        }
        return mostRelevant;
    }

    /**
     * Finds all identical elements in both arrays and returns them
     * @param list1 -> first array of values
     * @param list2 -> second array of values
     * @return list of all identical elements
     */
    public static ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) {
        Set<String> set1 = new HashSet<String>(list1);
        Set<String> set2 = new HashSet<String>(list2);
        set1.retainAll(set2);
        return new ArrayList<String>(set1);
    }

    /**
     * Tries to get an integer input
     * @return choice -> integer value of input, -1 if it cannot be converted
     */
    private static int intChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            exit();
        }
        System.out.println("\n");
        return choice;
    }
}