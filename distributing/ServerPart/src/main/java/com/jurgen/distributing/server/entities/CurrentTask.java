package com.jurgen.distributing.server.entities;

import java.net.Socket;

public class CurrentTask {

	private Socket socket;
	private Range range;

	public CurrentTask(Socket socket, Range range) {
		this.socket = socket;
		this.range = range;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

}
