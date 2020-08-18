package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MultiThreadedServer extends Application {
    double area;
    double radius;
    private int port = 8000;
    DataInputStream in = null;
    DataOutputStream out = null;
    ServerSocket server;
    Socket socket;
    int clientNo = 0;
    TextArea ta;
    @Override
    public void start(Stage stage) throws Exception {
        ta = new TextArea();
        Scene scene = new Scene(new ScrollPane(ta),450, 200);
        stage.setScene(scene);
        stage.show();

        new Thread(() -> {
           try {
               server = new ServerSocket(port);
               ta.appendText("MultiThreadedServer started at " + new Date() + "\n");

               while (true) {
                   socket = server.accept();
                   clientNo++;

                   Platform.runLater(() -> {
                       ta.appendText("Starting thread for client " + clientNo + " at " + new Date() + "\n");
                       InetAddress inetAddress = socket.getInetAddress();

                       ta.appendText("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
                       ta.appendText("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");
                   });
                   new Thread(new HandleAClient(socket)).start();

               }
           } catch (IOException e) {
               e.printStackTrace();
           }
        }).start();

    }
    class HandleAClient implements Runnable {
        private Socket socket;

        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    double radius = inputStream.readDouble();
                    double area = radius * radius * Math.PI;

                    outputStream.writeDouble(area);

                    Platform.runLater(() -> {
                        ta.appendText("radius recieved from client: " + radius + "\n");
                        ta.appendText("area found: " + area + "\n");
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
