package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ranks.RankingsManager;

import java.io.IOException;

public class ResultsController {
    @FXML
    Label winner;

    public void setResults(String winnerNickname, String loserNickname) {
        this.winner.setText(winnerNickname);
        saveResults(winnerNickname, loserNickname);
    }

    private void saveResults(String winnerNickname, String loserNickname) {
        RankingsManager rankingsManager = RankingsManager.getInstance();
        rankingsManager.updatePlayer(winnerNickname, true);
        rankingsManager.updatePlayer(loserNickname, false);
        rankingsManager.updateRankings();
    }

    @FXML
    public void newGameClicked(MouseEvent mouseEvent) {
        navigateTo(mouseEvent, "/fxml/launch.fxml");
    }

    @FXML
    public void showRankingsClicked(MouseEvent mouseEvent) {
        navigateTo(mouseEvent, "/fxml/rankings.fxml");
    }

    private void navigateTo(MouseEvent mouseEvent, String filename) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));
        Parent root;
        try {
            root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
