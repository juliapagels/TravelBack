import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Created by Julia Pagels
 */
public class OutputBox {

    /**
     *generates a new window, which shows the html-code needed for the webmap
     * @param text
     */
    public void display(String text) {
        Stage window = new Stage();
        TextArea tfOut;

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("HTML-Code Output");
        window.setMinWidth(250);

        Label output = new Label();
        output.setText("If you copy this code and add it to your html-code of your website the map will show up.");

        //Output
        tfOut = new TextArea(text);
        tfOut.setWrapText(true);
        tfOut.setEditable(false);

        //Copy-Button
        String copyText = tfOut.getText();
        Button copyButton = new Button("Copy code");
        copyButton.setPrefSize(100, 20);
        copyButton.setAlignment(Pos.CENTER);
        copyButton.setOnAction(e -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(copyText);
            clipboard.setContent(content);
        });

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

        vBox.getChildren().addAll(output, tfOut);
        hBox.getChildren().addAll(copyButton, closeButton);

        Scene scene = new Scene(border);
        scene.getStylesheets().add("css-stylesheet.css");
        window.setScene(scene);
        window.showAndWait();
    }

}
