import networkDatabase.Database;
import networkDatabase.entities.Friendship;
import networkDatabase.entities.Message;
import networkDatabase.entities.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.List;

@SuppressWarnings({"ThrowablePrintedToSystemOut", "InfiniteLoopStatement"})
public class ClientThread extends Thread{
    private final Socket socket;
    private final ServerSocket serverSocket;
    private String username;
    private final Connection con;

    public ClientThread(Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        con = Database.getConnection();
    }

    public void run() {
        try{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            while (true) {
                String request = in.readLine();
                String raspuns;

                if (request.equals("stop")) {
                    raspuns = "Server stopped";
                    out.println(raspuns);
                } else {
                    raspuns = "Server received the request " + request + "!";
                    out.println(raspuns);
                    out.flush();
                    if (request.contains("register ")) {
                        int startIndex = request.indexOf("register ") + "register ".length();
                        System.out.println(register(request.substring(startIndex)));
                    } else if (request.contains("login ")) {
                        System.out.println("Login client!");
                        int startIndex = request.indexOf("login ") + "login ".length();
                        login(request.substring(startIndex));
                    } else if (request.contains("friend ")) {
                        System.out.println("Add friends!");
                        int startIndex = request.indexOf("friend ") + "friend ".length();
                        out.println(friend(request.substring(startIndex)));
                    } else if (request.contains("send ")) {
                        System.out.println("Send message");
                        int startIndex = request.indexOf("send ") + "send ".length();
                        sendMessage(request.substring(startIndex));
                    } else if (request.contains("read")) {
                        System.out.println("Read message!");

                        List<String> result = read();

                        for (String message: result) {
                            out.println(message);
                            out.flush();
                            in.readLine();
                        }
                        out.println("done");
                    }
                }
                out.flush();


                if (request.equals("stop")) {
                    serverSocket.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public String register(String username) {
        System.out.println("Register client!");
        if (Person.userExists(username))
            return "username already exists!";

        Person.create(username);
        this.username = username;
        return "User created!";
    }

    public void login(String username) {
        if (Person.userExists(username))
            this.username = username;
    }

    public String friend(String username) {
        try{
            Friendship.create(Person.getIdByName(this.username), Person.getIdByName(username));
        }  catch (SQLException e) {
            if (e.getMessage().contains("duplicate key"))
                return "Already friends";
            e.printStackTrace();
        }
        return "Friend added";
    }

    public void sendMessage(String message) {
        List<Integer> idFriends = Friendship.getAllFriendsId(Person.getIdByName(this.username));

        for (Integer id: idFriends) {
            Message.create(message, id);
        }
    }

    public List<String> read() {
        return Message.getMessagesFor(Person.getIdByName(this.username));
    }
}
