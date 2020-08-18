package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends Application {
    private int port = 8000;
    private String host = "localhost";
    DataInputStream in;
    DataOutputStream out;
    Socket socket;
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5, 5, 5,5));
        borderPane.setStyle("-fx-border-color: green");
        borderPane.setLeft(new Label("Enter a radius: "));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        borderPane.setCenter(tf);

        BorderPane mainPane = new BorderPane();
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(borderPane);

        Scene scene = new Scene(mainPane, 450, 200);
        stage.setScene(scene);
        stage.setTitle("CLIENT 1");
        stage.show();

        tf.setOnAction(e -> {
            try {
                double radius = Double.parseDouble(tf.getText().trim());

                out.writeDouble(radius);
                out.flush();

                double area = in.readDouble();

                ta.appendText("Radius is: " + radius + "\n");
                ta.appendText("Area recieved from the server is: " + area + "\n");
                ta.appendText("\n");
            }catch (IOException ex) {
                System.out.println(ex);

            }
        });

        try {
            Socket socket = new Socket(host, port);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            ta.appendText(e.toString() + "\n");
        }



    }

}
