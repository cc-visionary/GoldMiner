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

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {
    @FXML
    private Button skipButton;

    @FXML
    private CheckBox autoskipCheckbox;

    @FXML
    private GridPane board;

    private int n;

    public GameScreenController(int n, char choice) {
        this.n = n;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateBoard(n);
    }

    /**
     * Generates the board into the Game Screen
     * @param n determines the size of the board as n (width) x n (height) board
     */
    public void generateBoard(int n) {
        for(int i = 0; i < n; i++) {
            RowConstraints row = new RowConstraints(50);
            ColumnConstraints column = new ColumnConstraints(50);
            board.getRowConstraints().add(row);
            board.getColumnConstraints().add(column);
            for(int j = 0; j < n; j++) {
                Label label = new Label(" ");
                board.add(label, i, j);
            }
        }
    }

    @FXML
    public void skipMove(ActionEvent ae) {

    }
}