package sample;

import com.jfoenix.controls.JFXButton;
import connectivity.ConnectionClass;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SignUpController {

    @FXML
    public JFXButton SignInButton;

    @FXML
    public TextField FirstNameField;

    @FXML
    public TextField LastNameField;

    @FXML
    public TextField PhoneNumberField;

    @FXML
    public TextField EmailField;

    @FXML
    public TextField UsernameField;

    @FXML
    public TextField PasswordField;

    @FXML
    public TextField RePasswordField;

    @FXML
    public ImageView imgView;


    @FXML
    public JFXButton SignUpButton;


    @FXML
    public Button imagebutton;

    @FXML
    void SignUp(ActionEvent event)  {

//        ConnectionClass connectionClass = new ConnectionClass();
//        Connection connection = connectionClass.getConnection();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost/moiare","root","");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String FirstName = FirstNameField.getText();
        String LastName = LastNameField.getText();
        String PhoneNumber = PhoneNumberField.getText();
        String Email = EmailField.getText();
        String UserName = UsernameField.getText();
        String Password = PasswordField.getText();
        String RePassword = RePasswordField.getText();

        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("select * from user where UserName = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            st.setString(1, UserName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet r1 = null;
        try {
            r1 = st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (r1.next()) {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("0");
                    alert.setHeaderText("Look, an Information Dialog");
                    alert.setContentText("I have a great message for you!");
                    alert.showAndWait();
            }
            else{
                if(!isValidEmailAddress(Email)){
                     Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("1");
                    alert.setHeaderText("Look, an Information Dialog");
                    alert.setContentText("I have a great message for you!");
                    alert.showAndWait();
                }
                else{
                    if(!checkPassword(Password,RePassword)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("2");
                        alert.setHeaderText("Look, an Information Dialog");
                        alert.setContentText("I have a great message for you!");
                        alert.showAndWait();
                    }
                    else{
                        PreparedStatement ps=connection.prepareStatement("insert into user (FirstName,LastName,PhoneNumber,Email,UserName,Password,Status,UseNumber) values(?,?,?,?,?,?,?,?)");
                        ps.setString(1,FirstName);
                        ps.setString(2,LastName);
                        ps.setString(3,PhoneNumber);
                        ps.setString(4,Email);
                        ps.setString(5,UserName);
                        ps.setString(6,Password);
                        ps.setString(7,"user");
                        ps.setInt(8,0);
                        ps.execute();
                        connection.close();

                        try {
                            Parent newRoot = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                            Stage primaryStage = (Stage) SignUpButton.getScene().getWindow();
                            primaryStage.getScene().setRoot(newRoot);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public boolean checkPassword(String password, String repassword){
        if(password.equals(repassword)){
            if(password.length()>=8){

                return true;
            }else{
                return false;
//                gamotana ar aris rtuli
            }
        }else{
//            gamotana rom ar udris
            return false;
        }
    }



    @FXML
    void changeinto(ActionEvent event) {
        SignInButton.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }


