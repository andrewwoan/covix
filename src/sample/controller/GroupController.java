package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import sample.database.DBConnection;
import sample.model.ConnectionTable;
import sample.model.User;


public class GroupController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button myDashBtn;

    @FXML
    private Button GroupBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button ConnectionBtn;

    @FXML
    private Button UpdateStatusBtn;

    @FXML
    private Button AboutBtn;

    @FXML
    private Label nameLbl;

    @FXML
    private WebView mapView;

    @FXML
    private TableView groupMembersTable;

    @FXML
    private TableColumn<ConnectionTable, String> connectionNameCol;

    @FXML
    private TableColumn<ConnectionTable, String> connectionEmailCol;

    @FXML
    private TableColumn<ConnectionTable, String> connectionCovixScoreCol;

    @FXML
    private Button joinBtn;

    @FXML
    private ComboBox areaSelectionComboBox;

    @FXML
    private Label covixScoreLbl;

    @FXML
    private Button resetMap;

    private String emailP = "";

    ObservableList<ConnectionTable> obListCon = FXCollections.observableArrayList();

    public void setEmail(String email){
        emailP=email;

    }

    public void setTable(String name){
        DBConnection connection = new DBConnection();
//        ---------------------------
        ResultSet userlist = connection.getGroupList(name);
        while(true){
            try {
                if (!userlist.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                obListCon.add(new ConnectionTable(userlist.getString("fullName"),userlist.getString("email"),userlist.getString("covixScore")));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        connectionNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        connectionEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        connectionCovixScoreCol.setCellValueFactory(new PropertyValueFactory<>("covixScore"));

        groupMembersTable.setItems(obListCon);
    }

    public int calcGroupScore(String whichTable){
        int groupScore = 0;
        DBConnection connection = new DBConnection();
        groupScore+=connection.calcGroupScore(whichTable,"covixScore");
        return groupScore;
    }

    public boolean setBtnText(String whichTable, String comboBoxSelection){
        boolean nullOrNot = false;
        DBConnection connection = new DBConnection();
        nullOrNot = connection.checkGroup(whichTable,comboBoxSelection);
        return nullOrNot;
    }

    @FXML
    void initialize() {

        DBConnection connection = new DBConnection();

        WebEngine webEngine = mapView.getEngine();
        final URL urlGoogleMaps = getClass().getResource("/sample/html/googleMapMain.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        webEngine.setJavaScriptEnabled(true);

        areaSelectionComboBox.getItems().addAll(
                "State College, PA",
                "Erie, PA",
                "Philadelphia, PA",
                "Greene, PA"
        );
        resetMap.setOnAction(event -> {
            URL goog = getClass().getResource("/sample/html/googleMapMain.html");
            webEngine.load(goog.toExternalForm());
            groupMembersTable.getItems().clear();
            covixScoreLbl.setText("----");
        });

        joinBtn.setOnAction(event -> {
            User user = connection.getUser(emailP);
            groupMembersTable.getItems().clear();

            if(joinBtn.getText().equals("Join Group")){
                if(areaSelectionComboBox.getValue().toString().equals("State College, PA")){
                    connection.insertIntoGroup("statecollege",user.getFullName(),emailP);
                    connection.updateGroupAdd("sc",emailP);
                    setTable("statecollege");
                    covixScoreLbl.setText(String.valueOf(calcGroupScore("statecollege")));
                }else if(areaSelectionComboBox.getValue().toString().equals("Erie, PA")){
                    connection.insertIntoGroup("erie",user.getFullName(),emailP);
                    connection.updateGroupAdd("er",emailP);
                    setTable("erie");
                    covixScoreLbl.setText(String.valueOf(calcGroupScore("erie")));
                }else if(areaSelectionComboBox.getValue().toString().equals("Philadelphia, PA")) {
                    connection.insertIntoGroup("philadelphia",user.getFullName(),emailP);
                    connection.updateGroupAdd("ph",emailP);
                    setTable("philadelphia");
                    covixScoreLbl.setText(String.valueOf(calcGroupScore("philadelphia")));
                }else if(areaSelectionComboBox.getValue().toString().equals("Greene, PA")){
                    connection.insertIntoGroup("greene",user.getFullName(),emailP);
                    connection.updateGroupAdd("gr",emailP);
                    setTable("Greene");
                    covixScoreLbl.setText(String.valueOf(calcGroupScore("Greene")));

                }
                joinBtn.setText("Leave Group");
            }else{
                if(areaSelectionComboBox.getValue().toString().equals("State College, PA")){
                    connection.deleteFromGroup("statecollege",emailP);
                    connection.updateGroupRemove("sc",emailP);
                    setTable("statecollege");
                    covixScoreLbl.setText(String.valueOf(calcGroupScore("statecollege")));
                }else if(areaSelectionComboBox.getValue().toString().equals("Erie, PA")){
                    connection.deleteFromGroup("erie",emailP);
                    connection.updateGroupRemove("er",emailP);
                    setTable("erie");
                    covixScoreLbl.setText(String.valueOf(calcGroupScore("erie")));
                }else if(areaSelectionComboBox.getValue().toString().equals("Philadelphia, PA")) {
                    connection.deleteFromGroup("philadelphia",emailP);
                    connection.updateGroupRemove("ph",emailP);
                    setTable("philadelphia");
                    covixScoreLbl.setText(String.valueOf(calcGroupScore("philadelphia")));
                }else if(areaSelectionComboBox.getValue().toString().equals("Greene, PA")){
                    connection.deleteFromGroup("greene",emailP);
                    connection.updateGroupRemove("gr",emailP);
                    setTable("greene");
                    covixScoreLbl.setText(String.valueOf(calcGroupScore("greene")));
                }
                joinBtn.setText("Join Group");
            }
        });


        areaSelectionComboBox.setOnAction(event -> {
            groupMembersTable.getItems().clear();
            if(areaSelectionComboBox.getValue().toString().equals("State College, PA")){
                URL goog = getClass().getResource("/sample/html/googleMap.html");
                webEngine.load(goog.toExternalForm());
                setTable("statecollege");
                covixScoreLbl.setText(String.valueOf(calcGroupScore("statecollege")));
                if(setBtnText("statecollege",emailP)){
                    joinBtn.setText("Join Group");
                }else{
                    joinBtn.setText("Leave Group");
                }
            }else if(areaSelectionComboBox.getValue().toString().equals("Erie, PA")){
                URL goog = getClass().getResource("/sample/html/googleMap3.html");
                webEngine.load(goog.toExternalForm());
                setTable("erie");
                covixScoreLbl.setText(String.valueOf(calcGroupScore("erie")));
                if(setBtnText("erie",emailP)){
                    joinBtn.setText("Join Group");
                }else{
                    joinBtn.setText("Leave Group");
                }
            }else if(areaSelectionComboBox.getValue().toString().equals("Philadelphia, PA")) {
                URL goog = getClass().getResource("/sample/html/googleMap2.html");
                webEngine.load(goog.toExternalForm());
                setTable("philadelphia");
                covixScoreLbl.setText(String.valueOf(calcGroupScore("philadelphia")));

                if(setBtnText("philadelphia",emailP)){
                    joinBtn.setText("Join Group");
                }else{
                    joinBtn.setText("Leave Group");
                }
            }else if(areaSelectionComboBox.getValue().toString().equals("Greene, PA")){
                URL goog = getClass().getResource("/sample/html/googleMap4.html");
                webEngine.load(goog.toExternalForm());
                setTable("greene");
                covixScoreLbl.setText(String.valueOf(calcGroupScore("statecollege")));

                if(setBtnText("greene",emailP)){
                    joinBtn.setText("Join Group");
                }else{
                    joinBtn.setText("Leave Group");
                }
            }


        });
        myDashBtn.setOnAction(event -> {
            myDashBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Dashboard.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DashboardController dashboardController = loader.getController();
            dashboardController.setName(emailP);

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });

}}
