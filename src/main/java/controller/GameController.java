package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class GameController {

    @FXML
    public javafx.scene.control.Label player1;

    @FXML
    public Label player2;

    @FXML
    GridPane board;

    public void setPlayer1(String nickname){
        this.player1.setText(nickname);
    }

    public void setPlayer2(String nickname){
        this.player2.setText(nickname);
    }

    public void initialize(){
        this.player1.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.player2.setPrefWidth(Region.USE_COMPUTED_SIZE);
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

    /*@FXML
    public void handleMouseClick(MouseEvent mouseEvent) {
    }*/
}
