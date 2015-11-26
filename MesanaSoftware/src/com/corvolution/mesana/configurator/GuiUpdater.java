package com.corvolution.mesana.configurator;

import com.corvolution.cm2.connection.SensorEvent;
import com.corvolution.cm2.connection.SensorListener;
import com.corvolution.mesana.gui.ConfigGui;
import com.corvolution.mesana.gui.ReaderGui;

public class GuiUpdater implements SensorListener
{
	String guiMode;

	public GuiUpdater(String mode)
	{
		guiMode = mode;
	}

	@Override
	public void sensorConnection(SensorEvent e)
	{
		if (guiMode.equals("READER"))
		{
			ReaderGui.update(e);
		}
		else
		{
			ConfigGui.update(e);
		}

	}
}
