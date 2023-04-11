import java.util.*;
import java.util.stream.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class UserData {

    private static final HashTable<String, HashTable<String, Object>> userData = new HashTable<>();

    public UserData() {
        fill();
    }

    public HashTable<String, Object> getUser(String username) {
        return userData.get(username);
    }

    public Set<String> getAllUsernames() {
        return userData.keySet();
    }

    public void registerUser(String username, String password) {
        HashTable<String, Object> newUserData = new HashTable<>();
        newUserData.put("tweets", new ArrayList<Tweet>());
        newUserData.put("retweets", new ArrayList<Tweet>());
        newUserData.put("followers", new ArrayList<String>());
        newUserData.put("following", new ArrayList<String>());
        newUserData.put("password", password.hashCode());
        userData.put(username, newUserData);
    }

    public ArrayList<Tweet> getTweets(String username) {
        return (ArrayList<Tweet>) userData.get(username).get("tweets");
    }

    public void addTweet(String username, Tweet tweet) {
        ArrayList<Tweet> tweets = getTweets(username);
        tweets.add(tweet);
        userData.get(username).put("tweets", tweets);
    }

    public ArrayList<Tweet> getRetweets(String username) {
        return (ArrayList<Tweet>) userData.get(username).get("retweets");
    }

    public void addRetweet(String username, Tweet tweet) {
        ArrayList<Tweet> retweets = getRetweets(username);
        retweets.add(tweet);
        userData.get(username).put("retweets", retweets);
    }

    public ArrayList<Tweet> getRetweetsWhereAuthorIs(String username, String author) {
        return getRetweets(username).stream()
                .filter(tweet -> tweet.createdBy().equals(author))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<String> getFollowers(String username) {
        return (ArrayList<String>) userData.get(username).get("followers");
    }

    public void addFollower(String username, String followerUsername) {
        ArrayList<String> followers = getFollowers(username);
        followers.add(followerUsername);
        userData.get(username).put("followers", followers);
    }

    public ArrayList<String> getFollowing(String username) {
        return (ArrayList<String>) userData.get(username).get("following");
    }

    public void addFollowing(String username, String followingUsername) {
        ArrayList<String> following = getFollowing(username);
        if (!following.contains(followingUsername)) {
            following.add(followingUsername);
            userData.get(username).put("following", following);
            addFollower(followingUsername, username);
        }
    }

    public void unfollow(String username, String unfollowingUsername) {
        ArrayList<String> following = getFollowing(username);
        if (following.contains(unfollowingUsername)) {
            following.remove(unfollowingUsername);
            userData.get(username).put("following", following);
            removeFollower(unfollowingUsername, username);
        }
    }

    public void removeFollower(String username, String follower) {
        ArrayList<String> followers = getFollowers(username);
        followers.remove(follower);
        userData.get(username).put("followers", followers);
    }

    public boolean passwordEquals(String username, String password) {
        return getPasswordHash(username) == password.hashCode();
    }

    private static int getPasswordHash(String username) {
        return (int) userData.get(username).get("password");
    }

    private static void fill() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            userData.put("user1", createUser(
                    Arrays.asList(new Tweet("Hello, world!", "user1", sdf.parse("01-01-2023")),
                            new Tweet("I love programming!", "user1", sdf.parse("01-02-2023"))),
                    Arrays.asList(new Tweet("Happy New Year!", "user2", sdf.parse("01-01-2023")),
                            new Tweet("Java is amazing!", "user3", sdf.parse("01-03-2023"))),
                    Arrays.asList("user2", "user3"),
                    Arrays.asList("user2", "user3"),
                    92394
            ));

            userData.put("user2", createUser(
                    Arrays.asList(new Tweet("Happy New Year!", "user2", sdf.parse("01-01-2023")),
                            new Tweet("I'm learning Python!", "user2", sdf.parse("01-03-2023"))),
                    Arrays.asList(new Tweet("Hello, world!", "user1", sdf.parse("01-01-2023")),
                            new Tweet("Java is amazing!", "user3", sdf.parse("01-03-2023"))),
                    Arrays.asList("user1", "user3"),
                    Arrays.asList("user1", "user3"),
                    23567
            ));

            userData.put("user3", createUser(
                    Arrays.asList(new Tweet("Just finished a 5K run!", "user3", sdf.parse("01-05-2023")),
                            new Tweet("Excited for the big game tonight!", "user3", sdf.parse("01-06-2023"))),
                    Arrays.asList(new Tweet("Hello, world!", "user1", sdf.parse("01-01-2023")),
                            new Tweet("Happy New Year!", "user2", sdf.parse("01-01-2023"))),
                    Arrays.asList("user1", "user2"),
                    Arrays.asList("user1", "user2"),
                    56239
            ));

            userData.put("user4", createUser(
                    Arrays.asList(new Tweet("Can't wait for the basketball playoffs!", "user4", sdf.parse("01-05-2023")),
                            new Tweet("My favorite team won last night!", "user4", sdf.parse("01-06-2023"))),
                    Arrays.asList(new Tweet("Java is amazing!", "user3", sdf.parse("01-03-2023")),
                            new Tweet("I'm learning Python!", "user2", sdf.parse("01-03-2023"))),
                    Arrays.asList("user2", "user3"),
                    Arrays.asList("user2", "user3"),
                    74829
            ));

            userData.put("user5", createUser(
                    Arrays.asList(new Tweet("Just reached level 100 in my favorite game!", "user5", sdf.parse("01-10-2023")),
                            new Tweet("Can't wait for the gaming convention next week!", "user5", sdf.parse("01-12-2023"))),
                    Arrays.asList(new Tweet("Working on a cool new app!", "user4", sdf.parse("01-06-2023")),
                            new Tweet("Java is amazing!", "user3", sdf.parse("01-03-2023"))),
                    Arrays.asList("user1", "user3"),
                    Arrays.asList("user1", "user3"),
                    10475
            ));

            userData.put("user6", createUser(
                    Arrays.asList(new Tweet("I just beat the final boss in my game!", "user6", sdf.parse("01-15-2023")),
                            new Tweet("Started playing a new MMORPG today!", "user6", sdf.parse("01-17-2023"))),
                    Arrays.asList(new Tweet("I'm learning Python!", "user2", sdf.parse("01-03-2023")),
                            new Tweet("Java is amazing!", "user3", sdf.parse("01-03-2023"))),
                    Arrays.asList("user1", "user4"),
                    Arrays.asList("user1", "user4"),
                    45612
            ));

            userData.put("user7", createUser(
                    Arrays.asList(new Tweet("Just went to an amazing concert!", "user7", sdf.parse("01-20-2023")),
                            new Tweet("Discovered a new favorite band today!", "user7", sdf.parse("01-22-2023"))),
                    Arrays.asList(new Tweet("Java is amazing!", "user3", sdf.parse("01-03-2023")),
                            new Tweet("Working on a cool new app!", "user4", sdf.parse("01-06-2023"))),
                    Arrays.asList("user1", "user5"),
                    Arrays.asList("user1", "user5"),
                    31247
            ));

            userData.put("user8", createUser(
                    Arrays.asList(new Tweet("I can't stop listening to this new album!", "user8", sdf.parse("01-25-2023")),
                            new Tweet("Practicing my guitar skills today!", "user8", sdf.parse("01-27-2023"))),
                    Arrays.asList(new Tweet("I'm learning Python!", "user2", sdf.parse("01-03-2023")),
                            new Tweet("Java is amazing!", "user3", sdf.parse("01-03-2023"))),
                    Arrays.asList("user2", "user6"),
                    Arrays.asList("user2", "user6"),
                    87459
            ));

            userData.put("user9", createUser(
                    Arrays.asList(new Tweet("Launching my new website!", "user9", sdf.parse("01-30-2023")),
                            new Tweet("Any suggestions for improvements?", "user9", sdf.parse("01-31-2023"))),
                    Arrays.asList(new Tweet("Working on a cool new app!", "user4", sdf.parse("01-06-2023")),
                            new Tweet("Java is amazing!", "user3", sdf.parse("01-03-2023"))),
                    Arrays.asList("user4", "user5"),
                    Arrays.asList("user4", "user5"),
                    38742
            ));

            userData.put("user10", createUser(
                    Arrays.asList(new Tweet("Excited to start a new course!", "user10", sdf.parse("02-01-2023")),
                            new Tweet("Learning never stops!", "user10", sdf.parse("02-02-2023"))),
                    Arrays.asList(new Tweet("Hello, world!", "user1", sdf.parse("01-01-2023")),
                            new Tweet("Happy New Year!", "user2", sdf.parse("01-01-2023"))),
                    Arrays.asList("user1", "user7"),
                    Arrays.asList("user1", "user7"),
                    21874
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

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
