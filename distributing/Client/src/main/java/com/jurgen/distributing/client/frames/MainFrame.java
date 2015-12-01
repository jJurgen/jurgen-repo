package com.jurgen.distributing.client.frames;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import com.jurgen.distributing.client.classes.ConnectionHandler;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame {

    // components
    private JPanel contentPane;
    private JTextArea jTextArea_Log;
    private JTextField jTextField_PORT;
    private JTextField jTextField_IP;
    private JButton jButton_Confirm;
    private JLabel jLabel_PORT;
    private JLabel jLabel_IP;
    // variables
    private String mainFolder;
    private ConnectionHandler connectionHandler;

    public MainFrame(String mainFolder) {
        this.mainFolder = mainFolder;
        prepareFolder();
        initcomponents();
        setVisible(true);
    }

    private void prepareFolder() {
        new File(mainFolder).mkdirs();
    }

    private void initcomponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setResizable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 90, 444, 182);
        contentPane.add(scrollPane);

        jTextArea_Log = new JTextArea();
        DefaultCaret caret = (DefaultCaret) jTextArea_Log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jTextArea_Log.setFont(new Font("Tahoma", Font.PLAIN, 15));
        jTextArea_Log.setEditable(false);
        scrollPane.setViewportView(jTextArea_Log);

        JLabel jLabel_Log = new JLabel("Log of actions:");
        jLabel_Log.setFont(new Font("Tahoma", Font.PLAIN, 16));
        jLabel_Log.setBounds(10, 65, 105, 24);
        contentPane.add(jLabel_Log);

        jTextField_PORT = new JTextField();
        jTextField_PORT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jTextField_PORT.setText("5533");
        jTextField_PORT.setBounds(310, 10, 100, 20);
        contentPane.add(jTextField_PORT);
        jTextField_PORT.setColumns(10);

        jTextField_IP = new JTextField();
        jTextField_IP.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jTextField_IP.setText("localhost");
        jTextField_IP.setBounds(310, 35, 100, 20);
        contentPane.add(jTextField_IP);
        jTextField_IP.setColumns(10);

        jLabel_PORT = new JLabel("PORT:");
        jLabel_PORT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jLabel_PORT.setBounds(260, 8, 40, 20);
        contentPane.add(jLabel_PORT);

        jLabel_IP = new JLabel("IP:");
        jLabel_IP.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jLabel_IP.setBounds(283, 33, 17, 20);
        contentPane.add(jLabel_IP);

        jButton_Confirm = new JButton("Confirm");
        jButton_Confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton_Confirm_ActionPerformed(evt);
            }
        });
        jButton_Confirm.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jButton_Confirm.setBounds(310, 60, 100, 23);
        contentPane.add(jButton_Confirm);

        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                connectionHandler.stopHandler();
                delete(new File(mainFolder));
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }
        });
    }

    public void addToLog(String message) {
        LocalTime time = LocalTime.now();
        jTextArea_Log.append(time.toString() + " :: " + message + "\n");
    }

    private void jButton_Confirm_ActionPerformed(ActionEvent evt) {
        try {
            String ip = jTextField_IP.getText();
            int port = Integer.parseInt(jTextField_PORT.getText());
            if (connectionHandler.tryConnecting(port, ip)) {
                connectionHandler.start();
                jLabel_IP.setVisible(false);
                jLabel_PORT.setVisible(false);
                jTextField_IP.setVisible(false);
                jTextField_PORT.setVisible(false);
                jButton_Confirm.setVisible(false);
            }
        } catch (NumberFormatException e) {
            addToLog("Error: " + e.getMessage());
        }
    }

    public void delete(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                this.delete(f);
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    public String getMainFolder() {
        return mainFolder;
    }

    public void setMainFolder(String mainFolder) {
        this.mainFolder = mainFolder;
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

}
