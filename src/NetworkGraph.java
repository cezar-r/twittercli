import java.util.HashSet;

public class NetworkGraph {

    private static UserData userData;
    private static String startingNode;
    private static final HashTable<String, HashSet<String>> graph = new HashTable<>();

    public NetworkGraph(UserData newUserData) {
        userData = newUserData;
        buildGraph();
    }

    private static void buildGraph() {
        for (String username: userData.getAllUsernames()) {
            graph.put(username, new HashSet<>(userData.getFollowing(username)));
            if (startingNode == null) {
                startingNode = username;
            }
        }
    }

    public void addEdge(String fromUser, String toUser) {
        graph.get(fromUser).add(toUser);
    }

    public void addNode(String user) {
        graph.put(user, new HashSet<>());
    }

    public void setStartingNode(String user) {
        startingNode = user;
    }


}
