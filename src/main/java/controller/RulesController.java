package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.BoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesController {

    private final Logger logger = LoggerFactory.getLogger(RulesController.class);

    @FXML
    public void hideRules(MouseEvent mouseEvent) {
        logger.info("Exiting from Rules scene");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
