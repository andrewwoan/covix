package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.database.DBConnection;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateStatusController {

    @FXML
    private Button myDashBtn;

    @FXML
    private Button GroupBtn;

    @FXML
    private Button ConnectionBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button UpdateStatusBtn;

    @FXML
    private Button AboutBtn;

    @FXML
    private ComboBox VaccinatedComboBox;

    @FXML
    private ComboBox TestedComboBox;

    @FXML
    private CheckBox sobCheckBox;

    @FXML
    private CheckBox feverCheckBox;

    @FXML
    private CheckBox dryCoughCheckBox;

    @FXML
    private CheckBox chestPainCheckBox;

    @FXML
    private CheckBox lossCheckBox;

    @FXML
    private CheckBox tirednessCheckBox;

    @FXML
    private CheckBox muscleCheckBox;

    @FXML
    private CheckBox diarCheckBox;

    @FXML
    private CheckBox nauseaCheckBox;

    @FXML
    private Button updateStatusBtn;

    @FXML
    private Label updateStatusLabelSuccess;

    private String emailP = "";

    public void setEmail(String email){
        emailP=email;
        updateStatusLabelSuccess.setText("Updating status for "+email);
    }

    @FXML
    void initialize() {

        DBConnection connection = new DBConnection();

        TestedComboBox.getItems().clear();
        VaccinatedComboBox.getItems().clear();


        VaccinatedComboBox.getItems().addAll(
                "Yes",
                "No"
        );

        TestedComboBox.getItems().addAll(
                "Yes",
                "No"
        );


        updateStatusBtn.setOnAction(event -> {

            int totalCovixScore = 0;

            if (sobCheckBox.isSelected()){
                totalCovixScore += 10;
            }


            if (feverCheckBox.isSelected()){
                totalCovixScore += 10;
            }

            if (dryCoughCheckBox.isSelected()){
                totalCovixScore += 10;
            }

            if (chestPainCheckBox.isSelected()){
                totalCovixScore += 10;
            }

            if (lossCheckBox.isSelected()){
                totalCovixScore += 10;
            }

            if (tirednessCheckBox.isSelected()){
                totalCovixScore += 10;
            }

            if (muscleCheckBox.isSelected()){
                totalCovixScore += 10;
            }

            if (diarCheckBox.isSelected()){
                totalCovixScore += 10;
            }

            if (nauseaCheckBox.isSelected()){
                totalCovixScore += 10;
            }

            if(VaccinatedComboBox.getValue().toString().equals("Yes")){
                totalCovixScore = (int)(totalCovixScore*.10);
                connection.updateGroupAdd("vacStat",emailP);
            }else{
                connection.updateGroupRemove("vacStat",emailP);
            }
            if(TestedComboBox.getValue().toString().equals("Yes")){
                totalCovixScore = (int)(totalCovixScore*.50);
            }

            int previous = connection.getPreviousStatus(emailP);

            connection.updatePreviousStatus(emailP, totalCovixScore);

            updateStatusLabelSuccess.setText("Status successfully updated.");

            ArrayList<String> allTables = connection.getAllTables();

            for(String i:allTables){
                System.out.println(i);
                int covixScore = connection.getCovixScore(emailP);
                int changeAmount = covixScore-previous;
                int changeAmount3 = changeAmount+totalCovixScore;
//                System.out.println(changeAmount3);
                connection.updateUserScores(i,emailP,changeAmount3);
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

        ConnectionBtn.setOnAction(event -> {
            ConnectionBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/OtherUserDashboard.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            OtherUserDashboardController controller = loader.getController();
            controller.setEmail(emailP);

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
