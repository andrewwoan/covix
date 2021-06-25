package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField createAccountEmail;

    @FXML
    private Button loginButtonSwitch;

    @FXML
    private PasswordField createAccountPassword;

    @FXML
    private TextField createAccountFullName;

    @FXML
    void initialize() {
        DBConnection connection = new DBConnection();

        //  Takes user to Create Account page
        loginButtonSwitch.setOnAction(event -> {
            loginButtonSwitch.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/login.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

        });

        createAccountButton.setOnAction(event -> {
//            System.out.println("Creating User");
            connection.createUser(  createAccountFullName.getText(),
                                    createAccountEmail.getText(),
                                    createAccountPassword.getText() );

            String userid = connection.getUserID(createAccountEmail.getText());
            connection.createUserConnections(userid);
        }); }


}
