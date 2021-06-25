package sample.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.DBConnection;
import sample.model.User;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginEmail;

    @FXML
    private Button createAccountSwitchButton;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField loginPassword;

    private DBConnection dbConnection;

    @FXML
    void initialize() {

        dbConnection = new DBConnection();


        loginButton.setOnAction(event-> {


            String loginEmailText = loginEmail.getText().trim();
            String loginPasswordText = loginPassword.getText().trim();

            User user = new User();
            user.setEmail(loginEmailText);
            user.setPassword(loginPasswordText);

            ResultSet userRow = dbConnection.checkForUser(user);
            int counter = 0;

            try{
                while (userRow.next()){
                    counter++;
                }
                if (counter==1){

                    loginButton.getScene().getWindow().hide();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Dashboard.fxml"));

                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Parent root = loader.getRoot();
                    DashboardController dashboardController = loader.getController();
                    dashboardController.setName(loginEmail.getText().trim());
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();

                }else{
                    errorLabel.setText("Email or Password Incorrect");
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        });

        //  Takes user to Create Account page
        createAccountSwitchButton.setOnAction(event -> {
            createAccountSwitchButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/CreateAccount.fxml"));

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

//    private void loginUser(String email, String password) {
//        //Checks if fields are empty, if so, bring them to user dashboard.
//        if(!email.equals("") || !password.equals("")){
//
//        }else{
//
//        }
//    }
}
