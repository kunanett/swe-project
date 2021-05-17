package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LaunchController {

    @FXML
    public javafx.scene.control.TextField player1;

    @FXML
    public javafx.scene.control.TextField player2;

    @FXML
    public Label prompt;

    private final Logger logger = LoggerFactory.getLogger(LaunchController.class);

    public void initialize(){
        logger.info("Initializing Launch scene");
        prompt.setVisible(false);
    }

    public void setPrompt(String message){
        prompt.setText(message);
        prompt.setVisible(true);
        logger.info(message);
    }

    @FXML
    public void startGame(MouseEvent mouseEvent) throws IOException {
        logger.info("Clicked on Start Game Button");
        if (player1.getText().isEmpty() || player2.getText().isEmpty()) {
            setPrompt("Nickname missing!");
        }else if(player1.getText().length() > 10){
            setPrompt("Player 1 nickname is too long!");
        }else if(player2.getText().length() > 10){
            setPrompt("Player 2 nickname is too long!");
        }else if(player2.getText().equals(player1.getText())){
            setPrompt("Nicknames must be different!");
        }else {
            navigateTo(mouseEvent, "/fxml/game.fxml");
        }
    }

    @FXML
    public void showRules(MouseEvent mouseEvent) throws IOException {
        logger.info("Clicked on show Rules button");
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
        logger.info("Clicked on Rankings button");
        navigateTo(mouseEvent, "/fxml/rankings.fxml");
    }

    private void navigateTo(MouseEvent mouseEvent, String filename) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));
        Parent root = fxmlLoader.load();
        if (filename.equals("/fxml/game.fxml")){
            fxmlLoader.<GameController>getController().setPlayers(player1.getText(), player2.getText());
        }
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void onQuit(){
        logger.info("Clicked on Quit button");
        Platform.exit();
    }
}
