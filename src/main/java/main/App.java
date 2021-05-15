package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ranks.RankingsManager;

public class App extends Application {
    private final Logger logger = LoggerFactory.getLogger(BoardManager.class);

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Application launching...");
        Parent root = FXMLLoader.load(App.class.getResource("/fxml/launch.fxml"));
        stage.setTitle("SWE");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();

        RankingsManager.getInstance().createTable();
    }
}