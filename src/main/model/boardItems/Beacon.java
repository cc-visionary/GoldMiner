package main.model.boardItems;

final public class Beacon extends BoardItem {
    private int stepsToGoldPot;
    private boolean activated = false;

    public Beacon(int xPos, int yPos, int gxPos, int gyPos) {
        super(xPos, yPos);

        if(xPos == gxPos) stepsToGoldPot = Math.abs(yPos - gyPos) - 1;
        else if(yPos == gyPos) stepsToGoldPot = Math.abs(xPos - gxPos) - 1;
        else stepsToGoldPot = -1;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setStepsToGoldPot(int stepsToGoldPot) {
        this.stepsToGoldPot = stepsToGoldPot;
    }

    public int getStepsToGoldPot() {
        return stepsToGoldPot;
    }

    public boolean isActivated() {
        return activated;
    }
}
