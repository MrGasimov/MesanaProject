package com.corvolution.mesana.configurator;
import com.corvolution.cm2.ConnectionManager;
import com.corvolution.cm2.UsbListener;
import com.corvolution.mesana.gui.ConfigGui;
import com.corvolution.mesana.gui.OperatorAccess;
import com.corvolution.mesana.gui.ReaderGui;


public class MainClass {

	public static void main(String[] args) {
		
		Thread usbListener = new Thread(new UsbListener());
		usbListener.start();
		PropertyManager pManager = new PropertyManager();
		OperatorAccess opAccess = new OperatorAccess();				
		ConnectionManager.getInstance().addSensorListener(new GuiUpdater(pManager.getProperty("GUI_MODE")));
		if(pManager.getProperty("GUI_MODE").equals("CONFIGURATOR")){
			new ConfigGui(opAccess.getLogin(),opAccess.getPassword()); 
		}else{
			new ReaderGui(opAccess.getLogin(),opAccess.getPassword());
		}
	}

}