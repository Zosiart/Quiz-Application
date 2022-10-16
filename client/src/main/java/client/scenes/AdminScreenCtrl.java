package client.scenes;

import client.utils.ServerUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Activity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminScreenCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<Activity> data;

    /**
     * Creates a new screen with injections
     *
     * @param server   ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public AdminScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Returns to the home screen
     */
    public void goToHomeScreen() {
        mainCtrl.showHomeScreen();
    }

    @FXML
    private TableView<Activity> activityTable;

    @FXML
    private TableColumn<Activity, String> id;

    @FXML
    private TableColumn<Activity, String> title;

    @FXML
    private TableColumn<Activity, String> source;

    @FXML
    private TableColumn<Activity, String> consumption;

    @FXML
    private TableColumn<Activity, String> image_path;

    @FXML
    private TextField inputActivityID;

    @FXML
    private TextField inputActivityTitle;

    @FXML
    private TextField inputActivityConsumption;

    @FXML
    private TextField inputActivitySource;

    @FXML
    private TextField inputActivityImagePath;


    /**
     * Get values into the table
     */
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getId()));
        title.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTitle()));
        source.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSource()));
        consumption.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getConsumption_in_wh().toString()));
        image_path.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getImage_path()));
    }

    /**
     * Refresh admin screen
     */
    @FXML
    public void refresh() {
        try {
            List<Activity> activityList = mainCtrl.getServer().getAllActivities();
            ObservableList<Activity> observableArrayList =
                    FXCollections.observableArrayList(activityList);
            activityTable.setItems(observableArrayList);
        } catch (Exception e) {
            mainCtrl.showPopup(Alert.AlertType.ERROR, "Connection failed");
        }
    }

    /**
     * Adds activity to database
     */
    public void addActivity() {
        Activity activity = checkTextFields();
        if (activity != null) {
            try {
                mainCtrl.getServer().addActivity(activity);
                refresh();
                Alert successfulAdd = new Alert(Alert.AlertType.INFORMATION);
                successfulAdd.initOwner(mainCtrl.getPrimaryStage());
                successfulAdd.setHeaderText("The activity has been added successfully!");
                successfulAdd.showAndWait();
            } catch (Exception e){
                mainCtrl.showPopup(Alert.AlertType.ERROR, "Failed to add the activity");
            }
        }
    }

    /**
     * Deletes activity from a database
     */
    public void deleteActivity() {
        Activity toBeDeleted = activityTable.getSelectionModel().getSelectedItem();
        if (toBeDeleted == null) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.initOwner(mainCtrl.getPrimaryStage());
            noSelection.setHeaderText("Please select an activity from the table first");
            noSelection.showAndWait();
            return;
        }
        Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDeletion.setHeaderText("The activity is about to be deleted (no undo)!");
        confirmDeletion.initOwner(mainCtrl.getPrimaryStage());
        ((Button) confirmDeletion.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) confirmDeletion.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        Optional<ButtonType> res = confirmDeletion.showAndWait();
        if (res.get() == ButtonType.OK) {
            try {
                mainCtrl.getServer().deleteActivity(toBeDeleted);
            } catch (Exception e){
                mainCtrl.showPopup(Alert.AlertType.ERROR, "Failed to delete the activity");
            }
        }
        refresh();
    }

    /**
     * Edits activity from database
     */
    public void editActivity() {
        Activity toBeEdited = activityTable.getSelectionModel().getSelectedItem();
        if (toBeEdited == null) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setHeaderText("Please select an activity to edit from the table first");
            noSelection.initOwner(mainCtrl.getPrimaryStage());
            noSelection.showAndWait();
        }
        try {
            inputActivityID.setText(toBeEdited.getId());
            inputActivityImagePath.setText(toBeEdited.getImage_path());
            inputActivityTitle.setText(toBeEdited.getTitle());
            inputActivityConsumption.setText(Long.toString(toBeEdited.getConsumption_in_wh()));
            inputActivitySource.setText(toBeEdited.getSource());
            mainCtrl.getServer().deleteActivity(toBeEdited);
        } catch (Exception e){
            mainCtrl.showPopup(Alert.AlertType.ERROR, "Failed to get the activity");
        }
    }

    /**
     * Commits changes to server
     */
    public void commitEdit() {
        Activity toBeAdded = checkTextFields();
        if (toBeAdded != null) {
            try {
                mainCtrl.getServer().addActivity(toBeAdded);
                refresh();
            } catch (Exception e){
                mainCtrl.showPopup(Alert.AlertType.ERROR, "Failed to edit the activity");
            }
        }
    }


    /**
     * Checks whether text fields are filled
     *
     * @return
     */
    public Activity checkTextFields() {
        String id = inputActivityID.getText();
        String path = inputActivityImagePath.getText();
        String title = inputActivityTitle.getText();
        String source = inputActivitySource.getText();
        if (id.length() == 0 || path.length() == 0 || title.length() == 0
                || inputActivityConsumption.getText().length() == 0 || source.length() == 0) {
            Alert emptyFields = new Alert(Alert.AlertType.ERROR);
            emptyFields.initOwner(mainCtrl.getPrimaryStage());
            emptyFields.setHeaderText("Please fill all the text fields with corresponding data.");
            emptyFields.showAndWait();
            return null;
        }
        try {
            long consumption = Long.parseLong(inputActivityConsumption.getText());
            clearFields();
            Activity a = new Activity(id, path, title, consumption, source);
            return a;
        } catch (NumberFormatException e) {
            Alert wrongType = new Alert(Alert.AlertType.ERROR);
            wrongType.initOwner(mainCtrl.getPrimaryStage());
            wrongType.setHeaderText("Consumption must be an integer number!");
            wrongType.showAndWait();
            return null;
        }
    }

    /**
     * Clears text fields after them being manipulated
     */
    public void clearFields() {
        inputActivityID.clear();
        inputActivityImagePath.clear();
        inputActivityTitle.clear();
        inputActivityConsumption.clear();
        inputActivitySource.clear();
    }

    /**
     * method for importing activities from a json file
     */
    public void importActivities() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a JSON file to import from");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File selectedJson = fileChooser.showOpenDialog(new Stage());

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Activity> activityList = mapper.readValue(selectedJson, new TypeReference<List<Activity>>() {});
            for(var a : activityList){
                server.addActivity(a);
            }
            Alert successfulAdd = new Alert(Alert.AlertType.INFORMATION);
            successfulAdd.setHeaderText("Activities have been added successfully!");
            successfulAdd.initOwner(mainCtrl.getPrimaryStage());
            successfulAdd.showAndWait();
            refresh();
        }
        catch(IOException e) {
            Alert wrongImport = new Alert(Alert.AlertType.ERROR);
            wrongImport.setHeaderText("Something when wrong, please make sure the activities are in correct format!");
            wrongImport.initOwner(mainCtrl.getPrimaryStage());
            wrongImport.showAndWait();
        }
    }
}
