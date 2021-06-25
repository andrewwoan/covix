package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.DBConnection;
import sample.model.ConnectionTable;
import sample.model.GroupTable;

public class DashboardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button myDashBtn;

    @FXML
    private Button GroupBtn;

    @FXML
    private Button ConnectionBtn;

    @FXML
    private Button UpdateStatusBtn;

    @FXML
    private Button AboutBtn;

    @FXML
    private Label numberOfConnectionsLabel;

    @FXML
    private Label numberOfGroupsLabel;

    @FXML
    private Label covixScoreLbl;

    @FXML
    private Label nameLbl;

    @FXML
    private TableView connectionsTable;

    @FXML
    private TableView groupsTable;

    @FXML
    private TableColumn<GroupTable, String> groupAreaCol;

    @FXML
    private TableColumn<GroupTable, String> numCol;

    ObservableList<GroupTable> obListCon2 = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ConnectionTable, String> connectionNameCol;

    @FXML
    private TableColumn<ConnectionTable, String> connectionEmailCol;

    @FXML
    private TableColumn<ConnectionTable, String> connectionCovixScoreCol;

    ObservableList<ConnectionTable> obListCon = FXCollections.observableArrayList();

    @FXML
    private Label vacLbl;

    @FXML
    private Label riskLbl;


    @FXML
    private Button logoutBtn;

    private String emailP = "";

    public void setName(String name){
        emailP = name;
        nameLbl.setText(name);
//        System.out.println(name + emailP);
        int groupCounter = 0;
        int connectionsCounter = 0;

        DBConnection connection = new DBConnection();
        connectionsTable.getItems().clear();
        int vacStat = connection.getIfInGroup("vacStat",nameLbl.getText());
        if (vacStat==1){
            vacLbl.setText("Vaccinated");
        }else{
            vacLbl.setText("Not Vaccinated");
        }
        int covixScore = connection.getCovixScore(nameLbl.getText());

//        ---------------------------
        ResultSet userlist = connection.getUserList(nameLbl.getText());

        while(true){
            try {
                if (!userlist.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                System.out.println(userlist.getString("fullName")+" "+userlist.getString("email")+ " " +userlist.getString("covixScore"));
                connectionsCounter+=1;
                obListCon.add(new ConnectionTable(userlist.getString("fullName"),userlist.getString("email"),userlist.getString("covixScore")));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        covixScoreLbl.setText(String.valueOf(covixScore));

        connection.updateRiskScore(emailP,connectionsCounter*5);
        riskLbl.setText(String.valueOf(connectionsCounter*5));

        if(connection.getIfInGroup("sc",emailP)==1){
            String number = String.valueOf(connection.countHowMany("statecollege"));
            obListCon2.add(new GroupTable("State College, PA",number));
            groupCounter+=1;
        }

        if(connection.getIfInGroup("ph",emailP)==1){
            String number = String.valueOf(connection.countHowMany("philadelphia"));
            obListCon2.add(new GroupTable("Philadelphia, PA",number));
            groupCounter+=1;
        }

        if(connection.getIfInGroup("er",emailP)==1){
            String number = String.valueOf(connection.countHowMany("erie"));
            obListCon2.add(new GroupTable("Erie, PA",number));
            groupCounter+=1;
        }

        if(connection.getIfInGroup("gr",emailP)==1){
            String number = String.valueOf(connection.countHowMany("greene"));
            obListCon2.add(new GroupTable("Greene, PA",number));
            groupCounter+=1;
        }

        numberOfGroupsLabel.setText("("+String.valueOf(groupCounter)+")");
        numberOfConnectionsLabel.setText("("+String.valueOf(connectionsCounter)+")");

        connectionNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        connectionEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        connectionCovixScoreCol.setCellValueFactory(new PropertyValueFactory<>("covixScore"));

        groupAreaCol.setCellValueFactory(new PropertyValueFactory<>("groupArea"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("numberOfPeople"));

        connectionsTable.setItems(obListCon);
        groupsTable.setItems(obListCon2);

    }

    @FXML
    void initialize() {

//        Update stuff everytime we go to dashboard controller.


        UpdateStatusBtn.setOnAction(event -> {
            UpdateStatusBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/UpdateStatus.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            UpdateStatusController updateStatusController = loader.getController();
            updateStatusController.setEmail(nameLbl.getText().trim());

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        });


        AboutBtn.setOnAction(event -> {
            UpdateStatusBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/About.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

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




        GroupBtn.setOnAction(event -> {
            ConnectionBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Group.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GroupController controller = loader.getController();
            controller.setEmail(nameLbl.getText().trim());

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        });

        ConnectionBtn.setOnAction(event -> {
            ConnectionBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/OtherUserDashboard.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            OtherUserDashboardController controller = loader.getController();
            controller.setEmail(nameLbl.getText().trim());

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        });

        logoutBtn.setOnAction(event -> {
            logoutBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/login.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        });





    }
}
