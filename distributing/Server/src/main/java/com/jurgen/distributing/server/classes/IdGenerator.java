package com.jurgen.distributing.server.classes;

public class IdGenerator {
	private Integer currentId= 0;
	
	public IdGenerator(){		
	}

	public Integer getCurrentId() {
		return ++currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}
	
}
