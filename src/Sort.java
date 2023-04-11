import java.util.ArrayList;

public class Sort {
	public static ArrayList<Tweet> mergesort(ArrayList<Tweet> tweets, ArrayList<Float> ratings) {
        if (tweets.size() <= 1) {
            return tweets;
        }
        int mid = tweets.size() / 2;
        ArrayList<Tweet> left = new ArrayList<>(tweets.subList(0, mid));
        ArrayList<Float> leftRatings = new ArrayList<>(ratings.subList(0, mid));
        ArrayList<Tweet> right = new ArrayList<>(tweets.subList(mid, tweets.size()));
        ArrayList<Float> rightRatings = new ArrayList<>(ratings.subList(mid, ratings.size()));
        left = mergesort(left, leftRatings);
        right = mergesort(right, rightRatings);
        return merge(left, leftRatings, right, rightRatings);
    }

    private static ArrayList<Tweet> merge(ArrayList<Tweet> left, ArrayList<Float> leftRatings,
                                           ArrayList<Tweet> right, ArrayList<Float> rightRatings) {
        ArrayList<Tweet> result = new ArrayList<>();
        ArrayList<Float> resultRatings = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size()) {
            if (leftRatings.get(i) > rightRatings.get(j)) {
                result.add(left.get(i));
                resultRatings.add(leftRatings.get(i));
                i++;
            } else {
                result.add(right.get(j));
                resultRatings.add(rightRatings.get(j));
                j++;
            }
        }
        result.addAll(left.subList(i, left.size()));
        resultRatings.addAll(leftRatings.subList(i, left.size()));
        result.addAll(right.subList(j, right.size()));
        resultRatings.addAll(rightRatings.subList(j, right.size()));
        return result;
    }

    public static ArrayList<Tweet> insertionSort(ArrayList<Tweet> tweets, ArrayList<Float> ratings) {
        for (int i = 1; i < tweets.size(); i++) {
            Tweet temp = tweets.get(i);
            float tempRating = ratings.get(i);
            int j = i - 1;
            while (j >= 0 && ratings.get(j) < tempRating) {
                tweets.set(j + 1, tweets.get(j));
                ratings.set(j + 1, ratings.get(j));
                j--;
            }
            tweets.set(j + 1, temp);
            ratings.set(j + 1, tempRating);
        }
        return tweets;
    }

    public static ArrayList<Tweet> hybridSort(ArrayList<Tweet> tweets, ArrayList<Float> ratings) {
        if (tweets.size() <= 10) {
            return insertionSort(tweets, ratings);
        } else {
            return mergesort(tweets, ratings);
        }
    }
}
