package sample;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    private int userID;


    @FXML
    private Button HomeButton1;

    @FXML
    private Button UploadButton1;

    @FXML
    public TextField TitleField;

    @FXML
    public TextArea AboutField;

    @FXML
    private HBox MainHbox;
    @FXML
    public Button imagebutton;

    @FXML
    public ImageView imgView;

    @FXML
    private Button Button1;
    @FXML
    public Button UploadButton;

    public FileInputStream fin = null;
    public int len;

    @FXML
    public JFXButton LogOutButton;

    @FXML
    private VBox MainVbox;
    public boolean isadmin;
    @FXML

    private BorderPane MainBorder;
    public void setUser(int user_id){
        this.userID = user_id;
    }
    public void setAdmin(boolean isAdmin){
        this.isadmin=isAdmin;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {

            Button1.getStyleClass().add("button1");
            HomeButton1.getStyleClass().add("button1");
            UploadButton1.getStyleClass().add("button1");
            show_MainPage();
            System.out.print(userID);
            System.out.println(isadmin);
            if(isadmin==false) {
                Button1.setVisible(false);
                UploadButton1.setVisible(false);
            }else{
                Button1.setVisible(true);
                UploadButton1.setVisible(true);
            }

        });

    }


    @FXML
    void LogOut(ActionEvent event) {
        LogOutButton.getScene().getWindow().hide();
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
    @FXML
    void Request_Click(ActionEvent event) {
        show_RequestPage();
    }
    public void show_RequestPage(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = null;


            conn = DriverManager.getConnection("jdbc:mysql://localhost/moiare", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from request Where Status IS NULL");
            VBox vbox = new VBox();
            while(rs.next()){
                HBox hbox = new HBox();
                hbox.setSpacing(20);
                hbox.setPadding(new Insets(20));
                rs.previous();
                String placeNamee="";
                String placeDescriptionee="";
                int pricee=0;
                Blob img=null;
                for(int i=0; i<4;i++){
                    if(rs.next()){
                        int RequestID=rs.getInt("RequestID");
                        int UserID=rs.getInt("UserID");
                        int PlaceID = rs.getInt("PlaceID");
                        System.out.println(RequestID+UserID+PlaceID);
                        conn = DriverManager.getConnection("jdbc:mysql://localhost/moiare", "root", "");
                        st = conn.createStatement();

                        ResultSet rs2 = st.executeQuery("Select * from content Where `PlaceID`= "+PlaceID);
                        if(rs2.next()){

                        img = rs2.getBlob("PlaceImage");
                        placeNamee=rs2.getString("PlaceName");
                        placeDescriptionee=rs2.getString("PlaceDerscription");
                        pricee = rs2.getInt("Price");}
                        InputStream is = img.getBinaryStream();
                        Image image = new Image(is);
                        is.close();
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(200);
                        imageView.setFitHeight(100);
                         Label placeName = new Label(placeNamee);
                         Label placeDescription = new Label(placeDescriptionee);

                        String prices = Integer.toString(pricee);
                        Label price = new Label(prices);
                        int ID=rs.getInt("PlaceID");
                        Label lab = new Label("$");
                        ResultSet rs1 = st.executeQuery("Select * from user Where `UserID`= "+UserID);
                        String name="";
                        if(rs1.next()) {
                            name=rs1.getString("FirstName") + rs1.getString("LastName");}
                            Label lab2 = new Label(name);
                            lab.setStyle("-fx-text-fill: yellow;");

                        Button Rejectbutton = new Button("Reject");
                        Button Acceptbutton = new Button("Accept");
                        Acceptbutton.setStyle(  "-fx-background-color: lightgreen;");
                        Rejectbutton.setStyle(  "-fx-background-color: red;");
                        HBox hbox1 = new HBox(lab,price);
                        VBox vbox2 = new VBox(placeName,placeDescription,lab2,hbox1);
                        Rejectbutton.setPadding(new Insets(20,0,0,0));
                        HBox hbox2 = new HBox(Acceptbutton,Rejectbutton);
                        vbox2.setStyle("-fx-background-color: white;");
                        vbox2.setPrefWidth(200);
                        vbox2.setPadding(new Insets(20));

                        VBox vbox1 = new VBox(imageView, vbox2,hbox2);
                        vbox1.setPadding(new Insets(20));
                        hbox.getChildren().addAll(vbox1);
                        Acceptbutton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                Connection conn = null;
                                try {
                                    conn = DriverManager.getConnection("jdbc:mysql://localhost/moiare", "root", "");

                                    PreparedStatement ps=conn.prepareStatement("Update request set `Status`=? Where `RequestID` = ? ");
                                    ps.setBoolean(1,true);
                                    ps.setInt(2,RequestID);
                                    ps.execute();
                                    conn.close();
                                    show_MainPage();
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                        Rejectbutton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                Connection conn = null;
                                try {
                                    conn = DriverManager.getConnection("jdbc:mysql://localhost/moiare", "root", "");

                                    PreparedStatement ps=conn.prepareStatement("Update request set `Status`=? Where `RequestID` = ? ");
                                    ps.setBoolean(1,false);
                                    ps.setInt(2,RequestID);
                                    ps.execute();
                                    conn.close();
                                    show_MainPage();
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    }
                }
                vbox.getChildren().addAll(hbox);
                vbox.setSpacing(50);
                vbox.setPadding(new Insets(20));
            }
            ScrollPane scrollPane = new ScrollPane(vbox);
            scrollPane.setFitToHeight(true);
            MainBorder.setCenter(scrollPane);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show_MainPage(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/moiare", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from content");
            while(rs.next()){
                HBox hbox = new HBox();
                hbox.setSpacing(10);

                hbox.setPadding(new Insets(20));
                rs.previous();
                for(int i=0; i<3;i++){

                    if(rs.next()) {
                        //image
                        Blob img = rs.getBlob("PlaceImage");
                        InputStream is = img.getBinaryStream();
                        Image image = new Image(is);
                        is.close();
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(200);
                        imageView.setFitHeight(100);
                        //labels
                        String title = rs.getString("PlaceName");
                        Label placeName = new Label(title);
                        String about = rs.getString("PlaceDerscription");
                        Label placeDescription = new Label(about);
                        int pricee = rs.getInt("Price");
                        String pric = Integer.toString(pricee);
                        Label price = new Label("$"+pric);
                        int ID=rs.getInt("PlaceID");
                        //Pane
                        Pane pane = new Pane();
                        pane.setStyle("-fx-background-color: white;");
                        pane.setPrefSize(200,100);
                        //button
                        Button Buybutton = new Button("buy");
                        Buybutton.setStyle(  "-fx-background-color: lightgreen;");
                        Button Updatebutton = new Button("Update");
                        Updatebutton.setStyle(  "-fx-background-color: lightgreen;");
                        Button Deletbutton = new Button("Delet");
                        Deletbutton.setStyle(  "-fx-background-color: red;");

                        //box
                        if(isadmin==true){
                            HBox hboxa = new HBox(Updatebutton,Deletbutton);
                            VBox vbox2 = new VBox(placeName,placeDescription,price,hboxa);
                            vbox2.setPadding(new Insets(20));
                            pane.getChildren().addAll(vbox2);
                            VBox vbox1 = new VBox(imageView, pane);
                            vbox1.setPadding(new Insets(20));
                            hbox.getChildren().addAll(vbox1);
                            Deletbutton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    Connection conn = null;
                                    try {
                                        conn = DriverManager.getConnection("jdbc:mysql://localhost/moiare", "root", "");

                                        PreparedStatement ps=conn.prepareStatement("Delete from content where `PlaceID` = ?");
                                        ps.setInt(1,ID);
                                        ps.execute();
                                        conn.close();
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("1");
                                        alert.setHeaderText("Look, an Information Dialog");
                                        alert.setContentText("Delete Succesfully!");
                                        alert.showAndWait();
                                        show_MainPage();
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                            Updatebutton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    UploadPage(title,about,pric,image);
                                }
                            });
                        }else{
                            VBox vbox2 = new VBox(placeName,placeDescription,price,Buybutton);
                            vbox2.setPadding(new Insets(20));
                            pane.getChildren().addAll(vbox2);
                            VBox vbox1 = new VBox(imageView, pane);
                            vbox1.setPadding(new Insets(20));
                            hbox.getChildren().addAll(vbox1);
                            Buybutton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    Connection conn = null;
                                    try {
                                        conn = DriverManager.getConnection("jdbc:mysql://localhost/moiare", "root", "");

                                        PreparedStatement ps=conn.prepareStatement("insert into request (PlaceID,UserID) values(?,?)");
                                        ps.setInt(1,ID);
                                        ps.setInt(2,userID);
                                        ps.execute();
                                        conn.close();
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("1");
                                        alert.setHeaderText("Look, an Information Dialog");
                                        alert.setContentText("Buy Succesfully!");
                                        alert.showAndWait();
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                        }


                    }
                }

                MainBorder.setCenter(hbox);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void UploadPage(String title, String About,String Price,Image img){VBox mainbox = new VBox();
        mainbox.setPrefSize(100,200);
        mainbox.setPadding(new Insets(20));
        HBox  Titlebox = new HBox();
        HBox  Aboutbox = new HBox();
        HBox  Pricebox = new HBox();
        HBox  Imagebox = new HBox();
        Titlebox.setPrefSize(100,30);

        Label TitleLabel = new Label("Title");
        TextField TitleField = new TextField();
        Label AboutLabel = new Label("About");
        TextField AboutField = new TextField();
        Label PriceLabel = new Label("Price");
        TextField PriceField = new TextField();
        Label ImageLabel = new Label("Image");
        Button ImageButton = new Button("Image");
        ImageView imgView = new ImageView();
        imgView.setFitHeight(100);
        imgView.setFitWidth(150);
        if(title!=null){
            TitleField.setText(title);
            AboutField.setText(About);
            PriceField.setText(Price);
            imgView.setImage(img);
        }
        Button UploadButton = new Button("Upload");
        Titlebox.getChildren().addAll(TitleLabel,TitleField);
        Aboutbox.getChildren().addAll(AboutLabel,AboutField);
        Pricebox.getChildren().addAll(PriceLabel,PriceField);
        Imagebox.getChildren().addAll(ImageLabel,ImageButton,imgView);
        mainbox.getChildren().addAll(Titlebox,Aboutbox,Pricebox,Imagebox,UploadButton);
        HBox.setMargin(TitleField,new Insets(20,0,0,100));
        HBox.setMargin(AboutField,new Insets(20,0,0,89));
        HBox.setMargin(PriceField,new Insets(20,0,0,93));
        HBox.setMargin(ImageButton,new Insets(20,0,0,60));
        HBox.setMargin(imgView,new Insets(30,0,0,60));
        HBox.setMargin(TitleLabel,new Insets(20,0,0,0));
        HBox.setMargin(AboutLabel,new Insets(20,0,0,0));
        HBox.setMargin(PriceLabel,new Insets(20,0,0,0));
        HBox.setMargin(ImageLabel,new Insets(30,0,0,0));
        VBox.setMargin(UploadButton,new Insets(0,0,0,100));
        TitleField.setPrefSize(200,200);
        PriceField.setPrefSize(200,200);
        AboutField.setPrefSize(200,200);
        this.MainBorder.setCenter(mainbox);
        BorderPane.setMargin(mainbox,new Insets(60,0,0,250));

        ImageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fc = new FileChooser();
                FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
                fc.getExtensionFilters().addAll(ext1, ext2);
                Stage primaryStage = (Stage) UploadButton.getScene().getWindow();

                File file = fc.showOpenDialog(primaryStage);

                BufferedImage bf = null;
                try {
                    bf = ImageIO.read(file);
                    Image image = SwingFXUtils.toFXImage(bf, null);
                    imgView.setImage(image);

                    fin = new FileInputStream(file);

                    len = (int) file.length();



                } catch (Exception ek) {
                    ek.printStackTrace();
                }
            }
        });
        UploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");

                    Connection conn = null;

                    conn = DriverManager.getConnection("jdbc:mysql://localhost/moiare", "root", "");

                    PreparedStatement ps = null;

                    ps = conn.prepareStatement("INSERT INTO content(PlaceName, PlaceDerscription,PlaceImage, Price)VALUES(?, ?, ?, ? )");

                    String title=TitleField.getText();
                    String About = AboutField.getText();
                    int price = Integer.parseInt(PriceField.getText());
                    ps.setString(1,title);
                    ps.setString(2,About);
                    ps.setBinaryStream(3, fin, len);
                    ps.setInt(4,price);

                    int status = 0;

                    status = ps.executeUpdate();

                    if (status > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Information dialog");
                        alert.setContentText("Photo saved successfully");
                        alert.showAndWait();

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog.");
                        alert.setHeaderText("Error Information");
                        alert.showAndWait();
                    }
                    conn.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("1");
                    alert.setHeaderText("Look, an Information Dialog");
                    alert.setContentText("Upload Succesfully!");
                    alert.showAndWait();
                    show_MainPage();

                } catch (Exception A) {
                    A.printStackTrace();
                }
            }
        });}
    public void show_UploadPage(){
        UploadPage(null,null,null,null);
    }
    }



