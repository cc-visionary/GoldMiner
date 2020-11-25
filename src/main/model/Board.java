package main.model;

import main.model.boardItems.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    private ArrayList<ArrayList<BoardSpace>> board;
    private Miner miner;
    private GoldPot goldPot;
    private ArrayList<Beacon> beacons;
    private ArrayList<Pit> pits;
    private Statistics statistics;
    private int nBeacons, nPits;

    public Board(int nBeacons, int nPits, int n) {
        this.statistics = new Statistics();
        this.miner = new Miner(this);
        this.board = new ArrayList<>();
        this.nBeacons = nBeacons;
        this.nPits = nPits;
        this.beacons = new ArrayList<Beacon>();
        this.pits = new ArrayList<Pit>();

        generateBoard(n);
    }

    public ArrayList<ArrayList<BoardSpace>> getBoard() {
        return board;
    }

    /**
     * Generate a n x n Board
     * @param n number of rows and columns in a board
     */
    private void generateBoard(int n) {
        ArrayList<ArrayList<Integer>> itemPositions = generateRandomPositions(n * n);

        for(int r = 0; r < n; r++) {
            this.board.add(new ArrayList<>());
            for(int c = 0; c < n; c++) {
                if(r == 0 && c == 0) this.board.get(r).add(new BoardSpace(this.miner, c, r));
                else if (inList(r * n + c, itemPositions.get(0))) this.board.get(r).add(new BoardSpace(this.goldPot, c, r));
                else if(inList(r * n + c, itemPositions.get(1))) {
                    Beacon beacon = new Beacon(c, r, this.goldPot.getXPos(), this.goldPot.getYPos());
                    this.board.get(r).add(new BoardSpace(beacon, c, r));
                    this.beacons.add(beacon);
                } else if(inList(r * n + c, itemPositions.get(2))) {
                    Pit pit = new Pit(c, r);
                    this.board.get(r).add(new BoardSpace(pit, c, r));
                    this.pits.add(pit);
                }
                else this.board.get(r).add(new BoardSpace(c, r));
            }
        }

        verifyBeacons();
    }

    /**
     * Checks if there are beacons who has a Pit in between of it and the Gold Pot
     * If there is, set its Steps to Gold Pot value to -1
     */
    private void verifyBeacons() {
        // loop the board
        for(int i = 0; i < board.size(); i++) {
            for(int j = 0; j < board.get(i).size(); j++) {
                // for each board space, loop its board items
                for(BoardItem boardItem : board.get(i).get(j).getBoardItems()) {
                    // check if that board item is a beacon
                    if(boardItem instanceof Beacon) {
                        // if it is, then check if it has the same horizontal or vertical position with the Gold Pot
                        Beacon beacon = (Beacon) boardItem;
                        if(beacon.getXPos() == this.goldPot.getXPos()) {
                            // if it is, check if there's a Pit in between
                            int smaller = Math.min(beacon.getYPos(), this.goldPot.getYPos()), bigger = Math.max(beacon.getYPos(), this.goldPot.getYPos());
                            // loop the spaces in between the Beacon and the Gold Pot
                            for(int y = smaller + 1; y < bigger && beacon.getStepsToGoldPot() != -1; y++) {
                                for(BoardItem checkBoardItem : board.get(y).get(beacon.getXPos()).getBoardItems()) {
                                    if(checkBoardItem instanceof Pit) {
                                        // if there is, set the steps to gold pot value to -1
                                        beacon.setStepsToGoldPot(-1);
                                        break;
                                    }
                                }
                            }
                        } else if(beacon.getYPos() == this.goldPot.getYPos()) {
                            // if it is, check if there's a Pit in between
                            int smaller = Math.min(beacon.getXPos(), this.goldPot.getXPos()), bigger = Math.max(beacon.getXPos(), this.goldPot.getXPos());
                            // loop the spaces in between the Beacon and the Gold Pot
                            for(int x = smaller + 1; x < bigger && beacon.getStepsToGoldPot() != -1; x++) {
                                for(BoardItem checkBoardItem : board.get(beacon.getYPos()).get(x).getBoardItems()) {
                                    if(checkBoardItem instanceof Pit) {
                                        // if there is, set the steps to gold pot value to -1
                                        beacon.setStepsToGoldPot(-1);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Generates random number for a unique position of GoldPots, Beacons, and Pits
     * @param upperBound maximum value for a unique position
     * @return 2-dimensional array where in:
     *           - index 0 contains gold pot position
     *           - index 1 contains beacon positions
     *           - index 2 contains pit positions
     */
    private ArrayList<ArrayList<Integer>> generateRandomPositions(int upperBound) {
        final int N = (int) Math.sqrt(upperBound);

        // assert nBeacons + nPits + 2 >= upperBound (to make sure there is space for every item on the board)

        ArrayList<ArrayList<Integer>> storage = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionsTaken = new ArrayList<>();

        // random position for gold pot
        ArrayList<Integer> goldPotPosition = new ArrayList<Integer>();
        goldPotPosition.add(generateUniqueRandomPosition(upperBound, positionsTaken));
        positionsTaken.add(goldPotPosition.get(0)); // to track taken positions;
        this.goldPot = new GoldPot(goldPotPosition.get(0) % N, goldPotPosition.get(0) / N);
        System.out.println(goldPotPosition.get(0) + " " + this.goldPot.getXPos() + " " + this.goldPot.getYPos());

        // random position for beacon
        ArrayList<Integer> beaconPositions = new ArrayList<Integer>();
        for(int i = 0; i < nBeacons; i++) {
            int position = generateUniqueRandomPosition(upperBound, positionsTaken);
            beaconPositions.add(position);
            positionsTaken.add(position);
        }

        // random position for pits
        ArrayList<Integer> pitPositions = new ArrayList<Integer>();
        for(int i = 0; i < nPits; i++) {
            int position = generateUniqueRandomPosition(upperBound, positionsTaken);
            pitPositions.add(position);
            positionsTaken.add(position);
        }

        storage.add(goldPotPosition);
        storage.add(beaconPositions);
        storage.add(pitPositions);

        return storage;
    }

    /**
     * Generates a unique number whose positions isn't taken yet
     * @param upperBound     maximum number
     * @param positionsTaken positions which were already taken
     * @return               unique number which isn't taken yet
     */
    private int generateUniqueRandomPosition(int upperBound, ArrayList<Integer> positionsTaken) {
        Random randomizer = new Random();
        int position;
        do {
            position = randomizer.nextInt(upperBound - 1) + 1;
        } while(inList(position, positionsTaken));
        return position;
    }

    /**
     * Performs a linear search to verify if position is already taken
     * @return if position is taken or not
     */
    private boolean inList(int currentPosition, ArrayList<Integer> positions) {
        for(int position : positions) {
            if(currentPosition == position) return true;
        }
        return false;
    }

    /**
     * Moves an item into xth column, yth row space on a board
     * @param boardItem item to be moved
     * @param xPos      column of item to land
     * @param yPos      row of item to land
     */
    public void moveBoardItem(BoardItem boardItem, int xPos, int yPos, boolean override) {
        if(boardItem.isMovable() || override) {
            board.get(boardItem.getYPos()).get(boardItem.getXPos()).removeBoardItem(boardItem);
            board.get(yPos).get(xPos).addBoardItem(boardItem);
            boardItem.setXPos(xPos);
            boardItem.setYPos(yPos);
        }
    }

    public Miner getMiner() {
        return miner;
    }

    public ArrayList<Beacon> getBeacons() {
        return beacons;
    }

    public ArrayList<Pit> getPits() {
        return pits;
    }

    public GoldPot getGoldPot() {
        return goldPot;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
