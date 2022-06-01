import serverUI.ServerMainFrame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 8100;

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                //socket.setSoTimeout(10000);
                new ClientThread(socket, serverSocket).start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new ServerMainFrame().setVisible(true);
        new Server ();
    }

}
