package main.model.boardItems;

import main.model.Board;
import main.model.BoardSpace;

import java.util.ArrayList;

final public class Miner extends BoardItem {
    private char direction;
    private Board board;

    // directions:
    // u - up
    // r - right
    // d - down
    // l - left
    public Miner(Board board) {
        super(0, 0, true);
        this.direction = 'r';
        this.board = board;
    }

    /**
     * Moves te miner 1 step forward depending on its location
     */
    public void front() {
        ArrayList<ArrayList<BoardSpace>> aBoard = board.getBoard();
        switch(this.direction) {
            case 'r':
                if(getXPos() + 1 < aBoard.size()) board.moveBoardItem(this, getXPos() + 1, getYPos());
                else System.out.println("Can't move the miner 1 step right");
                break;
            case 'l':
                if(getXPos() - 1 >= 0) board.moveBoardItem(this, getXPos() - 1, getYPos());
                else System.out.println("Can't move the miner 1 step left");
                break;
            case 'u':
                if(getYPos() - 1 >= 0) board.moveBoardItem(this, getXPos(), getYPos() - 1);
                else System.out.println("Can't move the miner 1 step up");
                break;
            case 'd':
                if(getYPos() + 1 < aBoard.size()) board.moveBoardItem(this, getXPos(), getYPos() + 1);
                else System.out.println("Can't move the miner 1 step down");
                break;
        }
    }

    /**
     * Rotates the Miner clockwise
     */
    public void rotate() {
        switch (this.direction) {
            case 'u':
                // up
                this.direction = 'r';
                break;
            case 'd':
                // down
                this.direction = 'l';
                break;
            case 'l':
                // left
                this.direction = 'u';
                break;
            case 'r':
                // right
                this.direction = 'd';
                break;
            default:
                System.out.println("Direction is not in the choices.");
        }
    }

    /**
     * Scans the direction where the Miner is facing
     * @return integer determining what has been found
     */
    public int scan() {
        int result = -1;
        System.out.println(direction);
        switch(this.direction) {
            case 'u':
                result = scanUp(getYPos(), getXPos());
                break;
            case 'd':
                result = scanDown(getYPos(), getXPos());
                break;
            case 'l':
                result = scanLeft(getYPos(), getXPos());
                break;
            case 'r':
                result = scanRight(getYPos(), getXPos());
                break;
        }

        return result;
    }

    /**
     * Given a row and column, return the nearest item that it sees in its left
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item on the left of the miner
     */
    private int scanLeft(int row, int column) {
        // checks the all spaces in the left of the miner until an item is found
        for(int c = column - 1; c >= 0; c--) {
            for(BoardItem boardItem : board.getBoard().get(row).get(c).getBoardItems()) {
                if(boardItem instanceof Beacon) return 1;
                else if(boardItem instanceof Pit) return 2;
                else if(boardItem instanceof GoldPot) return 3;
            }
        }

        return -1;
    }

    /**
     * Given a row and column, return the nearest item that it sees in its right
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item on the right of the miner
     */
    private int scanRight(int row, int column) {
        // checks the all spaces in the right of the miner until an item is found
        for(int c = column + 1; c < board.getBoard().get(row).size(); c++) {
            for(BoardItem boardItem : board.getBoard().get(row).get(c).getBoardItems()) {
                if(boardItem instanceof Beacon) return 1;
                else if(boardItem instanceof Pit) return 2;
                else if(boardItem instanceof GoldPot) return 3;
            }
        }

        return -1;
    }

    /**
     * Given a row and column, return the nearest item that it sees above it
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item above the miner
     */
    private int scanUp(int row, int column) {
        // checks the all spaces in the above the miner until an item is found
        for(int r = row - 1; r >= 0; r--) {
            for(BoardItem boardItem : board.getBoard().get(r).get(column).getBoardItems()) {
                if(boardItem instanceof Beacon) return 1;
                else if(boardItem instanceof Pit) return 2;
                else if(boardItem instanceof GoldPot) return 3;
            }
        }

        return -1;
    }

    /**
     * Given a row and column, return the nearest item that it sees below it
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item below the miner
     */
    private int scanDown(int row, int column) {
        // checks the all spaces in the below the miner until an item is found
        for(int r = row + 1; r < board.getBoard().get(0).size(); r++) {
            for(BoardItem boardItem : board.getBoard().get(r).get(column).getBoardItems()) {
                if(boardItem instanceof Beacon) return 1;
                else if(boardItem instanceof Pit) return 2;
                else if(boardItem instanceof GoldPot) return 3;
            }
        }

        return -1;
    }

    public char getDirection() {
        return direction;
    }
}
