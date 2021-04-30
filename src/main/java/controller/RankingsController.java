package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ranks.Player;
import ranks.RankingsManager;

import java.io.IOException;
import java.util.List;

public class RankingsController {

    @FXML
    private TableView<Player> rankingsTable;

    @FXML
    private TableColumn<Player, Long> rank;

    @FXML
    private TableColumn<Player, String> nickname;

    @FXML
    private TableColumn<Player, Long> points;

    @FXML
    private TableColumn<Player, Long> bestRank;

    public void goBack(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void initialize(){
        refreshTable();
    }

    private void refreshTable(){
        List<Player> players = RankingsManager.getInstance().getRankings();

        rank.setCellFactory(col -> {
            TableCell<Player, Long> indexCell = new TableCell<>();
            ReadOnlyObjectProperty<TableRow<Player>> rowProperty = indexCell.tableRowProperty();
            ObjectBinding<String> rowBinding = Bindings.createObjectBinding(() -> {
                TableRow<Player> row = rowProperty.get();
                if (row != null) {
                    int rowIndex = row.getIndex() + 1;
                    if (rowIndex < row.getTableView().getItems().size() + 1) {
                        return Integer.toString(rowIndex);
                    }
                }
                return null;
            }, rowProperty);
            indexCell.textProperty().bind(rowBinding);
            return indexCell;
        });

        nickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        points.setCellValueFactory(new PropertyValueFactory<>("points"));
        bestRank.setCellValueFactory(new PropertyValueFactory<>("bestRank"));

        ObservableList<Player> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(players);
        rankingsTable.setItems(observableResult);
        rankingsTable.refresh();
    }
}
