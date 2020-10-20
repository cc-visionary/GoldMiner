package main.model.boardItems;

import main.model.Board;

abstract public class BoardItem {
    int xPos, yPos;
    Board board;

    public BoardItem(Board board, int xPos, int yPos) {
        this.board = board;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public BoardItem(Board board) {
        this.board = board;
        this.xPos = 0;
        this.yPos = 0;
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
}
