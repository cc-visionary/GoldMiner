package main.model;

import main.model.boardItems.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    private ArrayList<ArrayList<BoardSpace>> board;
    private Miner miner;

    public Board(int n) {
        this.miner = new Miner(this);
        this.board = new ArrayList<>();

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
                else if (inList(r * 10 + c, itemPositions.get(0))) this.board.get(r).add(new BoardSpace(new GoldPot(c, r), c, r));
                else if(inList(r * 10 + c, itemPositions.get(1))) this.board.get(r).add(new BoardSpace(new Beacon(c, r), c, r));
                else if(inList(r * 10 + c, itemPositions.get(2))) this.board.get(r).add(new BoardSpace(new Pit(c, r), c, r));
                else this.board.get(r).add(new BoardSpace(c, r));
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
        final int NBEACON = 5, NPIT = 5;

        // assert NBEACON + NPIT + 2 >= upperBound (to make sure there is space for every item on the board)

        ArrayList<ArrayList<Integer>> storage = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionsTaken = new ArrayList<>();

        // random position for gold pot
        ArrayList<Integer> goldPotPosition = new ArrayList<Integer>();
        goldPotPosition.add(generateUniqueRandomPosition(upperBound, positionsTaken));
        positionsTaken.add(goldPotPosition.get(0)); // to track taken positions;

        // random position for beacon
        ArrayList<Integer> beaconPositions = new ArrayList<Integer>();
        for(int i = 0; i < NBEACON; i++) {
            int position = generateUniqueRandomPosition(upperBound, positionsTaken);
            beaconPositions.add(position);
            positionsTaken.add(position);
        }

        // random position for pits
        ArrayList<Integer> pitPositions = new ArrayList<Integer>();
        for(int i = 0; i < NPIT; i++) {
            int position = generateUniqueRandomPosition(upperBound, positionsTaken);
            pitPositions.add(position);
            positionsTaken.add(position);
        }

        storage.add(goldPotPosition);
        storage.add(beaconPositions);
        storage.add(pitPositions);

        return storage;
    }

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
    public void moveBoardItem(BoardItem boardItem, int xPos, int yPos) {
        if(boardItem.isMovable()) {

        }
    }

    public Miner getMiner() {
        return miner;
    }
}
