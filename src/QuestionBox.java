import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Julia Pagels
 */

public class QuestionBox {

    /**
     * initialized help window with further information about geoJSON
     */
    public void display() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("GeoJSCON");
        window.setMinWidth(250);

        Label helpMessage = new Label();
        helpMessage.setText("If you like to show a track on your map, you can upload a geoJSON file." + System.lineSeparator() +
                "You can create it by using www.openrouteservice.org." + System.lineSeparator() +
                "There you can recreate your track and export it as a geoJSON-file." + System.lineSeparator() +
                "Otherwise you could use a converter to change a track from another filetyp," + System.lineSeparator() + "" +
                "for example www.geojson.io or your desktop GIS-application.");

        //Close Window-Button
        Button closeButton = new Button("Close");
        closeButton.setPrefSize(100, 20);
        closeButton.setAlignment(Pos.CENTER);
        closeButton.setOnAction(e -> window.close());

        //Layout
        BorderPane border = new BorderPane();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(50, 50, 50, 50));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(25, 50, 25, 50));
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        border.setCenter(vBox);
        border.setBottom(hBox);

        vBox.getChildren().addAll(helpMessage);
        hBox.getChildren().addAll(closeButton);

        Scene scene = new Scene(border);
        scene.getStylesheets().add("css-stylesheet.css");
        window.setScene(scene);
        window.showAndWait();
    }

}
