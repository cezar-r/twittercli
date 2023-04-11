/**
 * @file: CoupleSorting.java
 * @description: This file contains a class that allows for couple sorting. Couple sorting is when you
 * have two lists that are associated with each other, and you want to sort one of the lists based on
 * how the other list is sorted
 * @author: Connor Olive
 */

import java.util.*;

/**
 * Class that contains static methods for couple sorting
 */
public class CoupleSorting {

    /**
     * Only public-facing API for this class. Sorts the toSort list based on values passed in score
     * @param toSort -> list to sort
     * @param scores -> values to use to sort list by
     */
    public static void sortBy(ArrayList toSort, ArrayList<Float> scores) {
        int n = toSort.size();

        // insertion sort is faster for very small unsorted arrays
        if (n < 16) {
            insertionSort(toSort, scores);
        } else {
            mergeSort(toSort, scores);
        }
    }

    /**
     * Implementation of insertion sort, using the values in scores to sort toSort
     * @param toSort -> list to sort
     * @param scores -> values to use to sort list by
     */
    private static void insertionSort(ArrayList toSort, ArrayList<Float> scores) {
        for (int i = 1; i < toSort.size(); i++) {
            Object item = toSort.get(i);
            Float score = scores.get(i);
            int j = i - 1;

            while (j >= 0 && scores.get(j) < score) {
                toSort.set(j + 1, toSort.get(j));
                scores.set(j + 1, scores.get(j));
                j--;
            }
            toSort.set(j + 1, item);
            scores.set(j + 1, score);
        }
    }

    /**
     * Implementation of merge sort, using the values in scores to sort toSort
     * @param toSort -> list to sort
     * @param scores -> values to use to sort list by
     */
    private static void mergeSort(ArrayList toSort, ArrayList<Float> scores) {
        if (toSort.size() <= 1) {
            return;
        }

        int middle = toSort.size() / 2;
        ArrayList<Object> left = new ArrayList<>(toSort.subList(0, middle));
        ArrayList<Float> leftScores = new ArrayList<>(scores.subList(0, middle));
        ArrayList<Object> right = new ArrayList<>(toSort.subList(middle, toSort.size()));
        ArrayList<Float> rightScores = new ArrayList<>(scores.subList(middle, scores.size()));

        mergeSort(left, leftScores);
        mergeSort(right, rightScores);
        merge(toSort, scores, left, leftScores, right, rightScores);
    }

    /**
     * Helper method for mergeSort to merge two sub arrays
     * @param toSort -> list to sort
     * @param scores -> values to use to sort list by
     * @param left -> left subarray of toSort
     * @param leftScores -> left subarray of scores
     * @param right -> right subarray of toSort
     * @param rightScores -> right subarray of scores
     */
    private static void merge(ArrayList<Object> toSort, ArrayList<Float> scores,
                              ArrayList<Object> left, ArrayList<Float> leftScores,
                              ArrayList<Object> right, ArrayList<Float> rightScores) {
        int leftIndex = 0, rightIndex = 0, index = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (leftScores.get(leftIndex) >= rightScores.get(rightIndex)) {
                toSort.set(index, left.get(leftIndex));
                scores.set(index, leftScores.get(leftIndex));
                leftIndex++;
            } else {
                toSort.set(index, right.get(rightIndex));
                scores.set(index, rightScores.get(rightIndex));
                rightIndex++;
            }
            index++;
        }

        while (leftIndex < left.size()) {
            toSort.set(index, left.get(leftIndex));
            scores.set(index, leftScores.get(leftIndex));
            leftIndex++;
            index++;
        }

        while (rightIndex < right.size()) {
            toSort.set(index, right.get(rightIndex));
            scores.set(index, rightScores.get(rightIndex));
            rightIndex++;
            index++;
        }
    }
}
