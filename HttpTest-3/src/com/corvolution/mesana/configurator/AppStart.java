package com.corvolution.mesana.configurator;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import gui.GuiBuilder;
import gui.OperatorAccess;


public class AppStart {
	
	
	public static void main(String[] args) throws MalformedURLException, IOException{
		
		Thread thread = new Thread(new FindSensor());
		thread.start();
				
		OperatorAccess opAccess = new OperatorAccess();
		new GuiBuilder(opAccess.getLogin(),opAccess.getPassword());
		
		//Measurement m = new Measurement();
		//m.putMethod("SENSOR_OUTBOX", "test");
		
		
		
	}
	
}
	

