package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SignInController implements Initializable {
    @FXML
    private JFXButton SignUpButton;

    @FXML
    private JFXTextField UserNamefield;

    @FXML
    private JFXTextField PasswordField;

    @FXML
    private JFXButton SignInButton;

    @FXML
    void SignIn(ActionEvent event) {
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

        String query = "SELECT * FROM `user` WHERE `UserName` = ? AND `Password` = ?";

        String username=UserNamefield.getText();
        String password=PasswordField.getText();
        try {
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                SignInButton.getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                MainPageController controller = fxmlLoader.<MainPageController>getController();
                System.out.println(rs.getInt("UserID"));
                controller.setUser(rs.getInt("UserID"));
                controller.setAdmin(rs.getBoolean("IsAdmin"));

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();


            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Information dialog");
                alert.setContentText("Error");
                alert.showAndWait();
            }


            {
    }

} catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

