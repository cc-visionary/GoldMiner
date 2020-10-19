package main.gameScreen;

public class GameScreenController {
    public GameScreenController(int n, char choice) {
        System.out.println(String.format("%d - %c", n, choice));
        generateBoard(n);
    }

    /**
     * Generates the board into the Game Screen
     * @param n determines the size of the board as n (width) x n (height) board
     */
    public void generateBoard(int n) {

    }
}
