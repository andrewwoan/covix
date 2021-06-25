package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.DBConnection;
import sample.model.ConnectionTable;
import sample.model.GroupTable;
import sample.model.User;

public class OtherUserDashboardController {

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
    private Label covixScoreLbl;

    @FXML
    private Label numberOfConnectionsLabel;

    @FXML
    private Label numberOfGroupsLabel;

    @FXML
    private TableView connectionsTable;

    @FXML
    private TableColumn<ConnectionTable, String> connectionNameCol;

    @FXML
    private TableColumn<ConnectionTable, String> connectionEmailCol;

    @FXML
    private TableColumn<ConnectionTable, String> connectionCovixScoreCol;

    @FXML
    private TableView groupsTable;

    @FXML
    private TableColumn<GroupTable, String> groupAreaCol;

    @FXML
    private TableColumn<GroupTable, String> numCol;

    @FXML
    private ComboBox userComboBox;


    @FXML
    private Label userScoreLbl;

    @FXML
    private Button connectBtn;

    private String emailP = "";

    @FXML
    private Label riskScoreLbl;

    @FXML
    private Label vacStatLbl;


    ObservableList<ConnectionTable> obListCon = FXCollections.observableArrayList();


    ObservableList<GroupTable> obListCon2 = FXCollections.observableArrayList();


    public void setEmail(String email){
        emailP=email;

        ArrayList<String> emails = null;
        DBConnection connection = new DBConnection();
//        System.out.println("hello" + emailP);
        emails = connection.getEmails(emailP);


        userComboBox.setItems(FXCollections.observableArrayList(emails));

    }

    public void setTable(String email){
        DBConnection connection = new DBConnection();
        int covixScore = connection.getCovixScore(email);
        int groupCounter = 0;
        int connectionsCounter = 0;
        covixScoreLbl.setText(String.valueOf(covixScore));

//        ---------------------------
        ResultSet userlist = connection.getUserList(email);
        while(true){
            try {
                if (!userlist.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                connectionsCounter+=1;
                obListCon.add(new ConnectionTable(userlist.getString("fullName"),userlist.getString("email"),userlist.getString("covixScore")));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        if(connection.getIfInGroup("sc",email)==1){
            String number = String.valueOf(connection.countHowMany("statecollege"));
            obListCon2.add(new GroupTable("State College, PA",number));
            groupCounter+=1;
        }

        if(connection.getIfInGroup("ph",email)==1){
            String number = String.valueOf(connection.countHowMany("philadelphia"));
            obListCon2.add(new GroupTable("Philadelphia, PA",number));
            groupCounter+=1;
        }

        if(connection.getIfInGroup("er",email)==1){
            String number = String.valueOf(connection.countHowMany("erie"));
            obListCon2.add(new GroupTable("Erie, PA",number));
            groupCounter+=1;
        }

        if(connection.getIfInGroup("gr",email)==1){
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

    public void updateCovixScores(String email, String cOrd, String otherEmail){
        DBConnection connection = new DBConnection();
        int totalScore = 0;
        totalScore+=connection.getCovixScore(email);

        System.out.println(cOrd);
        System.out.println(email);
        System.out.println(connection.userConnectionScores(connection.getUserID(email)));
        System.out.println(cOrd.equals("Connect"));


        if (cOrd.equals("Connect")){
            totalScore+=connection.getCovixScore(otherEmail);
        }else{
            totalScore-=connection.getCovixScore(otherEmail);
        }
        connection.updateCovixScore(email,totalScore);

    }

    public boolean setBtnText(String comboBoxSelection){
        boolean nullOrNot = false;
        DBConnection connection = new DBConnection();
        nullOrNot = connection.checkConnection(emailP,comboBoxSelection);
        return nullOrNot;
    }


    @FXML
    void initialize() {

        DBConnection connection = new DBConnection();

        ConnectionBtn.setOnAction(event -> {
            ConnectionBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/OtherUserDashboard.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            UpdateStatusController updateStatusController = loader.getController();
//            updateStatusController.setEmail(nameLbl.getText().trim());

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

        connectBtn.setOnAction(event -> {
//            Email of person they want to connect to

            String email = userComboBox.getValue().toString();
            String cOrd = connectBtn.getText();

            if(cOrd.equals("Connect")){
                updateCovixScores(emailP, cOrd, email);
                connection.connectUser(connection.getUserID(emailP),emailP,email);
                connectBtn.setText("Disconnect");
            }else{
                updateCovixScores(emailP, cOrd,email);
                connection.disconnectUser(connection.getUserID(emailP),email);
                connectBtn.setText("Connect");
            }

        });

        userComboBox.setOnAction(event -> {
            groupsTable.getItems().clear();
            connectionsTable.getItems().clear();
            User user = new User();
            user = connection.getUser(userComboBox.getValue().toString());
            setTable(userComboBox.getValue().toString());
            nameLbl.setText(user.getEmail());
            covixScoreLbl.setText(String.valueOf(connection.getCovixScore(user.getEmail())));
            userScoreLbl.setText(user.getFullName()+ "'s Covix Score");

            int vacStat = connection.getIfInGroup("vacStat",nameLbl.getText());
            if (vacStat==1){
                vacStatLbl.setText("Vaccinated");
            }else{
                vacStatLbl.setText("Not Vaccinated");
            }
            String temp = numberOfConnectionsLabel.getText().replace("(","").replace(")","");
            riskScoreLbl.setText(String.valueOf(Integer.parseInt(temp)*5));



            if(setBtnText(userComboBox.getValue().toString())){
                connectBtn.setText("Connect");
            }else{
                connectBtn.setText("Disconnect");
            }


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


    }
}
