
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends JFrame {
    private String username;
    private PrintWriter out;
    private BufferedReader in;
    private final JTextField name = new JTextField(20);
    private final JTextArea textMessage = new JTextArea(5, 20);
    private final JPanel userPanel = new JPanel();
    private final JLabel userLabel = new JLabel();
    private GridBagConstraints horizontal;
    private GridBagConstraints vertical;
    private final JPanel panel = new JPanel();
    private final JFrame user = new JFrame();
    private JFrame messagesFrame = new JFrame();
    private JLabel messagesLabel = new JLabel();
    private ArrayList<String> messages = new ArrayList();

    public Client() {
        super("Client");
        setVisible(true);
        connectToServer();
        init();
    }

    private void connectToServer() {
        try {
            String serverAddress = "127.0.0.1";
            int PORT = 8100;
            Socket socket = new Socket(serverAddress, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension minBtnSize = new Dimension(500, 500);
        setMinimumSize(minBtnSize);

        setLayout(new GridBagLayout());

        int margin = 5;

        horizontal = new GridBagConstraints();
        horizontal.insets = new Insets(margin, margin, margin, margin);
        //cPanel.gridwidth = GridBagConstraints.REMAINDER;
        horizontal.fill = GridBagConstraints.NONE;

        vertical = new GridBagConstraints();
        vertical.insets = new Insets(margin, margin, margin, margin);
        vertical.gridwidth = GridBagConstraints.REMAINDER;
        vertical.fill = GridBagConstraints.NONE;


        //login, register, add friend zone
        createUserPanel();
        add(userPanel);

        createMessagesFrame();

        //message zone
//        JButton logOutBtn = new JButton("Log out");
//        logOutBtn.addActionListener(this::logOut);
//        panel.add(logOutBtn, c);

//        JButton stopServerBtn = new JButton("Stop server");
//        stopServerBtn.addActionListener(this::stopServer);
//        panel.add(stopServerBtn, c);

        pack();
    }


    private void createMessagesFrame() {
        messagesFrame.setMinimumSize(new Dimension(500, 300));
        messagesFrame.setLayout(new BorderLayout());
        messagesFrame.add(messagesLabel, BorderLayout.CENTER);
        JButton next = new JButton("Next");
        next.setSize(new Dimension(50, 20));
        next.addActionListener(this::getNextMessage);
        messagesFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
        messagesFrame.add(next, BorderLayout.AFTER_LAST_LINE);
    }

    private void getNextMessage(ActionEvent actionEvent) {
        if(messages.size() == 1) {
            messagesFrame.dispose();
        }
        else{
            messages.remove(0);
            messagesLabel.setText(messages.get(0));
        }
    }

    private void createUserPanel() {
        userPanel.setLayout(new GridBagLayout());
        JButton loginRegisterBtn = new JButton("Login/Register");
        loginRegisterBtn.addActionListener(this::createLoginRegisterWindow);
        loginRegisterBtn.setBounds(10, 10, 10, 100);
        userPanel.add(loginRegisterBtn);
    }


    private void createLoginRegisterWindow(ActionEvent actionEvent) {
        user.setLayout(new GridBagLayout());
        user.setMinimumSize(new Dimension(100, 50));

        user.add(name, vertical);

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(this::register);
        user.add(registerBtn, horizontal);

        JButton loginBtn = new JButton("Log in");
        loginBtn.addActionListener(this::logIn);
        user.add(loginBtn, horizontal);

        user.pack();
        user.setVisible(true);
    }

    private void read(ActionEvent actionEvent) {
        out.println("read");

        messages = new ArrayList<>();

        String response;
        try {
            response = in.readLine();
            System.out.println(response);

            response = in.readLine();

            if(response.equals("done"))
                System.out.println("No messages!");
            else {
                messagesFrame.setVisible(true);
                messagesLabel.setText(response);
                while(!response.equals("done")) {
                    messages.add(response);
                    System.out.println(response);
                    out.println("ok");
                    response = in.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(ActionEvent actionEvent)  {
        out.println("send " + textMessage.getText());
        textMessage.removeAll();

        String response;
        try {
            response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFriend(ActionEvent actionEvent) {
        out.println("friend " + name.getText());
        name.setText("");

        String response;
        try {
            response = in.readLine();
            System.out.println(response);

            response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logIn(ActionEvent actionEvent) {
        out.println("login " + name.getText());

        String response;
        try {
            response = in.readLine();
            System.out.println(response);
            username = name.getText();
            name.setText("");
            setLoggedIn();
            user.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(ActionEvent actionEvent) {
        out.println("register " + name.getText());

        String response;
        try {
            response = in.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        username = name.getName();
        setLoggedIn();
        name.setText("");
        user.dispose();

    }

    private void setLoggedIn() {
        userPanel.removeAll();
        userPanel.updateUI();

        GridBagConstraints moreVerticalSpace = (GridBagConstraints) vertical.clone();
        moreVerticalSpace.insets = new Insets(15, 5, 15, 5);

        userLabel.setText("Hello, " + username);
        add(userLabel, moreVerticalSpace);


        JPanel addFriendPanel = new JPanel();

        addFriendPanel.add(name, vertical);

        JButton addFriendBtn = new JButton("Add friend");
        addFriendBtn.addActionListener(this::addFriend);
        addFriendPanel.add(addFriendBtn, vertical);

        add(addFriendPanel, moreVerticalSpace);


        JPanel message = new JPanel();
        message.setLayout(new GridBagLayout());

        message.add(textMessage, vertical);

        JButton sendMessageBtn = new JButton("Send message");
        sendMessageBtn.addActionListener(this::send);
        message.add(sendMessageBtn, horizontal);

        JButton readBtn = new JButton("Read messages");
        readBtn.addActionListener(this::read);
        message.add(readBtn, horizontal);

        add(message, moreVerticalSpace);
    }
}
