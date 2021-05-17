package controller;

import javafx.scene.control.*;
import model.BoardManager;
import model.Field;
import model.GameState;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GameController {

    @FXML
    public javafx.scene.control.Label player1;

    @FXML
    public Label player2;

    @FXML
    GridPane board;

    private BoardManager game;

    @FXML
    Button giveUpButton;

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    @FXML
    public void initialize() {
        logger.info("Initializing board on GUI");
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

    public void setPlayerNames(String player1, String player2) {
        this.player1.setText(player1);
        this.player2.setText(player2);
    }

    @FXML
    private void handleMouseClick(MouseEvent mouseEvent) {
        var field = (StackPane) mouseEvent.getSource();
        var row = GridPane.getRowIndex(field);
        var col = GridPane.getColumnIndex(field);
        logger.info("Click on board, position: ({}, {})", row, col);
        game.movePiece(row, col);
        refreshBoard();
        checkGameOver(mouseEvent);
    }

    @FXML
    public void giveUpPressed(MouseEvent mouseEvent) {
        logger.info("Clicked on give up button");
        game.giveUp();
        gameOver(mouseEvent, game.getGameState());
    }

    private void checkGameOver(MouseEvent event) {
        game.checkIfGameIsOver();
        GameState gameState = game.getGameState();
        if (game.getGameState() != GameState.RUNNING) {
            showGameOverAlert();
            delay();
            gameOver(event, gameState);
        }
    }

    private void showGameOverAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("The game is over!");
        alert.setContentText("Please click OK to continue.");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/pane.css").toExternalForm());
        dialogPane.setId("alert");

        String buttonStyle = """
                                    -fx-background-color: #EDB5BF;
                                    -fx-font-family: 'Courier New', monospace;
                                    -fx-font-weight: bold;
                                    -fx-text-fill: #5F6366;
                                    -fx-background-radius: 1.5em;
                    """;

        ButtonBar buttonBar = (ButtonBar) alert.getDialogPane().lookup(".button-bar");
        buttonBar.getButtons().forEach(button -> button.setStyle(buttonStyle));
        delay();
        alert.showAndWait();
    }

    private void gameOver(MouseEvent event, GameState state) {
        String winner, loser;
        if (state.equals(GameState.PLAYER1_WON)) {
            winner = player1.getText();
            loser = player2.getText();
        } else {
            winner = player2.getText();
            loser = player1.getText();
        }
        goToResults(winner, loser, event);
    }

    private void goToResults(String winner, String loser, MouseEvent event) {
        logger.info("Showing game results");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/results.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
            fxmlLoader.<ResultsController>getController().setResults(winner, loser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshBoard() {
        logger.debug("Refreshing the board on the UI");
        logger.debug(game.toString());

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
                        -fx-background-image: url("/img/pink.png");
                          """;
                case PLAYER2 -> """
                        -fx-background-image: url("/img/blue.png");
                        """;
                case EMPTY -> """
                        -fx-background-color: transparent;
                        """;
            };
            field.setStyle(backgroundColor);
        }

        if (game.getNextPlayer() == BoardManager.NextPlayer.PLAYER1) {
            giveUpButton.setStyle("-fx-background-color: #EDB5BF");
        } else {
            giveUpButton.setStyle("-fx-background-color: #99CED3");
        }
    }

    @FXML
    private void showRules(MouseEvent mouseEvent) throws IOException {
        logger.info("Clicked on Rules button");
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

    private void delay(){
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
