package main.model.boardItems;

import main.model.Board;

final public class Beacon extends BoardItem {
    int stepsToGoldPot;
    public Beacon(int xPos, int yPos, int gxPos, int gyPos) {
        super(xPos, yPos);
        stepsToGoldPot = Math.abs(xPos - gxPos) + Math.abs(yPos - gyPos) - 1;
    }

    public int getStepsToGoldPot() {
        return stepsToGoldPot;
    }
}
