package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LaunchController {

    @FXML
    public void startGame(MouseEvent mouseEvent) throws IOException {
        navigateTo(mouseEvent, "/fxml/game.fxml");
    }

    @FXML
    public void showRules(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rules.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 500, 500));
        stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    @FXML
    public void showRankings(MouseEvent mouseEvent) throws IOException {
        navigateTo(mouseEvent, "/fxml/rankings.fxml");
    }

    private void navigateTo(MouseEvent mouseEvent, String filename) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }
}
