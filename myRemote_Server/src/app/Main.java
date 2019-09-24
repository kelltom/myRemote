package app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Main extends Application {

    // declare controls
    private Image logo;
    private ImageView ivLogo;
    private Label lblAddress;
    private TextField txtAddress;
    private Label lblPort;
    private TextField txtPort;
    private Label lblCS;
    private Label lblStatus;
    private Button btnDisconnect;

    // themes
    private static final String BACKGROUND = "#60A0D6";

    // constants
    private static final String CONNECTED = "Connected";
    private static final String DISCONNECTED = "Disconnected";

    @Override
    public void start(Stage primaryStage) {

        /* ----- Set the stage ----- */

        primaryStage.setTitle("myRemote Server");
        primaryStage.setResizable(false);


        /* ----- Set the controls ----- */

        // Logo ImageView
        logo = new Image("/resources/logo.png");
        ivLogo = new ImageView(logo);

        // IP Address Label
        lblAddress = new Label("IP Address: ");

        // IP Address TextView
        txtAddress = new TextField();
        txtAddress.setEditable(false);

        // Port Label wrapped in HBox root
        lblPort = new Label("Port: ");

        // Port Number TextView
        txtPort = new TextField();
        txtPort.setEditable(false);

        // Connection Status Labels wrapped in HBox layout (horizontal)
        lblCS = new Label("Connection Status: ");
        lblCS.setTextFill(Color.WHITE);
        lblStatus = new Label(/* will contain status of connection */);
        HBox hbStatusLabels = new HBox();
        hbStatusLabels.setPadding(new Insets(10,0,15,0));
        hbStatusLabels.getChildren().addAll(lblCS, lblStatus);

        // Disconnect Button
        btnDisconnect = new Button("Disconnect");
        btnDisconnect.setPrefWidth(Double.MAX_VALUE);
        btnDisconnect.setTextFill(Color.DARKRED);

        /* ----- Set the scene ----- */

        // Place all controls in a VBox layout (vertical)
        VBox controls = new VBox();
        controls.getChildren().addAll(
                lblAddress,
                txtAddress,
                lblPort,
                txtPort,
                hbStatusLabels,
                btnDisconnect
        );
        controls.setSpacing(4);
        controls.setPadding(new Insets(10,30,10,30));

        // Place VBox in the centre of a BorderPane root
        BorderPane root = new BorderPane();
        root.setTop(ivLogo);
        root.setCenter(controls);
        root.setPadding(new Insets(0,100,5,100));
        root.setStyle("-fx-background-color: " + BACKGROUND + ";");

        Scene scene = new Scene(root, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();


        /* -------------------- BEGIN RUNNING SERVER -------------------- */

        // create controller - a class containing code to handle client input
        Controller controller = new Controller();

        // get server info and display to user
        String[] info = controller.getConnectionInfo();
        txtAddress.setText(info[0]);
        txtPort.setText(info[1]);

        // start server - must run on new Thread to avoid blocking UI Thread
        Thread worker = new Thread(controller);
        worker.start();

        // update lblStatus every 5 seconds - terminate app when socket connection closes
        Timeline updateStatus = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (worker.isAlive()) {
                if (controller.getStatus()) {
                    lblStatus.setText(CONNECTED);
                    lblStatus.setTextFill(Color.LIGHTGREEN);
                } else {
                    lblStatus.setText(DISCONNECTED);
                    lblStatus.setTextFill(Color.DARKRED);
                }
            } else {
                controller.closeSockets();
                System.exit(0);
            }
        }));
        updateStatus.setCycleCount(Timeline.INDEFINITE);
        updateStatus.play();

        // overrides the exit button to terminate the application rather than minimizing it to system tray
        primaryStage.setOnHiding(event -> Platform.runLater(() -> {
            controller.closeSockets();
            System.exit(0);
        }));

        /* ----- Set Disconnect Button Listener ----- */
        btnDisconnect.setOnAction(event -> {
            controller.closeSockets();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        // method within the Application class to launch program as a JavaFX application
        launch(args);
    }
}
