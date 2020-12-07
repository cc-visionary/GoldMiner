package main.gameScreen;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import main.endGame.EndGameController;
import main.model.Agent;
import main.model.Board;
import main.model.BoardSpace;
import main.model.boardItems.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {
    @FXML
    private Button scanButton, frontButton, rotateButton, nextButton;

    @FXML
    private CheckBox autoSkipCheckbox;

    @FXML
    private GridPane grid;

    @FXML
    private AnchorPane ap;

    private char choice;
    private Board board;
    private Timeline timeline;

    public GameScreenController(Board board, char choice) {
        this.choice = choice;
        this.board = board;
        this.board.verifyBeacons();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateBoard();
    }

    /**
     * Refreshes the Board and reassigns the values from the Game Board
     */
    public void refresh() {
        Node node = grid.getChildren().get(0); // to maintain grid line
        grid.getChildren().clear();            // removes all entities
        grid.getChildren().add(0,node);  // restore the grid line
        for(ArrayList<BoardSpace> row : board.getBoard()) {
            for (BoardSpace column : row) {
                for (BoardItem boardItem : column.getBoardItems()) {
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(45);
                    imageView.setFitWidth(45);
                    if (boardItem instanceof Beacon) imageView.setImage(new Image("/images/sprites/beacon.png"));
                    else if (boardItem instanceof Pit) imageView.setImage(new Image("/images/sprites/pit hole.png"));
                    else if (boardItem instanceof GoldPot) imageView.setImage(new Image("/images/sprites/gold pot.png"));
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
                    grid.add(imageView, column.getRow(), column.getColumn());
                }
            }
        }
        grid.setGridLinesVisible(true);

        if(board.getMiner().didFallOnPit()) {
            // show game over screen
            endGame("Game Over. Miner Failed...");
        } else if(board.getMiner().didReachGoldPot()) {
            endGame("Miner has reached the Gold Pot!");
        }
    }

    /**
     * Generates the board into the Game Screen
     * through looping the 2-dimensional ArrayList of the Board
     */
    public void generateBoard() {
        for(ArrayList<BoardSpace> row : board.getBoard()) {
            RowConstraints rowConstraints = new RowConstraints(50);
            ColumnConstraints columnConstraints = new ColumnConstraints(50);
            rowConstraints.setValignment(VPos.CENTER);
            columnConstraints.setHalignment(HPos.CENTER);
            grid.getRowConstraints().add(rowConstraints);
            grid.getColumnConstraints().add(columnConstraints);
            for(BoardSpace column : row) {
                for(BoardItem boardItem : column.getBoardItems()) {
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(45);
                    imageView.setFitWidth(45);

                    if (boardItem instanceof Beacon) imageView.setImage(new Image("/images/sprites/beacon.png"));
                    else if (boardItem instanceof Pit) imageView.setImage(new Image("/images/sprites/pit hole.png"));
                    else if (boardItem instanceof GoldPot) imageView.setImage(new Image("/images/sprites/gold pot.png"));
                    else if (boardItem instanceof Miner) imageView.setImage(new Image("/images/sprites/miner.png"));

                    grid.add(imageView, column.getRow(), column.getColumn());
                }
            }
        }
    }

    @FXML
    public void autoSkip() {
        if(autoSkipCheckbox.isSelected()) {
            scanButton.setDisable(true);
            frontButton.setDisable(true);
            rotateButton.setDisable(true);
            nextButton.setDisable(true);
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> nextMove()));
            timeline.setCycleCount(Integer.MAX_VALUE);
            timeline.play();
        } else {
            scanButton.setDisable(false);
            frontButton.setDisable(false);
            rotateButton.setDisable(false);
            nextButton.setDisable(false);
            if(timeline != null) {
                timeline.stop();
                timeline = null;
            }
        }
    }

    @FXML
    public void nextMove() {
        switch (choice) {
            case 'R':
                board.getMiner().randomMove();
                break;
            case 'S':
                board.getMiner().smartMove();
                break;
        }
        refresh();
    }

    @FXML
    public void scan() {
        BoardItem scanned = board.getMiner().scan();
        if(scanned instanceof Pit) System.out.println("Pit Ahead!");
        else if(scanned instanceof Beacon) System.out.println("Beacon Ahead! (" + ((Beacon) scanned).getStepsToGoldPot() + " steps away from the gold pot)");
        else if(scanned instanceof GoldPot) System.out.println("Gold Pot Ahead!");
        else System.out.println("Scanned nothing...");
    }

    @FXML
    public void rotate() {
        board.getMiner().rotate();
        refresh();
    }

    @FXML
    public void front() {
        board.getMiner().front();
        refresh();
    }

    public void endGame(String text) {
        if(timeline != null) {
            timeline.stop();
            timeline = null;
        }
        Agent.resetValues();

        try {
            FXMLLoader endGameLoader = new FXMLLoader(getClass().getResource("/main/endGame/EndGame.fxml"));
            EndGameController endGameController = new EndGameController(text, board.getStatistics());
            endGameLoader.setController(endGameController);

            Stage stage = (Stage) ap.getScene().getWindow();
            stage.setScene(new Scene(endGameLoader.load()));
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}