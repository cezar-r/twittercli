import java.util.Scanner;
import java.util.*;


public class TwitterCLIViewerController {

    private final static String LINESEPARATOR = "----------------------------------------";
    private final static String NAVBAR = "Home[a]  Search[s]  Tweet[d]  Profile[f]";
    private static final UserData userData = new UserData();

    private static NetworkGraph graph;

    private static String curUsername;
    private static Scanner scanner;
    private static final ArrayList<Tweet> userFeed = new ArrayList<>();
    private static int curTweetIndex = 0;

    public TwitterCLIViewerController() {
        scanner = new Scanner(System.in);
        graph = new NetworkGraph(userData);
    }

    public void run() {
        authPage();
    }

    private static void exit() {
        System.out.println("Exiting Twitter CLI! See ya around");
        System.exit(1);
    }

    private static void authPage() {
        System.out.println(LINESEPARATOR + "\nWelcome to Twitter CLI!\n\nSign in [1]\nSign up [2]\nExit [any key at any time]\n" + LINESEPARATOR);
        int choice = intChoice();
        switch (choice) {
            case 1 -> loginPage();
            case 2-> signupPage();
            default -> exit();
        }
        findRelevantTweets();
        homePage();
    }

    private static void loginPage() {
        System.out.println(LINESEPARATOR + "\nEnter username:");
        String username = scanner.nextLine();
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
        System.out.println("Too many attempts!");
        exit();

    }

    private static void signupPage() {
        System.out.println(LINESEPARATOR + "\nEnter username:");
        String username = scanner.nextLine();
        System.out.println("\nEnter password:");
        String password = scanner.nextLine();
        userData.registerUser(username, password);
        curUsername = username;
        signUpPreferencesPage();
    }

    private static void signUpPreferencesPage() {
        System.out.println(LINESEPARATOR + "\nChoose one of the following preferences below::");
        System.out.println("[1] Coding\n[2] Sports\n[3] Gaming\n[4] Music\n" + LINESEPARATOR);
        int choice = intChoice();
        switch (choice) {
            case 1 -> followCoders();
            case 2 -> followSports();
            case 3 -> followGamers();
            case 4 -> followMusic();
            default -> exit();
        }
    }

    private static void followCoders() {
        userData.addFollowing(curUsername, "user1");
        userData.addFollowing(curUsername, "user2");
        userData.addFollowing(curUsername, "user9");
        userData.addFollowing(curUsername, "user10");
    }

    private static void followSports() {
        userData.addFollowing(curUsername, "user3");
        userData.addFollowing(curUsername, "user4");
    }

    private static void followGamers() {
        userData.addFollowing(curUsername, "user5");
        userData.addFollowing(curUsername, "user6");
    }

    private static void followMusic() {
        userData.addFollowing(curUsername, "user7");
        userData.addFollowing(curUsername, "user8");
    }

    private static void homePage() {
        if (userFeed.size() == 0) {
            System.out.println("No tweets to display!\n");
        } else {
            System.out.println(LINESEPARATOR + "\n" + userFeed.get(curTweetIndex).toString() + " \n" +LINESEPARATOR);
            System.out.println("Prev[1]        Next[2]        Retweet[3]\n");
        }
        System.out.println(NAVBAR.replaceFirst("\\[a\\]", "[#]"));

        String input = scanner.nextLine();
        try {
            int tweetViewChoice = Integer.parseInt(input);
            switch (tweetViewChoice) {
                case 1 -> curTweetIndex = Math.max(0, curTweetIndex-1);
                case 2 -> curTweetIndex = Math.min(userFeed.size()-1, curTweetIndex+1);
                case 3 -> userData.addRetweet(curUsername, userFeed.get(curTweetIndex));
                default -> exit();
            }
        } catch (Exception e) {
            navigate(input);
        }
        homePage();

    }

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

    private static void searchResult(String username) {
        profilePage(username);
        System.out.println(NAVBAR.replaceFirst("\\[s\\]", "[#]"));

        String input = scanner.nextLine();
        try {
            int profileChoice = Integer.parseInt(input);
            switch (profileChoice) {
                case 1 -> {
                    if (userData.getFollowing(curUsername).contains(username)) {
                        userData.unfollow(curUsername, username);
                    } else {
                        userData.addFollowing(curUsername, username);
                    }
                }
                case 2 -> displayList(userData.getTweets(username), username, "tweets");
                case 3 -> displayList(userData.getFollowers(username), username, "followers");
                case 4 -> displayList(userData.getFollowing(username), username, "following");
                default -> exit();
            }
        } catch (Exception e) {
            navigate(input);
        }
        searchResult(username);
    }

    private static void displayList(ArrayList array, String username, String content) {
        System.out.println(LINESEPARATOR + "\n@" + username + "'s " + content + ":\n");
        System.out.println(LINESEPARATOR);
        if (array.size() == 0) {
            System.out.println("No " + content + " yet\n");
            return;
        }
        for (Object obj: array) {
            System.out.println(obj.toString() + "\n" + LINESEPARATOR);
        }
        System.out.println("\n");
    }

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

    private static void profilePage(String username) {
        int numTweets = userData.getTweets(username).size();
        int numFollowers = userData.getFollowers(username).size();
        int numFollowing = userData.getFollowing(username).size();

        System.out.println(LINESEPARATOR);
        System.out.printf("Profile - @%s\n\n", username);
        int n = String.valueOf(numTweets).length() + String.valueOf(numFollowers).length() + String.valueOf(numFollowing).length() + 30;
        int space1 = (80 - n) / 2;
        int space2 = 80 - n - space1;

        System.out.printf("%d Tweets%" + space1/4 + "s%d Followers%" + space2/4 + "s%d Following\n\n", numTweets, "", numFollowers, "", numFollowing);
        String followOption = "Follow";
        ArrayList<String> following = userData.getFollowing(curUsername);
        if (following.contains(username)) {
            followOption = "Unfollow";
        }
        String[] vals = { followOption, "View Tweets", "View Followers", "View Following" };
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

    private static void profilePage() {
        profilePage(curUsername);
        System.out.println(NAVBAR.replaceFirst("\\[f\\]", "[#]"));

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

    private static void navigate(String page) {
        switch (page) {
            case "a" -> homePage();
            case "s" -> searchPage();
            case "d" -> tweetPage();
            case "f" -> profilePage();
            default -> exit();
        }
    }

    private static void findRelevantTweets() {
        ArrayList<String> following = userData.getFollowing(curUsername);
        for (String username: following) {
            List<Tweet> tweets = userData.getTweets(username);
            userFeed.addAll(tweets);
        }
        Collections.shuffle(userFeed);

    }

    private static int intChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            exit();
        }
        System.out.println("\n\n");
        return choice;
    }
}
