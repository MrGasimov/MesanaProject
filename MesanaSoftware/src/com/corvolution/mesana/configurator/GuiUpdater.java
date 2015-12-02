package com.corvolution.mesana.configurator;

import com.corvolution.cm2.connection.ConnectionEvent;
import com.corvolution.cm2.connection.DisconnectionEvent;
import com.corvolution.cm2.connection.SensorListener;
import com.corvolution.mesana.gui.MesanaConfigurator;
import com.corvolution.mesana.gui.ReaderGui;

public class GuiUpdater implements SensorListener
{
	String guiMode;

	public GuiUpdater(String mode)
	{
		guiMode = mode;
	}

	@Override
	public void sensorConnection(ConnectionEvent cEvent)
	{
		if (guiMode.equals("READER"))
		{
			ReaderGui.connection(cEvent);
		}
		else
		{
			MesanaConfigurator.connection(cEvent);
		}

	}

	@Override
	public void sensorDisconnection(DisconnectionEvent dEvent)
	{
		if (guiMode.equals("READER"))
		{
			ReaderGui.disconnection(dEvent);
		}
		else
		{
			MesanaConfigurator.disconnection(dEvent);
		}
		
	}
}
