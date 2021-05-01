package controller;

import gameLogic.BoardManager;
import gameLogic.Field;
import gameLogic.GameState;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

//CHECKSTYLE:OFF
public class GameController {

    @FXML
    public javafx.scene.control.Label player1;

    @FXML
    public Label player2;

    @FXML
    GridPane board;

    private BoardManager game;

    public void setPlayerNames(String player1, String player2) {
        this.player1.setText(player1);
        this.player2.setText(player2);
    }

    public void initialize() {
        game = new BoardManager();

        this.player1.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.player2.setPrefWidth(Region.USE_COMPUTED_SIZE);

        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var field = new StackPane();
                field.setOnMouseClicked(this::handleMouseClick);
                field.setId("field");
                board.add(field, j, i);
            }
        }
        refreshBoard();
    }

    @FXML
    public void handleMouseClick(MouseEvent mouseEvent) {
        var field = (StackPane) mouseEvent.getSource();
        var row = GridPane.getRowIndex(field);
        var col = GridPane.getColumnIndex(field);
        game.movePiece(row, col);
        refreshBoard();
        checkGameOver(mouseEvent);
    }

    public void checkGameOver(MouseEvent event) {
        GameState state = game.getGameState();
        if (!state.equals(GameState.RUNNING)) {
            String winner;
            String loser;
            if (state.equals(GameState.PLAYER1_WON)) {
                winner = player1.getText();
                loser = player2.getText();
            } else {
                winner = player2.getText();
                loser = player1.getText();
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/results.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
                fxmlLoader.<ResultsController>getController().setWinner(winner, loser);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshBoard() {
        Field[][] boardRepresentation = game.getBoard();
        ObservableList<Node> fields = board.getChildren();
        for (var field : fields) {
            int row = GridPane.getRowIndex(field);
            int col = GridPane.getColumnIndex(field);
            String backgroundColor = switch (boardRepresentation[row][col]) {
                case UNAVAILABLE -> """
                        -fx-background-color: #5F6366;
                        """;
                case PLAYER1 -> """
                        -fx-background-color: #EDB5BF;
                        """;
                case PLAYER2 -> """
                        -fx-background-color: #99CED3;
                        """;
                case EMPTY -> """
                        -fx-background-color: transparent;
                        """;
            };
            field.setStyle(backgroundColor);
        }
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
}
