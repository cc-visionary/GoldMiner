package main.model;

import main.model.boardItems.BoardItem;

import java.util.ArrayList;
import java.util.Map;

public class Agent {
    private static BoardItem scannedItem;
    private static ArrayList<Character> fronts = new ArrayList<>();
    private static int currRotate;
    private static boolean foundUsefulBeacon = false, isBacktracking = false;

    public static void addFront(char front) {
        fronts.add(front);
    }

    public static void setScannedItem(BoardItem scannedItem) {
        Agent.scannedItem = scannedItem;
    }

    public static void setCurrRotate(int currRotate) {
        Agent.currRotate = currRotate;
    }

    public static void setFoundUsefulBeacon(boolean foundUsefulBeacon) {
        Agent.foundUsefulBeacon = foundUsefulBeacon;
    }

    public static void setIsBacktracking(boolean isBacktracking) {
        Agent.isBacktracking = isBacktracking;
    }

    public static ArrayList<Character> getFronts() {
        return fronts;
    }

    public static BoardItem getScannedItem() {
        return scannedItem;
    }

    public static int getCurrRotate() {
        return currRotate;
    }

    public static boolean isFoundUsefulBeacon() {
        return foundUsefulBeacon;
    }

    public static boolean isBacktracking() {
        return isBacktracking;
    }

    public static void resetValues() {
        setScannedItem(null);
        setCurrRotate(0);
        setFoundUsefulBeacon(false);
    }
}
