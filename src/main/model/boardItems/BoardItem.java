package main.model.boardItems;

import main.model.Board;

abstract public class BoardItem {
    int xPos, yPos;
    boolean movable;

    public BoardItem(int xPos, int yPos, boolean movable) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.movable = movable;
    }

    public BoardItem(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.movable = false;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public boolean isMovable() {
        return movable;
    }
}
