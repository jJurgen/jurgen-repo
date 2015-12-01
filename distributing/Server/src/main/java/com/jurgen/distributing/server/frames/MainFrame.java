package com.jurgen.distributing.server.frames;

import com.jurgen.distributing.server.classes.ServerManager;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;


import java.awt.Color;
import java.awt.Font;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class MainFrame extends JFrame {
	// controlls
	private JPanel contentPane;
	private JTextArea jTextArea_Log;
	private JMenuItem jMenuItem_SelectFile;
	private JButton jButton_StartServer;
	private JLabel jLabel_CorrectPassword;
	private JMenuItem jMenuItem_Exit;
	// variables
	private String mainFolder;
	private ServerManager serverManager;
	private String zipFilePath = "";
	private boolean isFound = false;

	public MainFrame(String title, String mainFolder) {
		this.mainFolder = mainFolder;
		prepareFolder();
		setTitle(title);
		initComponents();
		setVisible(true);
	}

	private void prepareFolder() {
		new File(mainFolder).mkdirs();
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 370);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		jMenuItem_SelectFile = new JMenuItem("Select file");
		jMenuItem_SelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jMenuItem_SelectedFile_ActionPerformed(evt);
			}
		});
		jMenuItem_SelectFile.setHorizontalAlignment(SwingConstants.LEFT);
		mnNewMenu.add(jMenuItem_SelectFile);
		
		jMenuItem_Exit = new JMenuItem("Exit");
		jMenuItem_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jMenuItem_Exit_ActionPerformed(evt);
			}
		});
		mnNewMenu.add(jMenuItem_Exit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(192, 192, 192), 2, true));
		scrollPane.setBounds(0, 100, 524, 242);
		contentPane.add(scrollPane);

		jTextArea_Log = new JTextArea();
		DefaultCaret caret = (DefaultCaret) jTextArea_Log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jTextArea_Log.setEditable(false);
		jTextArea_Log.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setViewportView(jTextArea_Log);

		JLabel jLabel_Log = new JLabel("Log of actions:");
		jLabel_Log.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		jLabel_Log.setBounds(10, 81, 89, 18);
		contentPane.add(jLabel_Log);

		jButton_StartServer = new JButton("Start server");
		jButton_StartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton_StartServer_ActionPerformed(evt);
			}
		});
		jButton_StartServer.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		jButton_StartServer.setBounds(0, 45, 110, 25);
		contentPane.add(jButton_StartServer);

		jLabel_CorrectPassword = new JLabel("Correct password:");
		jLabel_CorrectPassword.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		jLabel_CorrectPassword.setBounds(10, 11, 240, 23);
		contentPane.add(jLabel_CorrectPassword);
		jLabel_CorrectPassword.setVisible(false);
	}

	private void jMenuItem_SelectedFile_ActionPerformed(ActionEvent evt) {
		JFileChooser fileChooser = new JFileChooser();
		String ext = ".zip";
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(0);
		int ret = fileChooser.showDialog(null, "Select zip-archive");
		if (ret == 0) {
			int index = fileChooser.getSelectedFile().getAbsolutePath().lastIndexOf(".");
			String fileExt = fileChooser.getSelectedFile().getAbsolutePath().substring(index);
			if (ext.equals(fileExt)) {
				zipFilePath = fileChooser.getSelectedFile().getAbsolutePath();
				addToLog("ZIP-File: " + zipFilePath);
			} else {
				JOptionPane.showMessageDialog(null, "Please select correct archive(.zip)");
			}
		}
	}
	
	private void jMenuItem_Exit_ActionPerformed(ActionEvent evt){
		System.exit(0);
	}

	private void jButton_StartServer_ActionPerformed(ActionEvent evt) {
		if (zipFilePath.equals("")) {
			JOptionPane.showMessageDialog(null, "Please select archive(.zip)");
		} else {
			serverManager.startClientsSearching();
			jButton_StartServer.setVisible(false);
		}
	}

	public synchronized void addToLog(String message) {
		LocalTime time = LocalTime.now();
		jTextArea_Log.append(time.toString() + " :: " + message + "\n");
	}

	public void setAnswer(String password) {
		jLabel_CorrectPassword.setText("Correct password:" + password);
		jLabel_CorrectPassword.setVisible(true);
	}

	public String getMainFolder() {
		return mainFolder;
	}

	public void setMainFolder(String mainFolder) {
		this.mainFolder = mainFolder;
	}

	public ServerManager getServerManager() {
		return serverManager;
	}

	public void setServerManager(ServerManager servermanager) {
		this.serverManager = servermanager;
	}

	public String getZipFilePath() {
		return zipFilePath;
	}

	public void setZipFilePath(String zipFilePath) {
		this.zipFilePath = zipFilePath;
	}

	public boolean isFound() {
		return isFound;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}
}
