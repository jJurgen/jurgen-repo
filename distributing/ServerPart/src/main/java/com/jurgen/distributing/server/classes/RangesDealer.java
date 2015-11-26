package com.jurgen.distributing.server.classes;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.jurgen.distributing.server.entities.CurrentTask;
import com.jurgen.distributing.server.entities.Range;

public class RangesDealer {

	private Range tempRange = null;
	private int passLength = 1;
	private int[] indexes;
	private long tempVal = 0;
	private long tempMaxVal = 0;
	private int extent = 3;
	private List<CurrentTask> tasks;

	private static String[] symbols = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
			"d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
			"y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z", "`", "~", "!", "@", "#", "$", "¹", ";", "%", "^", "&", "?", "*", "(",
			")", "-", "_", "+", "=", "|", "/", "\\", "{", "}", "'", ",", ".", "<", ">" };

	public RangesDealer() {
		tasks = new ArrayList<>();
		indexes = new int[symbols.length];
		for (int i = 0; i < symbols.length; i++) {
			indexes[i] = i;
		}
	}

	public Range getNewRange(Socket socket) {
		for (CurrentTask ct : tasks) {
			if (ct.getSocket().equals(socket)) {
				tasks.remove(ct);
				break;
			}
		}

		for (CurrentTask ct : tasks) {
			if (ct.getSocket().isClosed()) {
				Range range = ct.getRange();
				tasks.remove(ct);
				tasks.add(new CurrentTask(socket, range));
				return range;
			}
		}

		Range range = getNextRange();
		tasks.add(new CurrentTask(socket, range));
		return range;
	}

	public Range getNextRange() {
		int[] startIndexes = new int[passLength];
		if ((tempRange == null) || (tempRange.getStart().length() < passLength)) {
			tempMaxVal = (long) Math.pow(symbols.length, passLength);
			for (int i = 0; i < passLength; i++) {
				startIndexes[i] = 0;
			}
			tempRange = new Range(getPassword(startIndexes), getPassword(getFinishIndexes(extent)));
		} else {
			tempRange.setStart(tempRange.getFinish());
			tempRange.setFinish(getPassword(getFinishIndexes(extent)));
		}
		return tempRange;
	}

	private int[] getFinishIndexes(int k) {
		int[] finish = new int[passLength];
		long step = (long) Math.pow(symbols.length, k);
		if (tempVal + step < tempMaxVal) {
			long tmp;
			long finishNum = tmp = tempVal + step;
			for (int i = passLength - 1; i >= 0; i--) {
				long val = (long) (tmp / Math.pow(symbols.length, i));
				tmp %= Math.pow(symbols.length, i);
				finish[passLength - i - 1] = (int) val;
			}
			tempVal = finishNum;
		} else {
			for (int j = 0; j < passLength; j++) {
				finish[j] = symbols.length - 1;
				tempVal = 0;
				tempMaxVal = 0;
			}
			passLength++;
		}
		return finish;
	}

	private String getPassword(int[] arr) {
		String password = "";
		for (int i = 0; i < arr.length; i++) {
			password += symbols[arr[i]];
		}
		return password;
	}

}
