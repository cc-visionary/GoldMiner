package main.editBoard;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.gameScreen.GameScreenController;
import main.menu.MenuController;
import main.model.Board;
import main.model.BoardSpace;
import main.model.boardItems.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditBoardController implements Initializable {
    @FXML
    private GridPane grid;

    @FXML
    private VBox configurations;

    private Board board;
    private int boardSize, boardItemIndex;
    private char choice;
    private ArrayList<Integer> availableSpaces;

    public EditBoardController(int nBeacons, int nPits, int boardSize, char choice) {
        this.board = new Board(nBeacons, nPits, boardSize);
        this.boardSize = boardSize;
        this.choice = choice;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateBoard();
        setAvailableSpaces();
        generateConfigurations();
    }

    /**
     * Refreshes the Board and reassigns the values from the Game Board
     */
    public void refresh() {
        this.boardItemIndex = 0;
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
                    else if (boardItem instanceof Miner) imageView.setImage(new Image("/images/sprites/miner.png"));

                    grid.add(imageView, column.getRow(), column.getColumn());
                }
            }
        }
        grid.setGridLinesVisible(true);

        setAvailableSpaces();
        configurations.getChildren().clear();
        generateConfigurations();
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

    public void setAvailableSpaces() {
        this.availableSpaces = new ArrayList<Integer>();
        for(int i = 1; i < boardSize * boardSize; i++) this.availableSpaces.add(i);

        for(Beacon beacon : board.getBeacons()) this.availableSpaces.remove((Integer) (beacon.getYPos() * boardSize + beacon.getXPos()));
        for(Pit pit : board.getPits()) this.availableSpaces.remove((Integer) (pit.getYPos() * boardSize + pit.getXPos()));
        this.availableSpaces.remove((Integer) (board.getGoldPot().getYPos() * boardSize + board.getGoldPot().getXPos()));
    }

    public void generateConfigurations() {
        // Beacons Configuration
        Label beaconLabel = new Label("Beacons");
        configurations.getChildren().add(beaconLabel);
        for(int i = 0; i < board.getBeacons().size(); i++, boardItemIndex++) {
            HBox hBox = new HBox(10);
            Label label = new Label("Beacon " + (i + 1) + ":");
            ComboBox comboBox = new ComboBox();
            comboBox.getItems().add(String.format("%d, %d", board.getBeacons().get(i).getXPos() + 1, board.getBeacons().get(i).getYPos() + 1));
            comboBox.getSelectionModel().selectFirst();
            for(int availableSpace : this.availableSpaces) comboBox.getItems().add(String.format("%d, %d", (availableSpace % boardSize) + 1, (availableSpace / boardSize) + 1));
            comboBox.valueProperty().addListener(new ChangeListener() {
                final int index = boardItemIndex;

                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    onValueChange(index, (String) newValue);
                }
            });
            hBox.getChildren().addAll(label, comboBox);
            configurations.getChildren().add(hBox);
        }

        // Pits Configuration
        Label pitsLabel = new Label("Pits");
        configurations.getChildren().add(pitsLabel);
        for(int i = 0; i < board.getPits().size(); i++, boardItemIndex++) {
            HBox hBox = new HBox(10);
            Label label = new Label("Pit " + (i + 1) + ":");
            ComboBox comboBox = new ComboBox();
            comboBox.getItems().add(String.format("%d, %d", board.getPits().get(i).getXPos() + 1, board.getPits().get(i).getYPos() + 1));
            comboBox.getSelectionModel().selectFirst();
            for(int availableSpace : this.availableSpaces) comboBox.getItems().add(String.format("%d, %d", (availableSpace % boardSize) + 1, (availableSpace / boardSize) + 1));
            comboBox.valueProperty().addListener(new ChangeListener() {
                final int index = boardItemIndex;

                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    onValueChange(index, (String) newValue);
                }
            });
            hBox.getChildren().addAll(label, comboBox);
            configurations.getChildren().add(hBox);
        }

        // Gold Pot Configuration
        Label goldPotLabel = new Label("Gold Pot");
        configurations.getChildren().add(goldPotLabel);
        HBox hBox = new HBox(10);
        Label label = new Label("Gold Pot:");
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add(String.format("%d, %d", board.getGoldPot().getXPos() + 1, board.getGoldPot().getYPos() + 1));
        comboBox.getSelectionModel().selectFirst();
        for(int availableSpace : this.availableSpaces) comboBox.getItems().add(String.format("%d, %d", (availableSpace % boardSize) + 1, (availableSpace / boardSize) + 1));
        comboBox.valueProperty().addListener(new ChangeListener() {
            final int index = boardItemIndex + 1;

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                onValueChange(index, (String) newValue);
            }
        });
        hBox.getChildren().addAll(label, comboBox);
        configurations.getChildren().add(hBox);
    }

    /**
     * Handles the value change on the Board Items Configuration
     * @param index
     * @param newValue
     */
    public void onValueChange(int index, String newValue) { 
        int newXPos = Integer.parseInt(newValue.split(", ")[0]) - 1;
        int newYPos = Integer.parseInt(newValue.split(", ")[1]) - 1;

        if(index < board.getBeacons().size()) board.moveBoardItem(board.getBeacons().get(index), newXPos, newYPos, true);
        else if(index < board.getBeacons().size() + board.getPits().size()) board.moveBoardItem(board.getPits().get(index - board.getPits().size()), newXPos, newYPos, true);
        else board.moveBoardItem(board.getGoldPot(), newXPos, newYPos, true);

        refresh();
    }

    @FXML
    public void onCancel(ActionEvent ae) {
        try {
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/main/menu/Menu.fxml"));
            // set menu loader's controller to MenuController
            menuLoader.setController(new MenuController());

            Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
            stage.setScene(new Scene(menuLoader.load()));
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void onContinue(ActionEvent ae) {
        try {
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getResource("/main/gameScreen/GameScreen.fxml"));
            GameScreenController gameScreenController = new GameScreenController(this.board, this.choice);
            gameScreenLoader.setController(gameScreenController);

            Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
            stage.setScene(new Scene(gameScreenLoader.load()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
