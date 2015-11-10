package com.corvolution.cm2;
import java.util.EventObject;


public class SensorEvent extends EventObject {
	
	private boolean state;
	private String path;
	public SensorEvent(Object source, boolean state, String path) {
		super(source);
		this.state = state;
		this.path = path;
		// TODO Auto-generated constructor stub
	}
	
	public boolean getState(){
		return state;
	}
	
	public String getSensorPath(){
		return path;
	}
}
