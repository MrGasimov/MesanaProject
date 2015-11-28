package com.corvolution.mesana.configurator;

import org.json.simple.JSONObject;

import com.corvolution.cm2.connection.ConnectionManager;
import com.corvolution.cm2.connection.SensorNotifier;
import com.corvolution.mesana.gui.MesanaConfigurator;
import com.corvolution.mesana.gui.OperatorAccess;
import com.corvolution.mesana.gui.ReaderGui;
import com.corvolution.mesana.rest.RestApiConnector;

public class MainClass
{

	public static void main(String[] args)
	{

		Thread usbListener = new Thread(new SensorNotifier());
		usbListener.start();
		ConnectionManager.getInstance().addSensorListener(new GuiUpdater(PropertyManager.getInstance().getProperty(Constants.GUI_MODE)), ConnectionManager.CONNECTION);
		ConnectionManager.getInstance().addSensorListener(new GuiUpdater(PropertyManager.getInstance().getProperty(Constants.GUI_MODE)), ConnectionManager.DISCONNECTION);
		
		if (PropertyManager.getInstance().getProperty("GUI_MODE").equals("CONFIGURATOR"))
		{
			new MesanaConfigurator();
		}else
		{
			new ReaderGui();
		}

	}

}
