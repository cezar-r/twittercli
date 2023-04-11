/**
 * @file: Test.java
 * @description: This file contains test cases for features implemented in TwitterCLI such as
 * the hashtable and two sorting algorithms.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Comparator;


public class Test {

    public static void main(String[] args) {
        int score = 0;
        score += testUserData();
        score += testSorting();
        System.out.println("Final score: " + score + "/15");
    }

    /**
     * Tests the userdata hashtable
     */
    public static int testUserData() {
        System.out.println("Testing user data methods");
        int score = 0;
        score += testRegister();
        score += testAddTweet();
        score += testAddFollowers();
        score += testUnfollow();
        score += testPasswordEquals();
        System.out.println(score + "/5 user data testcases passed\n");
        return score;
    }

    /**
     * Tests both insertion and merge sort
     */
    public static int testSorting() {
        System.out.println("Testing sorting methods");
        int score = 0;
        score += testInsertionSort();
        score += testMergeSort();

        System.out.println(score + "/10 sorting testcases passed\n");
        return score;
    }

    /**
     * Tests the insertionSort method on small sizes
     */
    private static int testInsertionSort() {
        System.out.println(" Testing insertion sort on sizes 1, 2, 4, 8");
        int score = 0;
        for (int i = 0; i < 4; i++) {
            ArrayList<String> data = new ArrayList<>();
            ArrayList<Float> values = new ArrayList<>();
            fillWithRandomValues(data, values, 1 << i);
            ArrayList<Float> valuesCopy = deepCopy(values);

            CoupleSorting.sortBy(data, values);
            valuesCopy.sort(Comparator.reverseOrder());
            if (assertArrayEquals(values, valuesCopy, "arrays are not equal")) {
                score++;
            }
        }
        return score;
    }

    /**
     * Tests the mergeSort method on bigger sizes
     */
    private static int testMergeSort() {
        System.out.println(" Testing merge sort on sizes 16, 32, 64, 128, 256, 512");
        int score = 0;
        for (int i = 4; i < 10; i++) {
            ArrayList<String> data = new ArrayList<>();
            ArrayList<Float> values = new ArrayList<>();
            fillWithRandomValues(data, values, 1 << i);
            ArrayList<Float> valuesCopy = deepCopy(values);

            CoupleSorting.sortBy(data, values);
            valuesCopy.sort(Comparator.reverseOrder());
            if (assertArrayEquals(values, valuesCopy, "arrays are not equal")) {
                score++;
            }
        }
        return score;
    }

    /**
     * Tests registering users
     */
    public static int testRegister() {
        System.out.println("  Testing registerUser()");
        UserData userData = new UserData();
        String username = "testUser";
        String password = "testPassword";
        userData.registerUser(username, password);
        if (!assertNotNull(userData.getUser(username), "User should be registered")) {
            return 0;
        }
        return 1;
    }

    /**
     * Tests users tweeting
     */
    public static int testAddTweet() {
        System.out.println("  Testing addTweet()");
        UserData userData = new UserData();
        String username = "testUser";
        String password = "testPassword";
        userData.registerUser(username, password);

        String tweetContent = "Hello world";
        Tweet tweet = new Tweet(tweetContent, username, new Date());
        userData.addTweet(username, tweet);
        if (!assertNotNull(userData.getTweets(username), "Tweet was not added")) {
            return 0;
        }
        if (!userData.getTweets(username).get(0).content().equals(tweetContent)) {
            return 0;
        }
        return 1;
    }

    /**
     * Tests following users
     */
    public static int testAddFollowers() {
        System.out.println("  Testing addFollowing() and addFollower()");
        UserData userData = new UserData();
        String username1 = "testUser1";
        String password1 = "testPassword1";
        userData.registerUser(username1, password1);

        String username2 = "testUser2";
        String password2 = "testPassword2";
        userData.registerUser(username2, password2);

        userData.addFollowing(username1, username2);
        if (!assertEquals(userData.getFollowing(username1).get(0), username2, "user1 is not following user2")) {
            return 0;
        }
        if (!assertEquals(userData.getFollowers(username2).get(0), username1, "user1 is not a follower of user2")) {
            return 0;
        }
        return 1;
    }

    /**
     * Tests unfollowing users
     */
    public static int testUnfollow() {
        System.out.println("  Testing unfollow()");
        UserData userData = new UserData();
        String username1 = "testUser1";
        String password1 = "testPassword1";
        userData.registerUser(username1, password1);

        String username2 = "testUser2";
        String password2 = "testPassword2";
        userData.registerUser(username2, password2);

        userData.addFollowing(username1, username2);
        userData.unfollow(username1, username2);

        if (!assertEquals(userData.getFollowing(username1).size(), 0, "expected 0 following")) {
            return 0;
        }
        return 1;
    }

    /**
     * Tests password checker
     */
    private static int testPasswordEquals() {
        System.out.println("  Testing passwordEquals()");
        UserData userData = new UserData();
        String username = "testUser";
        String password = "testPassword";
        userData.registerUser(username, password);
        if (!assertTrue(userData.passwordEquals(username, password), "passwords don't match")) {
            return 0;
        }
        return 1;
    }

    private static boolean assertTrue(boolean val, String msg) {
        if (!val) {
            System.out.println(msg);
            return false;
        }
        return true;
    }

    private static boolean assertNotNull(Object obj, String msg) {
        if (obj == null) {
            System.out.println(msg);
            return false;
        }
        return true;
    }

    private static boolean assertEquals(Object obj1, Object obj2, String msg) {
        if (!obj1.equals(obj2)) {
            System.out.println(msg);
            return false;
        }
        return true;
    }

    private static boolean assertArrayEquals(ArrayList arr1, ArrayList arr2, String msg) {
        if (arr1.size() != arr2.size()) {
            System.out.println(msg);
            return false;
        }
        int n = arr1.size();
        for (int i = 0; i < n; i++) {
            if (!assertEquals(arr1.get(i), arr2.get(i), "expected equal values")) {
                System.out.println(msg);
                return false;
            }
        }
        return true;
    }

    private static void fillWithRandomValues(ArrayList<String> data, ArrayList<Float> values, int numValues) {
        Random rand = new Random();
        for (int i = 0; i < numValues; i++) {
            // Generate a random string of length 5
            String randomString = generateRandomString(5, rand);
            data.add(randomString);

            // Generate a random float between 0 (inclusive) and 1 (exclusive)
            float randomFloat = rand.nextFloat();
            values.add(randomFloat);
        }
    }

    private static String generateRandomString(int length, Random rand) {
        StringBuilder sb = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            int randomIndex = rand.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    private static ArrayList deepCopy(ArrayList arr) {
        ArrayList newArr = new ArrayList<>();
        for (Object obj: arr) {
            newArr.add(obj);
        }
        return newArr;
    }

}
