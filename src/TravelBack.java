import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Julia Pagels
 */
public class TravelBack extends Application {

    Stage window;
    ChoiceBox<String> mediatype;
    TextField tfUrl;
    TextField tfTitle;
    TextArea tfDesc;
    TextField tfLat;
    TextField tfLong;
    Button buttonAddMedia;
    Button buttonDone;
    TextArea tfOut;
    String outputtext;
    CheckBox cbGeoJSON;
    TextField tfGeoJSON;
    Button buttonGeoJSON;
    String geoJSONinput;
    LinkedList<Media> mediaList = new LinkedList();
    LinkedList<Hyperlink> points = new LinkedList<>();
    BorderPane border = new BorderPane();
    int index = 0;
    LinkedList<String> hyperlinks = new LinkedList();
    Button buttonSave;
    TextField tfIndex;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Defines everything about the layout of the window
     * includes all input fields
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("TravelBack");
        Scene scene = new Scene(border, 1050, 600);
        scene.getStylesheets().add("css-stylesheet.css");

        //Mediatype label
        Label labelMedia = new Label("Which kind of media do " + System.lineSeparator() +"you want to insert:");
        GridPane.setConstraints(labelMedia, 0, 0);

        //Mediatype Drop-Down-List
        mediatype = new ChoiceBox<>();
        mediatype.getItems().addAll("Picture", "Video", "Text");
        mediatype.setValue("Picture");
        GridPane.setConstraints(mediatype, 1, 0);

        //Listener for mediatype
        mediatype.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue == "Text") {
                tfUrl.setDisable(true);
            } else {
                tfUrl.setDisable(false);
            }
        });

        //URL label
        Label labelUrl = new Label("URL:");
        GridPane.setConstraints(labelUrl, 0, 1);

        //URL input
        tfUrl = new TextField();
        tfUrl.setPromptText("www.geo.uni...");
        GridPane.setConstraints(tfUrl, 1, 1);

        //Title label
        Label labelTitle = new Label("Title:");
        GridPane.setConstraints(labelTitle, 0, 2);

        //Title input
        tfTitle = new TextField();
        tfTitle.setMaxHeight(40);
        tfTitle.setPromptText("f.e. location, date");
        GridPane.setConstraints(tfTitle, 1, 2);

        //Description label
        Label labelDesc = new Label("Description:");
        GridPane.setConstraints(labelDesc, 0, 3);

        //Description input
        tfDesc = new TextArea();
        tfDesc.setWrapText(true);
        tfDesc.setMinHeight(100);
        tfDesc.setMaxHeight(100);
        GridPane.setConstraints(tfDesc, 1, 3);

        //Latitude label
        Label labelLat = new Label("Latitude:");
        GridPane.setConstraints(labelLat, 0, 4);

        //Latitude input
        tfLat = new TextField();
        GridPane.setConstraints(tfLat, 1, 4);

        //Longitude label
        Label labelLong = new Label("Longitude:");
        GridPane.setConstraints(labelLong, 0, 5);

        //Longitude input
        tfLong = new TextField();
        GridPane.setConstraints(tfLong, 1, 5);

        //index of media in medialist, invisible
        tfIndex = new TextField();
        tfIndex.anchorProperty();
        tfIndex.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != "") {
                    buttonSave.setVisible(true);
                    buttonAddMedia.setDisable(true);
                }
                if (newValue == "") {
                    buttonSave.setVisible(false);
                    buttonAddMedia.setDisable(false);
                }
            }
        });

        //button 'Add Media' and it's button event
        buttonAddMedia = new Button();
        buttonAddMedia.setText("Add Media");
        buttonAddMedia.setOnAction(event -> {
            read();
        });
        GridPane.setConstraints(buttonAddMedia, 0, 7);

        //button 'Save Changes' and it's button event
        buttonSave = new Button();
        buttonSave.setText("Save Changes");
        buttonSave.setVisible(false);
        buttonSave.setOnAction(event -> {
            change();
        });
        GridPane.setConstraints(buttonSave, 1, 7);

        //Checkbox GeoJSON-route label
        Label labelCheckbox = new Label("Would you like to include a route?");
        GridPane.setConstraints(labelCheckbox, 1, 0);

        //Checkbox GeoJSON-route
        cbGeoJSON = new CheckBox();
        cbGeoJSON.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue == true) {
                tfGeoJSON.setDisable(false);
                buttonGeoJSON.setDisable(false);
            } else {
                tfGeoJSON.setDisable(true);
                buttonGeoJSON.setDisable(true);
            }
        });
        GridPane.setConstraints(cbGeoJSON, 2, 0);

        //GeoJSON input
        tfGeoJSON = new TextField();
        tfGeoJSON.setText("No file selected.");
        tfGeoJSON.setDisable(true);
        tfGeoJSON.setEditable(false);
        GridPane.setConstraints(tfGeoJSON, 1, 1);

        //Question mark for further information about GeoJSCON
        Button questionmark = new Button();
        questionmark.setText("?");
        questionmark.setId("smallerButton");
        questionmark.setOnAction(e -> {
            QuestionBox questionBox = new QuestionBox();
            questionBox.display();
        });
        GridPane.setConstraints(questionmark, 0, 0);

        //Button Upload file
        buttonGeoJSON = new Button();
        buttonGeoJSON.setText("Select geoJSON file");
        buttonGeoJSON.setId("biggerButton");
        buttonGeoJSON.setDisable(true);
        GridPane.setConstraints(buttonGeoJSON, 2, 1);
        buttonGeoJSON.setOnAction(event -> {
            loadFile();
        });

        //button 'Done' and it's button event
        buttonDone = new Button();
        buttonDone.setText("Done");
        buttonDone.setDisable(true);
        buttonDone.setOnAction(event -> {
            print();
            OutputBox box = new OutputBox();
            box.display(outputtext);
        });

        GridPane.setConstraints(buttonDone, 1, 8);

        //Output
        tfOut = new TextArea();

        //Layout
        GridPane main = new GridPane();

        GridPane input = new GridPane();
        input.setPadding(new Insets(10, 20, 10, 20));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(200);
        input.setVgap(8);
        input.setHgap(50);
        GridPane.setConstraints(input, 0, 0);

        HBox hBoxSaveAdd = new HBox();
        hBoxSaveAdd.setPadding(new Insets(25, 20, 25, 20));
        hBoxSaveAdd.setSpacing(20);
        hBoxSaveAdd.setAlignment(Pos.BOTTOM_RIGHT);
        GridPane.setConstraints(hBoxSaveAdd, 0, 1);

        GridPane route = new GridPane();
        route.setPadding(new Insets(25, 20, 10, 20));
        route.setVgap(8);
        route.setHgap(20);
        GridPane.setConstraints(route, 0, 2);

        HBox hBoxDone = new HBox();
        hBoxDone.setPadding(new Insets(25, 20, 25, 20));
        hBoxDone.setSpacing(20);
        hBoxDone.setAlignment(Pos.CENTER_RIGHT);
        GridPane.setConstraints(hBoxDone, 0, 5);

        border.setCenter(main);
        border.setLeft(vBoxLeft());

        //add everything to grid
        hBoxDone.getChildren().addAll(buttonDone);
        hBoxSaveAdd.getChildren().addAll(buttonAddMedia, buttonSave);
        route.getChildren().addAll(questionmark, labelCheckbox, cbGeoJSON, tfGeoJSON, buttonGeoJSON);
        input.getChildren().addAll(labelMedia, mediatype, labelUrl, tfUrl, labelTitle, tfTitle, labelDesc, tfDesc, labelLat, tfLat, labelLong, tfLong);
        main.getChildren().addAll(input, hBoxSaveAdd, route, hBoxDone);


        window.setScene(scene);
        window.show();
    }

    /**
     * creates a dynamic menu with all saved mediaobjects
     *
     * @return vBox
     */
    public VBox vBoxLeft() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setMinWidth(300);
        vbox.setMaxWidth(300);
        vbox.setId("leftmenu");

        //Label Left
        Label labelLeft = new Label("Saved Mediaobjects");
        vbox.getChildren().add(labelLeft);

        final Hyperlink[] hpls = new Hyperlink[hyperlinks.size()];
        for (int i = 0; i < hyperlinks.size(); i++) {
            final Hyperlink hpl = hpls[i] = new Hyperlink(hyperlinks.get(i));
            int y = i;
            hpl.setOnAction(event -> loadMedia(y));
        }


        for (int i = 0; i < hpls.length; i++) {
            VBox.setMargin(hpls[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(hpls[i]);
        }
        return vbox;
    }

    /**
     * add's the filled out data in a new Object from class Media
     *
     * @return new Object from class Media
     */
    public Media add() {
        Media med = new Media();
        if (tfUrl.getText().equals("")) {
            med.url = ("");
        } else {
            med.url = tfUrl.getText();
        }
        if (tfDesc.getText().equals("")) {
            med.description = ("");
        } else {
            med.description = tfDesc.getText();
        }
        med.mediat = mediatype.getSelectionModel().selectedItemProperty().getValue();
        med.title = tfTitle.getText();
        med.longitude = Double.parseDouble(tfLong.getText());
        med.latitude = Double.parseDouble(tfLat.getText());

        return med;

    }

    /**
     * validates if the fields Longitude and Latitude are filled out
     * changes color of textfields, if they are filled out wrong
     *
     * @return boolean
     */
    public Boolean validation() {

        Boolean check = true;

        if (tfTitle.getText()==""){
            tfTitle.setId("text-field-red");
            tfTitle.setText("");
            tfTitle.setPromptText("Enter a Title");
            check = false;
        } else {
            tfTitle.setId("text-field-empty");
        }

        try {
            Double.parseDouble(tfLat.getText());
            tfLat.setId("text-field-empty");
        }
        catch (Exception ex){
            tfLat.setId("text-field-red");
            tfLat.setText("");
            tfLat.setPromptText("Enter Latitude as number");
            check = false;
        }
        try {
            Double.parseDouble(tfLong.getText());
            tfLong.setId("text-field-empty");
        }
        catch (Exception ex){
            tfLong.setId("text-field-red");
            tfLong.setText("");
            tfLong.setPromptText("Enter Longitude as number");
            check = false;
        }

        return check;

    }

    /**
     * saves a new mediatyp
     */
    public void read() {
        if (validation() == true) {
            Media med = add();
            med.ind = index;
            index++;
            mediaList.add(med);
            hyperlinks.add(med.title);
            buttonDone.setDisable(false);
            tfUrl.setText("");
            tfTitle.setText("");
            tfDesc.setText("");
            tfLat.setText("");
            tfLong.setText("");
            tfIndex.setText("");
            border.setLeft(vBoxLeft());
        } else {
            buttonDone.setDisable(true);
        }

    }

    /**
     * save the changes in an exciting mediatyp
     */
    public void change() {
        if (validation() == true) {
            Media med = add();
            mediaList.set(Integer.valueOf(tfIndex.getText()), med);
            hyperlinks.set(Integer.valueOf(tfIndex.getText()), med.title);
            buttonDone.setDisable(false);
            tfUrl.setText("");
            tfTitle.setText("");
            tfDesc.setText("");
            tfLat.setText("");
            tfLong.setText("");
            tfIndex.setText(null);
            border.setLeft(vBoxLeft());
            buttonSave.setVisible(false);
            buttonAddMedia.setDisable(false);
        } else {
            buttonDone.setDisable(true);
        }
    }

    /**
     * loads a local geoJSON-file
     */
    public void loadFile() {
        FileChooser chooseGeoJSON = new FileChooser();
        chooseGeoJSON.setInitialDirectory(new File("C:\\"));
        chooseGeoJSON.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GEOJSON Files", "*geojson"));
        File selectedGeoJSON = chooseGeoJSON.showOpenDialog(null);

        if (selectedGeoJSON != null) {
            tfGeoJSON.setText(selectedGeoJSON.getName());
            buttonGeoJSON.setText("Change file");
            String adress = selectedGeoJSON.getPath();
            try (BufferedReader reader = new BufferedReader(new FileReader(new File(adress)))) {

                String line;
                while ((line = reader.readLine()) != null)
                    geoJSONinput = line;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            tfGeoJSON.setText("No file selected.");
        }
    }

    /**
     * loads the data, that belongs to the hyperlink
     * fills out the input screen with the saved data
     *
     * @param z: index of the loaded media
     */
    public void loadMedia(int z) {
        mediatype.setValue(mediaList.get(z).mediat);
        tfTitle.setText(mediaList.get(z).title);
        tfDesc.setText(mediaList.get(z).description);
        tfLong.setText(mediaList.get(z).longitude.toString());
        tfLat.setText(mediaList.get(z).latitude.toString());
        tfIndex.setText(mediaList.get(z).ind.toString());
    }

    /**
     * final method, creates the html-code that is needed to add a map to a website
     */
    public void print() {
        String markers = "";
        String geoJSON = "";

        //query to add all media as a popup to the map
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).mediat == "Picture") {
                markers = markers + "L.marker([" + mediaList.get(i).latitude + ", " + mediaList.get(i).longitude + "]).addTo(mymap).bindPopup(\"<b><big>" + mediaList.get(i).title + "</b><br />" + mediaList.get(i).description + "</big><br /><br /> <a href='" + mediaList.get(i).url + "'> <img src='" + mediaList.get(i).url + "' width='400px'></a>\", customOptions);"+ System.lineSeparator();
            }
            if (mediaList.get(i).mediat == "Text") {
                markers = markers + "L.marker([" + mediaList.get(i).latitude + ", " + mediaList.get(i).longitude + "]).addTo(mymap).bindPopup(\"<b><big>" + mediaList.get(i).title + "</b><br />" + mediaList.get(i).description + "</big><br /><br />\", customOptions);"+ System.lineSeparator();
            }
            if (mediaList.get(i).mediat == "Video") {
                markers = markers + "L.marker([" + mediaList.get(i).latitude + ", " + mediaList.get(i).longitude + "]).addTo(mymap).bindPopup(\"<b><big>" + mediaList.get(i).title + "</b><br />" + mediaList.get(i).description + "</big><br /><br /> <video src='" + mediaList.get(i).url + "' controls width='400px'></a>\", customOptions);" + System.lineSeparator();
            }

        }

        //query, when a geoJSON-file is added it generates the code needed to add it to the map
        if (cbGeoJSON.isSelected() == true) {
            geoJSON = "var geojsonFeature =[" + geoJSONinput + "];" + System.lineSeparator() +
                    "var myStyle = {" + System.lineSeparator() +
                    "\"color\": \"#ff9944\"," + System.lineSeparator() +
                    "\"weight\": 5," + System.lineSeparator() +
                    "\"opacity\": 0.65};" + System.lineSeparator() +
                    "L.geoJSON(geojsonFeature, {style: myStyle}).addTo(mymap);";
        }

        //put's all textpieces together, that are needed for the map
        outputtext = ("<html>" + System.lineSeparator() +
                "<head>" + System.lineSeparator() +
                "<title>Karte</title>" + System.lineSeparator() +
                "<meta charset =\"utf-8\" />" + System.lineSeparator() +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" + System.lineSeparator() +
                "<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"docs/images/favicon.ico\" />" + System.lineSeparator() +
                "<link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.3.1/dist/leaflet.css\" integrity=\"sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ==\" crossorigin=\"\"/>" + System.lineSeparator() +
                "<script src=\"https://unpkg.com/leaflet@1.3.1/dist/leaflet.js\" integrity=\"sha512-/Nsx9X4HebavoBvEBuyp3I7od5tA0UzAxs+j83KgC8PU0kgB4XiK4Lfe4y4cgBtaRJQEIFCW+oC506aPT2L1zw==\" crossorigin=\"\">" + "</script>" + System.lineSeparator() +
                "</head>" + System.lineSeparator() +
                "<body>" + System.lineSeparator() +
                "<div id=\"mapid\" style=\"width: 800px; height: 500px;\"></div>" + System.lineSeparator() +
                "<script src=\"sample-geojson.js\" type=\"text/javascript\"></script>" + System.lineSeparator() +
                "<script>" + System.lineSeparator() +
                "var attri = 'Map data &copy; <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, '" + System.lineSeparator() +
                "'<a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, '" + System.lineSeparator() +
                "'Imagery © <a href=\"http://mapbox.com\">Mapbox</a>'," + System.lineSeparator() +
                "Url = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';" + System.lineSeparator() +
                "var mapbox = L.tileLayer(Url, {id: 'mapbox.streets', attribution: attri})" +System.lineSeparator() +
                "satellite = L.tileLayer(Url, {id: 'mapbox.satellite', attribution: attri});" +System.lineSeparator() +
                "var mymap = L.map('mapid',{" + System.lineSeparator() +
                "center:[" + mediaList.getFirst().latitude + ", " + mediaList.getFirst().longitude + "]," + System.lineSeparator() +
                "zoom: 5," + System.lineSeparator() +
                "layers: [mapbox, satellite]});" + System.lineSeparator() +
                System.lineSeparator() +
                "var baseLayers = {" + System.lineSeparator() +
                "\"Satellite\": satellite," + System.lineSeparator() +
                "\"Streets\": mapbox," + System.lineSeparator() +
                "};" + System.lineSeparator() +
                "L.control.layers(baseLayers).addTo(mymap);"+ System.lineSeparator() +
                System.lineSeparator() +
                "var customOptions = {'maxWidth': '400', 'width': '200',}" + System.lineSeparator() +
                System.lineSeparator() +
                markers + System.lineSeparator() +
                System.lineSeparator() +
                geoJSON + System.lineSeparator() +
                "</script>" + System.lineSeparator() +
                "</body>" + System.lineSeparator() +
                "</html>"
        );
    }

}
