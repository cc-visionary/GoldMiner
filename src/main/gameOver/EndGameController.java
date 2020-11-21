package main.gameOver;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.menu.MenuController;
import main.model.Statistics;

import java.net.URL;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {
    @FXML
    private Label endGameLabel, numScans, numRotates, numFronts;

    private Statistics statistics;
    private String text;

    public EndGameController(String text, Statistics statistics) {
        this.text = text;
        this.statistics = statistics;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        endGameLabel.setText(text);
        numScans.setText(String.format("%d", statistics.getNScan()));
        numRotates.setText(String.format("%d", statistics.getNRotate()));
        numFronts.setText(String.format("%d", statistics.getNFront()));
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

    @FXML
    public void returnToMenu(ActionEvent ae) {
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
}
