package main.gameScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import main.model.Board;
import main.model.BoardSpace;
import main.model.boardItems.Beacon;
import main.model.boardItems.GoldPot;
import main.model.boardItems.Miner;
import main.model.boardItems.Pit;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {
    @FXML
    private Button skipButton;

    @FXML
    private CheckBox autoskipCheckbox;

    @FXML
    private GridPane board;

    private Board gameBoard;

    public GameScreenController(int n, char choice) {
        gameBoard = new Board(n);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateBoard();
    }

    /**
     * Generates the board into the Game Screen
     * through looping the 2-dimensional ArrayList of the Board
     */
    public void generateBoard() {
        for(ArrayList<BoardSpace> row : gameBoard.getBoard()) {
            RowConstraints rowConstraints = new RowConstraints(50);
            ColumnConstraints columnConstraints = new ColumnConstraints(50);
            board.getRowConstraints().add(rowConstraints);
            board.getColumnConstraints().add(columnConstraints);
            for(BoardSpace column : row) {
                if(column.getBoardItem() != null) {
                    Label label = new Label(column.getRow() + " " + column.getColumn());
                    if (column.getBoardItem() instanceof Beacon) label = new Label("B");
                    else if (column.getBoardItem() instanceof Pit) label = new Label("P");
                    else if (column.getBoardItem() instanceof GoldPot) label = new Label("G");
                    else if (column.getBoardItem() instanceof Miner) label = new Label("M");
                    board.add(label, column.getRow(), column.getColumn());
                }
            }
        }
    }

    @FXML
    public void skipMove(ActionEvent ae) {

    }
}