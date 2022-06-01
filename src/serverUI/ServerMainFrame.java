package serverUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class ServerMainFrame extends JFrame {
    public ServerMainFrame() {
        super("Server");
        setMinimumSize(new Dimension(500, 500));
        init();
    }

    public void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int margin = 10;
        
        GridBagConstraints vertical = new GridBagConstraints();
        vertical.insets = new Insets(margin, margin, margin, margin);
        vertical.gridwidth = GridBagConstraints.REMAINDER;
        vertical.fill = GridBagConstraints.NONE;

        setLayout(new GridBagLayout());

        JButton newUserBtn = new JButton("New user");
        newUserBtn.addActionListener(this::newUser);
        add(newUserBtn, vertical);

        pack();
    }

    private void newUser(ActionEvent e) {
        System.out.println("new user");
        try {
            Class c = Class.forName("Client");
            Constructor constructor = c.getConstructor();
            constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }
}
