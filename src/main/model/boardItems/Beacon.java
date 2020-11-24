package main.model.boardItems;

import main.model.Board;
import main.model.BoardSpace;

import java.util.ArrayList;

final public class Beacon extends BoardItem {
    private int stepsToGoldPot;

    public Beacon(int xPos, int yPos, int gxPos, int gyPos) {
        super(xPos, yPos);

        if(xPos == gxPos) stepsToGoldPot = Math.abs(yPos - gyPos) - 1;
        else if(yPos == gyPos) stepsToGoldPot = Math.abs(xPos - gxPos) - 1;
        else stepsToGoldPot = -1;
    }

    public void setStepsToGoldPot(int stepsToGoldPot) {
        this.stepsToGoldPot = stepsToGoldPot;
    }

    public int getStepsToGoldPot() {
        return stepsToGoldPot;
    }
}
