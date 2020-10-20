package main.model.boardItems;

import main.model.Board;

final public class Miner extends BoardItem {
    private char direction;

    public Miner(char direction, Board board) {
        super(board);
        this.direction = direction;
    }

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


}
