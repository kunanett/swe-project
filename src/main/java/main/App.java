package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ranks.RankingsManager;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(App.class.getResource("/fxml/launch.fxml"));
        stage.setTitle("SWE");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();

        RankingsManager.createTable();
    }
}