package main.model.boardItems;

import main.model.Agent;
import main.model.Board;
import main.model.BoardSpace;

import java.util.ArrayList;
import java.util.Random;

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
                if(getXPos() + 1 < aBoard.size()) board.moveBoardItem(this, getXPos() + 1, getYPos(), false);
                else System.out.println("Can't move the miner 1 step right");
                break;
            case 'l':
                if(getXPos() - 1 >= 0) board.moveBoardItem(this, getXPos() - 1, getYPos(), false);
                else System.out.println("Can't move the miner 1 step left");
                break;
            case 'u':
                if(getYPos() - 1 >= 0) board.moveBoardItem(this, getXPos(), getYPos() - 1, false);
                else System.out.println("Can't move the miner 1 step up");
                break;
            case 'd':
                if(getYPos() + 1 < aBoard.size()) board.moveBoardItem(this, getXPos(), getYPos() + 1, false);
                else System.out.println("Can't move the miner 1 step down");
                break;
        }
        board.getStatistics().addFront();
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
        board.getStatistics().addRotate();
    }

    /**
     * Scans the direction where the Miner is facing
     * @return integer determining what has been found
     */
    public BoardItem scan() {
        BoardItem result = null;
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
        board.getStatistics().addScan();

        return result;
    }

    /**
     * Given a row and column, return the nearest item that it sees in its left
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item on the left of the miner
     */
    private BoardItem scanLeft(int row, int column) {
        // checks the all spaces in the left of the miner until an item is found
        for(int c = column - 1; c >= 0; c--) {
            for(BoardItem boardItem : board.getBoard().get(row).get(c).getBoardItems()) return boardItem;
        }

        return null;
    }

    /**
     * Given a row and column, return the nearest item that it sees in its right
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item on the right of the miner
     */
    private BoardItem scanRight(int row, int column) {
        // checks the all spaces in the right of the miner until an item is found
        for(int c = column + 1; c < board.getBoard().get(row).size(); c++) {
            for(BoardItem boardItem : board.getBoard().get(row).get(c).getBoardItems()) return boardItem;
        }

        return null;
    }

    /**
     * Given a row and column, return the nearest item that it sees above it
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item above the miner
     */
    private BoardItem scanUp(int row, int column) {
        // checks the all spaces in the above the miner until an item is found
        for(int r = row - 1; r >= 0; r--) {
            for(BoardItem boardItem : board.getBoard().get(r).get(column).getBoardItems()) return boardItem;
        }

        return null;
    }

    /**
     * Given a row and column, return the nearest item that it sees below it
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item below the miner
     */
    private BoardItem scanDown(int row, int column) {
        // checks the all spaces in the below the miner until an item is found
        for(int r = row + 1; r < board.getBoard().get(0).size(); r++) {
            for(BoardItem boardItem : board.getBoard().get(r).get(column).getBoardItems()) return boardItem;
        }

        return null;
    }

    /**
     * Determines whether or not the player has failed at the pit or not
     * @return yes/no
     */
    public boolean didFallOnPit() {
        for(BoardItem boardItem : board.getBoard().get(getYPos()).get(getXPos()).getBoardItems()) {
            if(boardItem instanceof Pit) return true;
        }

        return false;
    }

    /**
     * Determines whether or not the player has reached the gold pot and won or not
     * @return yes/no
     */
    public boolean didReachGoldPot() {
        for(BoardItem boardItem : board.getBoard().get(getYPos()).get(getXPos()).getBoardItems()) {
            if(boardItem instanceof GoldPot) return true;
        }

        return false;
    }

    /**
     * Lets the Miner do a Random Move
     */
    public void randomMove() {
        Random randomizer = new Random();
        switch(randomizer.nextInt(2) + 1) {
            case 1:
                front();
                break;
            case 2:
                rotate();
                break;
        }
    }

    /**
     * Lets the Miner do a Smart Move
     */
    public void smartMove() {
        if(Agent.getScannedItem() == null) {
            BoardItem boardItem = scan();
            if(boardItem != null) Agent.setScannedItem(boardItem);
        } else {
            if(Agent.getScannedItem() instanceof GoldPot) {
                front();
            } else if(Agent.getScannedItem() instanceof Pit) {
                rotate();
                Agent.setScannedItem(null);
            } else if(Agent.getScannedItem() instanceof Beacon) {

            }
        }
    }

    public char getDirection() {
        return direction;
    }
}
