package com.corvolution.mesana;

import com.corvolution.cm2.connection.ConnectionManager;
import com.corvolution.cm2.connection.SensorNotifier;
import com.corvolution.mesana.gui.MesanaConfigurator;
import com.corvolution.mesana.gui.MesanaReader;
import com.corvolution.mesana.utility.Constants;
import com.corvolution.mesana.utility.MesanaListener;
import com.corvolution.mesana.utility.PropertyManager;

/**
 * This is a main class.Entry for the application
 */
public class MainClass
{

	/**
	 * The main method.Execution of the application
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{

		Thread usbListener = new Thread(new SensorNotifier());
		usbListener.start();
		ConnectionManager.getInstance().addSensorListener(
				new MesanaListener(PropertyManager.getInstance().getProperty(Constants.GUI_MODE)),
				ConnectionManager.CONNECTION);
		ConnectionManager.getInstance().addSensorListener(
				new MesanaListener(PropertyManager.getInstance().getProperty(Constants.GUI_MODE)),
				ConnectionManager.DISCONNECTION);

		if (PropertyManager.getInstance().getProperty("GUI_MODE").equals(Constants.GUI_MODE_CONFIGURATOR))
		{
			new MesanaConfigurator();
		}
		else if (PropertyManager.getInstance().getProperty("GUI_MODE").equals(Constants.GUI_MODE_READER))
		{
			new MesanaReader();
		}

	}

}
