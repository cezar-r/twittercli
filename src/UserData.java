/**
 * @file: UserData.java
 * @description: This file contains a class that is used to store user data. The data is implemented as a Hashtable
 * @author: Cezar Rata
 */

import java.util.*;
import java.util.stream.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Class that holds the user data. Data is stored relationally in a hashtable
 */
public class UserData {

    private static final HashTable<String, HashTable<String, Object>> userData = new HashTable<>();

    public UserData() {
        fill();
    }

    /**
     * Returns a hashtable of the users data
     * @param username -> username to retrieve data from
     * @return -> HashTable<String, Object>, which contains the users data
     */
    public HashTable<String, Object> getUser(String username) {
        return userData.get(username);
    }

    /**
     * Returns every username in the dataset
     * @return -> userData.keySet(), every username
     */
    public Set<String> getAllUsernames() {
        return userData.keySet();
    }

    /**
     * Adds a brand-new user to the database
     * @param username -> username to add
     * @param password -> password to add
     */
    public void registerUser(String username, String password) {
        HashTable<String, Object> newUserData = new HashTable<>();
        newUserData.put("tweets", new ArrayList<Tweet>());
        newUserData.put("retweets", new ArrayList<Tweet>());
        newUserData.put("followers", new ArrayList<String>());
        newUserData.put("following", new ArrayList<String>());
        newUserData.put("password", password.hashCode());
        userData.put(username, newUserData);
    }

    /**
     * Returns a list of tweets from a given user
     * @param username -> username to get tweets from
     * @return -> ArrayList<Tweet>, list of tweets by given user
     */
    public ArrayList<Tweet> getTweets(String username) {
        return (ArrayList<Tweet>) userData.get(username).get("tweets");
    }

    /**
     * Adds a tweet to a users data
     * @param username -> username of account who created tweet
     * @param tweet -> tweet
     */
    public void addTweet(String username, Tweet tweet) {
        ArrayList<Tweet> tweets = getTweets(username);
        tweets.add(tweet);
        userData.get(username).put("tweets", tweets);
    }

    /**
     * Returns a list of retweets from a given user
     * @param username -> username to get retweets from
     * @return -> ArrayList<Tweet>, list of retweets by given user
     */
    public ArrayList<Tweet> getRetweets(String username) {
        return (ArrayList<Tweet>) userData.get(username).get("retweets");
    }

    /**
     * Adds a retweet to a users data
     * @param username -> username of account who created retweet
     * @param tweet -> tweet
     */
    public void addRetweet(String username, Tweet tweet) {
        ArrayList<Tweet> retweets = getRetweets(username);
        retweets.add(tweet);
        userData.get(username).put("retweets", retweets);
    }

    /**
     * Returns a list of retweets from a given user, where the tweet is originally written by the given author
     * @param username -> username to get retweets from
     * @param author -> username of tweet author
     * @return -> ArrayList<Tweet>, list of retweets by given user, written by given author
     */
    public ArrayList<Tweet> getRetweetsWhereAuthorIs(String username, String author) {
        return getRetweets(username).stream()
                .filter(tweet -> tweet.createdBy().equals(author))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a list of followers from a given user
     * @param username -> username to get followers from
     * @return -> ArrayList<Tweet>, list of followers of given user
     */
    public ArrayList<String> getFollowers(String username) {
        return (ArrayList<String>) userData.get(username).get("followers");
    }

    /**
     * Adds a follower to a users data
     * @param username -> username of account who is being followed
     * @param followerUsername -> username of account is followed
     */
    public void addFollower(String username, String followerUsername) {
        ArrayList<String> followers = getFollowers(username);
        followers.add(followerUsername);
        userData.get(username).put("followers", followers);
    }

    /**
     * Returns a list of following from a given user
     * @param username -> username to get following from
     * @return -> ArrayList<Tweet>, list of following of given user
     */
    public ArrayList<String> getFollowing(String username) {
        return (ArrayList<String>) userData.get(username).get("following");
    }

    /**
     * Adds a following to a users data
     * @param username -> username of account who is following
     * @param followingUsername -> username of account being followed
     */
    public void addFollowing(String username, String followingUsername) {
        ArrayList<String> following = getFollowing(username);
        if (!following.contains(followingUsername)) {
            following.add(followingUsername);
            userData.get(username).put("following", following);
            addFollower(followingUsername, username);
        }
    }

    /**
     * Remove an account from a users following list
     * @param username -> username of account that is unfollowing
     * @param unfollowingUsername -> username of account being unfollowed
     */
    public void unfollow(String username, String unfollowingUsername) {
        ArrayList<String> following = getFollowing(username);
        if (following.contains(unfollowingUsername)) {
            following.remove(unfollowingUsername);
            userData.get(username).put("following", following);
            removeFollower(unfollowingUsername, username);
        }
    }

    /**
     * Removes a follower from a users list of followers
     * @param username -> username of user to remove follower from
     * @param follower -> username of follower being removed
     */
    public void removeFollower(String username, String follower) {
        ArrayList<String> followers = getFollowers(username);
        followers.remove(follower);
        userData.get(username).put("followers", followers);
    }

    /**
     * Checks if the password attempt of a given username matches the true password
     * @param username -> username of account
     * @param password -> attempted password
     * @return -> true if passwords match, otherwise false
     */
    public boolean passwordEquals(String username, String password) {
        return getPasswordHash(username) == password.hashCode();
    }

    /**
     * Returns the password hash of a given user
     * @param username -> username associated to password hash
     * @return int -> password hash
     */
    private static int getPasswordHash(String username) {
        return (int) userData.get(username).get("password");
    }

    /**
     * Fills the data with hard-coded data
     */
    private static void fill() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            userData.put("coding_master", createUser(
                    Arrays.asList(new Tweet("Hello, world!", "coding_master", sdf.parse("01-01-2023")),
                            new Tweet("I love programming!", "coding_master", sdf.parse("01-02-2023"))),
                    Arrays.asList(new Tweet("Happy New Year!", "bob_the_dev", sdf.parse("01-01-2023")),
                            new Tweet("Java is amazing!", "fitness_is_everything", sdf.parse("01-03-2023"))),
                    Arrays.asList("bob_the_dev", "fitness_is_everything"),
                    Arrays.asList("bob_the_dev", "fitness_is_everything"),
                    92394
            ));

            userData.put("bob_the_dev", createUser(
                    Arrays.asList(new Tweet("Happy New Year!", "bob_the_dev", sdf.parse("01-01-2023")),
                            new Tweet("I'm learning Python!", "bob_the_dev", sdf.parse("01-03-2023"))),
                    Arrays.asList(new Tweet("Hello, world!", "coding_master", sdf.parse("01-01-2023")),
                            new Tweet("Java is amazing!", "fitness_is_everything", sdf.parse("01-03-2023"))),
                    Arrays.asList("coding_master", "fitness_is_everything"),
                    Arrays.asList("coding_master", "fitness_is_everything"),
                    23567
            ));

            userData.put("fitness_is_everything", createUser(
                    Arrays.asList(new Tweet("Just finished a 5K run!", "fitness_is_everything", sdf.parse("01-05-2023")),
                            new Tweet("Excited for the big game tonight!", "fitness_is_everything", sdf.parse("01-06-2023"))),
                    Arrays.asList(new Tweet("Hello, world!", "coding_master", sdf.parse("01-01-2023")),
                            new Tweet("Happy New Year!", "bob_the_dev", sdf.parse("01-01-2023"))),
                    Arrays.asList("coding_master", "bob_the_dev"),
                    Arrays.asList("coding_master", "bob_the_dev"),
                    56239
            ));

            userData.put("chris_da_hooper420", createUser(
                    Arrays.asList(new Tweet("Can't wait for the basketball playoffs!", "chris_da_hooper420", sdf.parse("01-05-2023")),
                            new Tweet("My favorite team won last night!", "chris_da_hooper420", sdf.parse("01-06-2023"))),
                    Arrays.asList(new Tweet("Java is amazing!", "fitness_is_everything", sdf.parse("01-03-2023")),
                            new Tweet("I'm learning Python!", "bob_the_dev", sdf.parse("01-03-2023"))),
                    Arrays.asList("bob_the_dev", "fitness_is_everything"),
                    Arrays.asList("bob_the_dev", "fitness_is_everything"),
                    74829
            ));

            userData.put("yasuo_goat", createUser(
                    Arrays.asList(new Tweet("Just reached level 100 in my favorite game!", "yasuo_goat", sdf.parse("01-10-2023")),
                            new Tweet("Can't wait for the gaming convention next week!", "yasuo_goat", sdf.parse("01-12-2023"))),
                    Arrays.asList(new Tweet("Working on a cool new app!", "chris_da_hooper420", sdf.parse("01-06-2023")),
                            new Tweet("Java is amazing!", "fitness_is_everything", sdf.parse("01-03-2023"))),
                    Arrays.asList("coding_master", "fitness_is_everything"),
                    Arrays.asList("coding_master", "fitness_is_everything"),
                    10475
            ));

            userData.put("Elden Ring Final Boss", createUser(
                    Arrays.asList(new Tweet("I just beat the final boss in my game!", "Elden Ring Final Boss", sdf.parse("01-15-2023")),
                            new Tweet("Started playing a new MMORPG today!", "Elden Ring Final Boss", sdf.parse("01-17-2023"))),
                    Arrays.asList(new Tweet("I'm learning Python!", "bob_the_dev", sdf.parse("01-03-2023")),
                            new Tweet("Java is amazing!", "fitness_is_everything", sdf.parse("01-03-2023"))),
                    Arrays.asList("coding_master", "chris_da_hooper420"),
                    Arrays.asList("coding_master", "chris_da_hooper420"),
                    45612
            ));

            userData.put("DJ Alex", createUser(
                    Arrays.asList(new Tweet("Just went to an amazing concert!", "DJ Alex", sdf.parse("01-20-2023")),
                            new Tweet("Discovered a new favorite band today!", "DJ Alex", sdf.parse("01-22-2023"))),
                    Arrays.asList(new Tweet("Java is amazing!", "fitness_is_everything", sdf.parse("01-03-2023")),
                            new Tweet("Working on a cool new app!", "chris_da_hooper420", sdf.parse("01-06-2023"))),
                    Arrays.asList("coding_master", "yasuo_goat"),
                    Arrays.asList("coding_master", "yasuo_goat"),
                    31247
            ));

            userData.put("guitar fanatic", createUser(
                    Arrays.asList(new Tweet("I can't stop listening to this new album!", "guitar fanatic", sdf.parse("01-25-2023")),
                            new Tweet("Practicing my guitar skills today!", "guitar fanatic", sdf.parse("01-27-2023"))),
                    Arrays.asList(new Tweet("I'm learning Python!", "bob_the_dev", sdf.parse("01-03-2023")),
                            new Tweet("Java is amazing!", "fitness_is_everything", sdf.parse("01-03-2023"))),
                    Arrays.asList("bob_the_dev", "Elden Ring Final Boss"),
                    Arrays.asList("bob_the_dev", "Elden Ring Final Boss"),
                    87459
            ));

            userData.put("frontend_wiz", createUser(
                    Arrays.asList(new Tweet("Launching my new website!", "frontend_wiz", sdf.parse("01-30-2023")),
                            new Tweet("Any suggestions for improvements?", "frontend_wiz", sdf.parse("01-31-2023"))),
                    Arrays.asList(new Tweet("Working on a cool new app!", "chris_da_hooper420", sdf.parse("01-06-2023")),
                            new Tweet("Java is amazing!", "fitness_is_everything", sdf.parse("01-03-2023"))),
                    Arrays.asList("chris_da_hooper420", "yasuo_goat"),
                    Arrays.asList("chris_da_hooper420", "yasuo_goat"),
                    38742
            ));

            userData.put("coding_noob123", createUser(
                    Arrays.asList(new Tweet("Excited to start a new course!", "coding_noob123", sdf.parse("02-01-2023")),
                            new Tweet("Learning never stops!", "coding_noob123", sdf.parse("02-02-2023"))),
                    Arrays.asList(new Tweet("Hello, world!", "coding_master", sdf.parse("01-01-2023")),
                            new Tweet("Happy New Year!", "bob_the_dev", sdf.parse("01-01-2023"))),
                    Arrays.asList("coding_master", "DJ Alex"),
                    Arrays.asList("coding_master", "DJ Alex"),
                    21874
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a user by creating a hashtable for that users data
     * @param tweets -> list of tweets
     * @param retweets -> list of retweets
     * @param followers -> list of accounts following user
     * @param following -> list of accounts user follows
     * @param passwordHash -> password hash of user
     * @return HashTable<String, Object> -> users data
     */
    private static HashTable<String, Object> createUser(
            List<Tweet> tweets,
            List<Tweet> retweets,
            List<String> followers,
            List<String> following,
            int passwordHash
    ) {
        HashTable<String, Object> user = new HashTable<String, Object>();
        user.put("tweets", new ArrayList<>(tweets));
        user.put("retweets", new ArrayList<>(retweets));
        user.put("followers", new ArrayList<>(followers));
        user.put("following", new ArrayList<>(following));
        user.put("password", passwordHash);
        return user;
    }

    /**
     * String representation of data
     * @return String -> string representation of data
     */
    @Override
    public String toString() {
        StringBuilder json = new StringBuilder("{");

        int userCounter = 0;
        for (String username : userData.keySet()) {
            HashTable<String, Object> user = userData.get(username);
            json.append("\"").append(username).append("\": {");

            json.append("\"tweets\": [");
            ArrayList<Tweet> tweets = (ArrayList<Tweet>) user.get("tweets");
            for (int i = 0; i < tweets.size(); i++) {
                json.append(tweets.get(i).toJson());
                if (i < tweets.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("], ");

            json.append("\"retweets\": [");
            ArrayList<Tweet> retweets = (ArrayList<Tweet>) user.get("retweets");
            for (int i = 0; i < retweets.size(); i++) {
                json.append(retweets.get(i).toJson());
                if (i < retweets.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("], ");

            json.append("\"followers\": [");
            ArrayList<String> followers = (ArrayList<String>) user.get("followers");
            for (int i = 0; i < followers.size(); i++) {
                json.append("\"").append(followers.get(i)).append("\"");
                if (i < followers.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("], ");

            json.append("\"following\": [");
            ArrayList<String> following = (ArrayList<String>) user.get("following");
            for (int i = 0; i < following.size(); i++) {
                json.append("\"").append(following.get(i)).append("\"");
                if (i < following.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("], ");

            json.append("\"password\": ").append(user.get("password"));

            json.append("}");

            if (userCounter < userData.size() - 1) {
                json.append(", ");
            }
            userCounter++;
        }

        json.append("}");
        return json.toString();
    }
}
