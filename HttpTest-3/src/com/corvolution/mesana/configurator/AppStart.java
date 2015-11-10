package com.corvolution.mesana.configurator;
import com.corvolution.cm2.ConnectionListener;
import com.corvolution.mesana.gui.GuiBuilder;
import com.corvolution.mesana.gui.OperatorAccess;



public class AppStart {

	public static void main(String[] args) throws Exception 
	{	
		Thread sensorListener = new Thread(new ConnectionListener());
		sensorListener.start();		
		OperatorAccess opAccess = new OperatorAccess();
		new GuiBuilder(opAccess.getLogin(), opAccess.getPassword());		
		
		
	}
}
