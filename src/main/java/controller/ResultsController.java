package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import results.RankingsManager;

import java.io.IOException;

public class ResultsController {

    @FXML
    Label winner;

    private final Logger logger = LoggerFactory.getLogger(ResultsController.class);

    public void setResults(String winnerNickname, String loserNickname) {
        this.winner.setText(winnerNickname);
        saveResults(winnerNickname, loserNickname);
    }

    private void saveResults(String winnerNickname, String loserNickname) {
        logger.info("Saving results to database");
        RankingsManager rankingsManager = RankingsManager.getInstance();
        rankingsManager.updatePlayer(winnerNickname, true);
        rankingsManager.updatePlayer(loserNickname, false);
        rankingsManager.updateRankings();
    }

    @FXML
    public void newGameClicked(MouseEvent mouseEvent) {
        logger.info("Clicked on New Game button");
        navigateTo(mouseEvent, "/fxml/launch.fxml");
    }

    @FXML
    public void showRankingsClicked(MouseEvent mouseEvent) {
        logger.info("Clicked on Rankings button");
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
            logger.error(e.getMessage());
        }
    }

    @FXML
    void onQuit() {
        logger.info("Clicked on Quit button");
        Platform.exit();
    }
}
