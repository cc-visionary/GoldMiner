package main.model;

import main.model.boardItems.BoardItem;

import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<BoardSpace>> board;

    public Board(int n) {
        this.board = new ArrayList<>();

        generateBoard(n);
    }

        public ArrayList<ArrayList<BoardSpace>> getBoard() {
        return board;
    }

    private void generateBoard(int n) {
        for(int h = 0; h < n; h++) {
            this.board.add(new ArrayList<>());
            for(int w = 0; w < n; w++) {
                this.board.get(h).add(new BoardSpace(h, w));
            }
        }
    }

    public void moveBoardItem(BoardItem boardItem, int xPos, int yPos) {

    }
}
