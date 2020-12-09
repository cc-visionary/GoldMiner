package main.model.boardItems;

final public class Beacon extends BoardItem {
    private int stepsToGoldPot;
    private boolean activated = false;

    public Beacon(int xPos, int yPos) {
        super(xPos, yPos);
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
