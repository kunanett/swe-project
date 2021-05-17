package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.BoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import results.RankingsManager;

public class App extends Application {
    private final Logger logger = LoggerFactory.getLogger(BoardManager.class);

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Application launching...");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        stage.setTitle("swe-project");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/pink_icon.png")));
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();

        RankingsManager.getInstance().createTable();
    }
}