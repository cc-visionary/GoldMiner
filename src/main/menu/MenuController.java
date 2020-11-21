package main.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import main.gameScreen.GameScreenController;
import main.model.Board;

import java.io.IOException;

public class MenuController {
    @FXML
    private Spinner spinner;

    @FXML
    public void chooseRandom(ActionEvent ae) throws IOException {
        goToGameScreen(ae, 'R');
    }

    @FXML
    public void chooseSmart(ActionEvent ae) throws IOException {
        goToGameScreen(ae, 'S');
    }

    private void goToGameScreen(ActionEvent ae, char choice) throws IOException {
        Stage primaryStage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
        Parent root = generateGameScreenLoader(choice).load();

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
    }

    private FXMLLoader generateGameScreenLoader(char choice) {
        FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getResource("/main/gameScreen/GameScreen.fxml"));
        gameScreenLoader.setController(new GameScreenController((int) spinner.getValue(), choice));

        return gameScreenLoader;
    }
}
