package main.model;

import main.model.boardItems.BoardItem;

import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<BoardItem>> board;

    public Board(int height, int width) {
        this.board = new ArrayList<>();

        generateBoard(height, width);
    }

    private void generateBoard(int height, int width) {
        for(int h = 0; h < height; h++) {
            this.board.add(new ArrayList<>());
            for(int w = 0; w < width; w++) {
                this.board.get(h).add(null);
            }
        }
    }

    public void moveBoardItem(BoardItem boardItem, int xPos, int yPos) {

    }
}
