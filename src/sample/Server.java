package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application {
    double area;
    double radius;
    private int port = 8000;
    DataInputStream in = null;
    DataOutputStream out = null;
    ServerSocket server;
    Socket socket;
    @Override
    public void start(Stage stage) throws Exception {
        TextArea ta = new TextArea();
        Scene scene = new Scene(new javafx.scene.control.ScrollPane(ta), 450, 200);
        stage.setScene(scene);
        stage.show();

        new Thread(() -> {
            try {
                server = new ServerSocket(port);
                socket = server.accept();
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    radius = in.readDouble();
                    area = radius*radius*Math.PI;
                    out.writeDouble(area);

                    Platform.runLater(() -> {
                        ta.appendText("Radius recieved from the client: " + radius + "\n");
                        ta.appendText("Area is: " + area + "\n");
                        ta.appendText("\n");
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }).start();


    }
}
