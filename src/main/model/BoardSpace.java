package main.model;

import main.model.boardItems.BoardItem;

import java.util.ArrayList;

public class BoardSpace {
    private ArrayList<BoardItem> boardItems;
    private int row, column;

    public BoardSpace(BoardItem boardItem, int row, int column) {
        this.boardItems = new ArrayList<BoardItem>();
        this.boardItems.add(boardItem);
        this.row = row;
        this.column = column;
    }

    public BoardSpace(int row, int column) {
        this.boardItems = new ArrayList<BoardItem>();
        this.row = row;
        this.column = column;
    }

    public ArrayList<BoardItem> getBoardItems() {
        return boardItems;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void addBoardItem(BoardItem boardItem) {
        this.boardItems.add(boardItem);
    }

    public void removeBoardItem(BoardItem boardItem) {
        this.boardItems.remove(boardItem);
    }
}
