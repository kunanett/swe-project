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

//CHECKSTYLE:OFF
public class ResultsController {
    @FXML
    Label winner;

    public void setWinner(String winnerNickname, String loserNickname) {
        this.winner.setText(winnerNickname);
        saveWinner(winnerNickname, loserNickname);
    }

    public void saveWinner(String winnerNickname, String loserNickname) {
        RankingsManager rankingsManager = RankingsManager.getInstance();
        rankingsManager.updatePlayer(winnerNickname, true);
        rankingsManager.updatePlayer(loserNickname, false);
        rankingsManager.updateRankings();
    }

    public void newGameClicked(MouseEvent mouseEvent) {
        navigateTo(mouseEvent, "/fxml/launch.fxml");
    }

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
