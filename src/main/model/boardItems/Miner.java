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
     * @return boolean value determining whether front was successful or not
     */
    public void front() {
        ArrayList<ArrayList<BoardSpace>> aBoard = board.getBoard();
        if(canFront()) {
            switch(this.direction) {
                case 'r':
                    if(getXPos() + 1 < aBoard.size()) board.moveBoardItem(this, getXPos() + 1, getYPos(), false);
                    break;
                case 'l':
                    if(getXPos() - 1 >= 0) board.moveBoardItem(this, getXPos() - 1, getYPos(), false);
                    break;
                case 'u':
                    if(getYPos() - 1 >= 0) board.moveBoardItem(this, getXPos(), getYPos() - 1, false);
                    break;
                case 'd':
                    if(getYPos() + 1 < aBoard.size()) board.moveBoardItem(this, getXPos(), getYPos() + 1, false);
                    break;
            }
            board.getStatistics().addFront();
        } else System.out.println("Can't move 1 step forward.");
    }

    private boolean canFront() {
        ArrayList<ArrayList<BoardSpace>> aBoard = board.getBoard();
        boolean front = false;
        switch(this.direction) {
            case 'r':
                if(getXPos() + 1 < aBoard.size()) front = true;
                break;
            case 'l':
                if(getXPos() - 1 >= 0) front = true;
                break;
            case 'u':
                if(getYPos() - 1 >= 0) front = true;
                break;
            case 'd':
                if(getYPos() + 1 < aBoard.size()) front = true;
                break;
        }
        return front;
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
                if(getYPos() - 1 >= 0) {
                    for(BoardItem boardItem : board.getBoard().get(getYPos() - 1).get(getXPos()).getBoardItems()) {
                        result = boardItem;
                        break;
                    }
                }
                break;
            case 'd':
                if(getYPos() + 1 < board.getBoard().size())  {
                    for(BoardItem boardItem : board.getBoard().get(getYPos() + 1).get(getXPos()).getBoardItems()) {
                        result = boardItem;
                        break;
                    }
                }
                break;
            case 'l':
                if(getXPos() - 1 >= 0) {
                    for(BoardItem boardItem : board.getBoard().get(getYPos()).get(getXPos() - 1).getBoardItems()) {
                        result = boardItem;
                        break;
                    }
                }
                break;
            case 'r':
                if(getXPos() + 1 < board.getBoard().size()) {
                    for(BoardItem boardItem : board.getBoard().get(getYPos()).get(getXPos() + 1).getBoardItems()) {
                        result = boardItem;
                        break;
                    }
                }
                break;
        }
        board.getStatistics().addScan();

        return result;
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
            if(Agent.isFoundUsefulBeacon()) {
                if(boardItem instanceof Pit || !canFront() || Agent.getCurrStepToBeacon() > Agent.getStepsToBeacon()) {
                    Agent.setIsBacktracking(true);
                    Agent.setCurrStepToBeacon(0);
                }
                // optional: backtrack in specific number of steps (value returned by the beacon)
                if(!canFront() && Agent.getFronts().size() == 0) {
                    rotate();
                    rotate();
                } else if(Agent.isBacktracking()) backtrack();
                else {
                    Agent.setCurrStepToBeacon(Agent.getCurrStepToBeacon() + 1);
                    front();
                    Agent.addFront(direction);
                }
            } else if(boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon) {
                if(boardItem instanceof Beacon) {
                    if(!((Beacon) boardItem).isActivated()) Agent.setScannedItem(boardItem);
                    else doNormalRoutine();
                } else if(boardItem instanceof Pit) {
                    if(Agent.getCurrRotate() == 0)  Agent.setScannedItem(boardItem);
                    else doNormalRoutine();
                } else Agent.setScannedItem(boardItem);
            } else doNormalRoutine();
        } else {
            if(Agent.getScannedItem() instanceof GoldPot) front();
            else if(Agent.getScannedItem() instanceof Pit) goOverThePit();
            else if(Agent.getScannedItem() instanceof Beacon) {
                boolean canGetStepsToGoldPot = false;
                switch (direction) {
                    case 'u':
                    case 'd':
                        if(Math.abs(Agent.getScannedItem().getYPos() - getYPos()) > 0) front();
                        else canGetStepsToGoldPot = true;
                        break;
                    case 'l':
                    case 'r':
                        if(Math.abs(Agent.getScannedItem().getXPos() - getXPos()) > 0) front();
                        else canGetStepsToGoldPot = true;
                        break;
                }
                // limits steps to check only upto the number of space the useful beacon is from the gold pot
                if(canGetStepsToGoldPot) {
                    if(((Beacon) Agent.getScannedItem()).getStepsToGoldPot() != -1) {
                        Agent.getFronts().clear();
                        Agent.setStepsToBeacon(((Beacon) Agent.getScannedItem()).getStepsToGoldPot());
                        Agent.setFoundUsefulBeacon(true);
                    }
                    ((Beacon) Agent.getScannedItem()).setActivated(true);
                    Agent.setScannedItem(null);
                }
            }
        }
    }

    private void faceUp() {
        switch (direction) {
            case 'r':
                rotate();
            case 'd':
                rotate();
            case 'l':
                rotate();
        }
    }

    private void faceLeft() {
        switch (direction) {
            case 'u':
                rotate();
            case 'r':
                rotate();
            case 'd':
                rotate();
        }
    }

    private void faceRight() {
        switch (direction) {
            case 'd':
                rotate();
            case 'l':
                rotate();
            case 'u':
                rotate();
        }
    }

    private void faceDown() {
        switch (direction) {
            case 'l':
                rotate();
            case 'u':
                rotate();
            case 'r':
                rotate();
        }
    }

    private void doNormalRoutine() {
        BoardItem boardItem = null;
        if(Agent.getCurrRotate() == 4) {
            Agent.setCurrRotate(0);
            if(!canFront()) {
                switch (direction) {
                    case 'r':
                        faceUp();
                        if(!canFront()) Agent.setGoingDown(true);
                        faceDown();
                        if(!canFront()) Agent.setGoingDown(false);
                        faceRight();
                        break;
                    case 'l':
                        faceUp();
                        if(!canFront()) Agent.setGoingDown(true);
                        faceDown();
                        if(!canFront()) Agent.setGoingDown(false);
                        faceLeft();
                        break;
                }
                switch(direction) {
                    case 'r':
                        // goes up or down depending on Agent.isGoingDown flag
                        if(Agent.isGoingDown()) faceDown();
                        else faceUp();

                        boardItem = scan();
                        if(boardItem instanceof GoldPot || boardItem instanceof Pit) Agent.setScannedItem(boardItem);
                        else if(boardItem instanceof Beacon) {
                            if(((Beacon) boardItem).isActivated()) front();
                            else Agent.setScannedItem(boardItem);
                        } else front();

                        boardItem = scan();
                        if(boardItem instanceof GoldPot || boardItem instanceof Pit) Agent.setScannedItem(boardItem);
                        else if(boardItem instanceof Beacon) {
                            if(((Beacon) boardItem).isActivated()) front();
                            else Agent.setScannedItem(boardItem);
                        } else front();

                        faceLeft();
                        break;
                    case 'l':
                        // goes up or down depending on Agent.isGoingDown flag
                        if(Agent.isGoingDown()) faceDown();
                        else faceUp();

                        boardItem = scan();
                        if(boardItem instanceof GoldPot || boardItem instanceof Pit) Agent.setScannedItem(boardItem);
                        else if(boardItem instanceof Beacon) {
                            if(((Beacon) boardItem).isActivated()) front();
                            else Agent.setScannedItem(boardItem);
                        } else front();

                        boardItem = scan();
                        if(boardItem instanceof GoldPot || boardItem instanceof Pit) Agent.setScannedItem(boardItem);
                        else if(boardItem instanceof Beacon) {
                            if(((Beacon) boardItem).isActivated()) front();
                            else Agent.setScannedItem(boardItem);
                        } else front();

                        boardItem = scan();
                        if(boardItem instanceof GoldPot || boardItem instanceof Pit) Agent.setScannedItem(boardItem);
                        else if(boardItem instanceof Beacon) {
                            if(((Beacon) boardItem).isActivated()) front();
                            else Agent.setScannedItem(boardItem);
                        } else front();
                        faceRight();
                        break;
                    case 'u':
                        faceRight();
                        if(!canFront()) faceLeft();
                    case 'd':
                        faceLeft();
                        if(!canFront()) faceRight();
                        break;
                }
            } else {
                boardItem = scan();
                if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                    Agent.setScannedItem(boardItem);
                    return;
                } else front();
            }
        } else {
            Agent.setCurrRotate(Agent.getCurrRotate() + 1);
            rotate();
        }
    }

    private void backtrack() {
        if(Agent.getFronts().size() > 0) {
            boolean canFront = false;
            int lastIndex = Agent.getFronts().size() - 1;
            // go to opposite direction
            switch(Agent.getFronts().get(lastIndex)) {
                case 'u':
                    if(direction != 'd') rotate();
                    else canFront = true;
                    break;
                case 'd':
                    if(direction != 'u') rotate();
                    else canFront = true;
                    break;
                case 'l':
                    if(direction != 'r') rotate();
                    else canFront = true;
                    break;
                case 'r':
                    if(direction != 'l') rotate();
                    else canFront = true;
                    break;
            }
            if(canFront) {
                front();
                Agent.getFronts().remove(lastIndex);
            }
        } else {
            rotate();
            Agent.setIsBacktracking(false);
        }
    }

    private void goOverThePit() {
        BoardItem boardItem;
        switch (direction) {
            case 'r':
                faceUp();
                boardItem = scan();
                if (canFront() && !(boardItem instanceof Pit)) {
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceRight();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceDown();

                    // move forward until it can pass through the pit
                    do {
                        // scan
                        boardItem = scan();
                        // if it scans a pit
                        if(boardItem instanceof Pit) {
                            // face to usual direction again and move forward
                            faceRight();
                            boardItem = scan();
                            if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                                Agent.setScannedItem(boardItem);
                                return;
                            } else front();
                        }
                        faceDown();
                        boardItem = scan();
                    } while(boardItem instanceof Pit);

                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();
                } else {
                    faceDown();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceRight();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceUp();

                    // move forward until it can pass through the pit
                    do {
                        // scan
                        boardItem = scan();
                        // if it scans a pit
                        if(boardItem instanceof Pit) {
                            // face to usual direction again and move forward
                            faceRight();
                            boardItem = scan();
                            if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                                Agent.setScannedItem(boardItem);
                                return;
                            } else front();
                        }
                        faceUp();
                        boardItem = scan();
                    } while(boardItem instanceof Pit);

                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();
                }
                faceRight();
                break;
            case 'l':
                faceUp();
                boardItem = scan();
                if (canFront() && !(boardItem instanceof Pit)) {
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceLeft();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceDown();

                    System.out.println("Direction left when going over the pit");

                    // move forward until it can pass through the pit
                    do {
                        // scan
                        boardItem = scan();
                        // if it scans a pit
                        if(boardItem instanceof Pit) {
                            // face to usual direction again and move forward
                            faceLeft();
                            boardItem = scan();
                            if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                                Agent.setScannedItem(boardItem);
                                return;
                            } else front();
                        }
                        faceDown();
                        boardItem = scan();
                    } while(boardItem instanceof Pit);

                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();
                } else {
                    faceDown();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceLeft();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceUp();

                    // move forward until it can pass through the pit
                    do {
                        // scan
                        boardItem = scan();
                        // if it scans a pit
                        if(boardItem instanceof Pit) {
                            // face to usual direction again and move forward
                            faceLeft();
                            boardItem = scan();
                            if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                                Agent.setScannedItem(boardItem);
                                return;
                            } else front();
                        }
                        faceUp();
                        boardItem = scan();
                    } while(boardItem instanceof Pit);

                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();
                }
                faceLeft();
                break;
            case 'u':
                faceLeft();
                boardItem = scan();
                if (canFront() && !(boardItem instanceof Pit)) {
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceUp();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceRight();

                    // move forward until it can pass through the pit
                    do {
                        // scan
                        boardItem = scan();
                        // if it scans a pit
                        if(boardItem instanceof Pit) {
                            // face to usual direction again and move forward
                            faceUp();
                            boardItem = scan();
                            if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                                Agent.setScannedItem(boardItem);
                                return;
                            } else front();
                        }
                        faceRight();
                        boardItem = scan();
                    } while(boardItem instanceof Pit);

                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();
                } else {
                    faceRight();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceUp();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceLeft();

                    // move forward until it can pass through the pit
                    do {
                        // scan
                        boardItem = scan();
                        // if it scans a pit
                        if(boardItem instanceof Pit) {
                            // face to usual direction again and move forward
                            faceUp();
                            boardItem = scan();
                            if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                                Agent.setScannedItem(boardItem);
                                return;
                            } else front();
                        }
                        faceLeft();
                        boardItem = scan();
                    } while(boardItem instanceof Pit);

                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();
                }
                faceUp();
                break;
            case 'd':
                faceLeft();
                boardItem = scan();
                if (canFront() && !(boardItem instanceof Pit)) {
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceDown();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceRight();

                    // move forward until it can pass through the pit
                    do {
                        // scan
                        boardItem = scan();
                        // if it scans a pit
                        if(boardItem instanceof Pit) {
                            // face to usual direction again and move forward
                            faceDown();
                            boardItem = scan();
                            if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                                Agent.setScannedItem(boardItem);
                                return;
                            } else front();
                        }
                        faceRight();
                        boardItem = scan();
                    } while(boardItem instanceof Pit);

                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();
                } else {
                    faceRight();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceDown();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    boardItem = scan();
                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();

                    faceLeft();

                    // move forward until it can pass through the pit
                    do {
                        // scan
                        boardItem = scan();
                        // if it scans a pit
                        if(boardItem instanceof Pit) {
                            // face to usual direction again and move forward
                            faceDown();
                            boardItem = scan();
                            if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                                Agent.setScannedItem(boardItem);
                                return;
                            } else front();
                        }
                        faceLeft();
                        boardItem = scan();
                    } while(boardItem instanceof Pit);

                    if (boardItem instanceof GoldPot || boardItem instanceof Pit || boardItem instanceof Beacon || !canFront()) {
                        Agent.setScannedItem(boardItem);
                        return;
                    } else front();
                }
                faceDown();
                break;
        }
        Agent.setScannedItem(null);
    }

    public char getDirection() {
        return direction;
    }
}
