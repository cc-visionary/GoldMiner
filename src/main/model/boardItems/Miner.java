package main.model.boardItems;

import main.model.Board;

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
        switch(this.direction) {
            case 'u':
                scanUp(getXPos(), getYPos());
                break;
            case 'd':
                scanDown(getXPos(), getYPos());
                break;
            case 'l':
                scanLeft(getXPos(), getYPos());
                break;
            case 'r':
                scanRight(getXPos(), getYPos());
                break;
        }

        board.getBoard();

        return result;
    }

    /**
     * Given a row and column, return the nearest item that it sees in its left
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item on the left of the miner
     */
    private int scanLeft(int row, int column) {
        int item = -1;
        int i = row;
        // checks the all spaces in the left of the miner until an item is found
        for(int j = column - 1; j > 0; j--) {
            BoardItem boardItem = board.getBoard().get(i).get(j).getBoardItem();
            if(boardItem != null) {
                if(boardItem instanceof Beacon) item = 1;
                else if(boardItem instanceof Pit) item = 2;
                else if(boardItem instanceof GoldPot) item = 3;
                break;
            }
        }

        return item;
    }

    /**
     * Given a row and column, return the nearest item that it sees in its right
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item on the right of the miner
     */
    private int scanRight(int row, int column) {
        int item = -1;
        int i = row;
        // checks the all spaces in the right of the miner until an item is found
        for(int j = column + 1; j < board.getBoard().get(i).size(); j++) {
            BoardItem boardItem = board.getBoard().get(i).get(j).getBoardItem();
            if(boardItem != null) {
                if(boardItem instanceof Beacon) item = 1;
                else if(boardItem instanceof Pit) item = 2;
                else if(boardItem instanceof GoldPot) item = 3;
                break;
            }
        }

        return item;
    }

    /**
     * Given a row and column, return the nearest item that it sees above it
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item above the miner
     */
    private int scanUp(int row, int column) {
        int item = -1;
        int j = column;
        // checks the all spaces in the above the miner until an item is found
        for(int i = row - 1; i > 0; i--) {
            BoardItem boardItem = board.getBoard().get(i).get(j).getBoardItem();
            if(boardItem != null) {
                if(boardItem instanceof Beacon) item = 1;
                else if(boardItem instanceof Pit) item = 2;
                else if(boardItem instanceof GoldPot) item = 3;
                break;
            }
        }

        return item;
    }

    /**
     * Given a row and column, return the nearest item that it sees below it
     * @param row    x location of the miner
     * @param column y location of the miner
     * @return       nearest item below the miner
     */
    private int scanDown(int row, int column) {
        int item = -1;
        int j = column;
        // checks the all spaces in the below the miner until an item is found
        for(int i = row + 1; i < board.getBoard().get(i).size(); i++) {
            BoardItem boardItem = board.getBoard().get(i).get(j).getBoardItem();
            if(boardItem != null) {
                if(boardItem instanceof Beacon) item = 1;
                else if(boardItem instanceof Pit) item = 2;
                else if(boardItem instanceof GoldPot) item = 3;
                break;
            }
        }

        return item;
    }

    public char getDirection() {
        return direction;
    }
}
