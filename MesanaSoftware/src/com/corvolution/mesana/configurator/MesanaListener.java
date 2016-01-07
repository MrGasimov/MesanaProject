package com.corvolution.mesana.configurator;
import com.corvolution.cm2.connection.ConnectionEvent;
import com.corvolution.cm2.connection.DisconnectionEvent;
import com.corvolution.cm2.connection.SensorListener;
import com.corvolution.mesana.gui.MesanaConfigurator;
import com.corvolution.mesana.gui.MesanaReader;

/**
 * This class represents listener used by Mesana Software Gui's .
 *
 * @author Suleyman Gasimov
 */
public class MesanaListener implements SensorListener
{
	
	private String guiMode;

	/**
	 * The constructor for field initialization.
	 *
	 * @param mode the mode
	 */
	public MesanaListener(String mode)
	{
		guiMode = mode;
	}
	
	/**
	 * This method is run when connection event occurs. 
	 *
	 * @param cEvent the c event
	 */
	@Override
	public void sensorConnection(ConnectionEvent cEvent)
	{
		if (guiMode.equals(Constants.GUI_MODE_READER))
		{
			MesanaReader.connection(cEvent);
		}
		else if(guiMode.equals(Constants.GUI_MODE_CONFIGURATOR))
		{
			MesanaConfigurator.connection(cEvent);
		}

	}
	
	/**
	 * This method is run when disconnection event occurs. 
	 *
	 * @param dEvent the d event
	 */
	@Override
	public void sensorDisconnection(DisconnectionEvent dEvent)
	{
		if (guiMode.equals(Constants.GUI_MODE_READER))
		{
			MesanaReader.disconnection(dEvent);
		}
		else if(guiMode.equals(Constants.GUI_MODE_CONFIGURATOR))
		{
			MesanaConfigurator.disconnection(dEvent);
		}
		
	}
}
