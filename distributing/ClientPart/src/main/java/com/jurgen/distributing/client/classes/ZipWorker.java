package com.jurgen.distributing.client.classes;

import java.io.File;
import java.util.Arrays;

import com.jurgen.distributing.client.frames.MainFrame;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ZipWorker {

	private MainFrame mainFrame;
	private static final String garbageFolder = "\\garbage\\";
	private int[] symbolIndexes;
	private String password = "";
	private ZipFile zFile = null;

	private static String[] symbols = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
			"d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
			"y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z", "`", "~", "!", "@", "#", "$", "№", ";", "%", "^", "&", "?", "*", "(",
			")", "-", "_", "+", "=", "|", "/", "\\", "{", "}", "'", ",", ".", "<", ">" };

	public ZipWorker() {
		symbolIndexes = new int[symbols.length];
		for (int i = 0; i < symbols.length; i++) {
			symbolIndexes[i] = i;
		}
	}

	public boolean SearchPassword(String start, String finish) {
		int[] startIndexes = getIndexes(start);
		int[] finishIndexes = getIndexes(finish);
		int[] tempPassword = startIndexes;
		if (this.extract(getPassword(tempPassword))) {
			this.password = getPassword(tempPassword);
			delete(new File(mainFrame.getMainFolder().concat(garbageFolder)));
			return true;
		}
		int i = tempPassword.length - 1;
		while (!Arrays.equals(tempPassword, finishIndexes)) {
			if (tempPassword[i] != symbolIndexes[symbolIndexes.length - 1]) {
				tempPassword[i]++;
				if (this.extract(getPassword(tempPassword))) {
					this.password = getPassword(tempPassword);
					delete(new File(mainFrame.getMainFolder().concat(garbageFolder)));
					return true;
				}
				if (i == tempPassword.length - 1) {
					continue;
				}
				i = tempPassword.length - 1;
			} else {
				tempPassword[i] = symbolIndexes[0];
				i--;
			}
		}
		delete(new File(mainFrame.getMainFolder().concat(garbageFolder)));
		return false;
	}

	private boolean extract(String pass) {
		try {
			zFile.setPassword(pass);
			zFile.extractAll(mainFrame.getMainFolder().concat(garbageFolder));
			return true;
		} catch (ZipException e) {
			return false;
		}
	}

	private int[] getIndexes(String str) {
		int[] indexes = new int[str.length()];
		for (int i = 0; i < str.length(); i++) {
			for (int j = 0; j < symbols.length; j++) {
				if (str.charAt(i) == symbols[j].toCharArray()[0]) {
					indexes[i] = j;
				}
			}
		}
		return indexes;
	}

	private String getPassword(int[] arr) {
		String password = "";
		for (int i = 0; i < arr.length; i++) {
			password += symbols[arr[i]];
		}
		return password;
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
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public ZipFile getzFile() {
		return zFile;
	}

	public void setzFile(ZipFile zFile) {
		this.zFile = zFile;
	}

	public String getPassword() {
		return password;
	}

}
