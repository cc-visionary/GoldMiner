package main.gameScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import main.gameOver.EndGameController;
import main.model.Board;
import main.model.BoardSpace;
import main.model.boardItems.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {
    @FXML
    private Button skipButton;

    @FXML
    private CheckBox autoskipCheckbox;

    @FXML
    private GridPane board;

    @FXML
    private AnchorPane ap;

    private Board gameBoard;

    public GameScreenController(int n, char choice) {
        gameBoard = new Board(n);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateBoard();
    }

    /**
     * Refreshes the Board and reassigns the values from the Game Board
     */
    public void refresh() {
        Node node = board.getChildren().get(0); // to maintain grid line
        board.getChildren().clear();            // removes all entities
        board.getChildren().add(0,node);  // restore the grid line
        for(ArrayList<BoardSpace> row : gameBoard.getBoard()) {
            for (BoardSpace column : row) {
                for (BoardItem boardItem : column.getBoardItems()) {
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(45);
                    imageView.setFitWidth(45);
                    if (boardItem instanceof Beacon) imageView.setImage(new Image("/images/sprites/beacon.png"));
                    else if (boardItem instanceof Pit) imageView.setImage(new Image("/images/sprites/pit hole.png"));
                    else if (boardItem instanceof GoldPot)
                        imageView.setImage(new Image("/images/sprites/gold pot.png"));
                    else if (boardItem instanceof Miner) {
                        imageView.setImage(new Image("/images/sprites/miner.png"));
                        switch (((Miner) boardItem).getDirection()) {
                            case 'u':
                                imageView.setRotate(-90);
                                break;
                            case 'd':
                                imageView.setRotate(90);
                                break;
                            case 'l':
                                imageView.setRotate(180);
                                break;
                        }
                    }
                    board.add(imageView, column.getRow(), column.getColumn());
                }
            }
        }
        board.setGridLinesVisible(true);

        if(gameBoard.getMiner().didFallOnPit()) {
            // show game over screen
            endGame("Game Over. Miner Failed...");
        } else if(gameBoard.getMiner().didReachGoldPot()) {
            endGame("Miner has reached the Gold Pot!");
        }
    }

    /**
     * Generates the board into the Game Screen
     * through looping the 2-dimensional ArrayList of the Board
     */
    public void generateBoard() {
        for(ArrayList<BoardSpace> row : gameBoard.getBoard()) {
            RowConstraints rowConstraints = new RowConstraints(50);
            ColumnConstraints columnConstraints = new ColumnConstraints(50);
            rowConstraints.setValignment(VPos.CENTER);
            columnConstraints.setHalignment(HPos.CENTER);
            board.getRowConstraints().add(rowConstraints);
            board.getColumnConstraints().add(columnConstraints);
            for(BoardSpace column : row) {
                for(BoardItem boardItem : column.getBoardItems()) {
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(45);
                    imageView.setFitWidth(45);

                    if (boardItem instanceof Beacon) imageView.setImage(new Image("/images/sprites/beacon.png"));
                    else if (boardItem instanceof Pit) imageView.setImage(new Image("/images/sprites/pit hole.png"));
                    else if (boardItem instanceof GoldPot) imageView.setImage(new Image("/images/sprites/gold pot.png"));
                    else if (boardItem instanceof Miner) imageView.setImage(new Image("/images/sprites/miner.png"));

                    board.add(imageView, column.getRow(), column.getColumn());
                }
            }
        }
    }

    public void randomMove() {
        Random randomizer = new Random();
        switch(randomizer.nextInt(3) + 1) {
            case 1:
                front();
                break;
            case 2:
                scan();
                break;
            case 3:
                rotate();
                break;
        }
    }

    @FXML
    public void nextMove(ActionEvent ae) {
        randomMove();
    }

    @FXML
    public void scan() {
        BoardItem scanned = gameBoard.getMiner().scan();
        if(scanned instanceof Pit) System.out.println("Pit Ahead!");
        else if(scanned instanceof Beacon) System.out.println("Beacon Ahead! (" + ((Beacon) scanned).getStepsToGoldPot() + " steps away from the gold pot)");
        else if(scanned instanceof GoldPot) System.out.println("Gold Pot Ahead!");
        else System.out.println("Scanned nothing...");
    }

    @FXML
    public void rotate() {
        gameBoard.getMiner().rotate();
        refresh();
    }

    @FXML
    public void front() {
        gameBoard.getMiner().front();
        refresh();
    }

    public void endGame(String text) {
        try {
            FXMLLoader endGameLoader = new FXMLLoader(getClass().getResource("/main/gameOver/EndGame.fxml"));
            EndGameController endGameController = new EndGameController(text, gameBoard.getStatistics());
            endGameLoader.setController(endGameController);

            Stage stage = (Stage) ap.getScene().getWindow();
            stage.setScene(new Scene(endGameLoader.load()));
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}