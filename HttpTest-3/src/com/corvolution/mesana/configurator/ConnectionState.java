package com.corvolution.mesana.configurator;

public class ConnectionState {
	public static boolean state;
	
	public static  void setTrueState(){
		state = true;
	}
	
	public static void setFalseState(){
		state = false;
	}
	
	public static boolean getState(){
		return state;
	}
	
}
