package main.model;

import main.model.boardItems.BoardItem;

import java.util.ArrayList;

public class Agent {
    private static BoardItem scannedItem;
    private static ArrayList<Character> fronts = new ArrayList<>();

    public static void addFront(char front) {
        fronts.add(front);
    }

    public static void setScannedItem(BoardItem scannedItem) {
        Agent.scannedItem = scannedItem;
    }

    public static ArrayList<Character> getFronts() {
        return fronts;
    }

    public static BoardItem getScannedItem() {
        return scannedItem;
    }
}
