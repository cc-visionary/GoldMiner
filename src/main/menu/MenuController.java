package main.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import main.editBoard.EditBoardController;
import main.gameScreen.GameScreenController;
import main.model.Board;

import java.io.IOException;

public class MenuController {
    @FXML
    private Spinner beaconSpinner, pitSpinner, sizeSpinner;

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
        Parent root = generateEditBoard(choice).load();

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
    }

    private FXMLLoader generateEditBoard(char choice) {
        FXMLLoader editBoardLoader = new FXMLLoader(getClass().getResource("/main/editBoard/EditBoard.fxml"));
        editBoardLoader.setController(new EditBoardController((int) beaconSpinner.getValue(), (int) pitSpinner.getValue(), (int) sizeSpinner.getValue(), choice));

        return editBoardLoader;
    }
}
