package main.model;

import main.model.boardItems.BoardItem;

public class BoardSpace {
    private BoardItem boardItem;
    private int row, column;

    public BoardSpace(BoardItem boardItem, int row, int column) {
        this.boardItem = boardItem;
        this.row = row;
        this.column = column;
    }

    public BoardSpace(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public BoardItem getBoardItem() {
        return boardItem;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setBoardItem(BoardItem boardItem) {
        this.boardItem = boardItem;
    }

    public void removeBoardItem() {
        setBoardItem(null);
    }
}
