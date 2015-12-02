package com.corvolution.cm2.connection;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

import com.corvolution.cm2.Constants;

public class SensorNotifier implements Runnable
{

	public void run()
	{
		sensorListener();
	}

	public void sensorListener()
	{
		int nConnectedSensors = 0;
		String[] letters = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "O", "P", "Q",
				"R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		File[] sensors = new File[letters.length];
		boolean[] isDrive = new boolean[letters.length];

		// init the file objects and the initial drive state
		for (int i = 0; i < letters.length; ++i)
		{
			sensors[i] = new File(letters[i] + ":/");

			// isDrive[i] = sensors[i].canRead();
			isDrive[i] = false;
		}
		// TODO Create Logging Class with output like "2015-11-23 23:00:43 INFO Blah..."
		// TODO Create Logging Class with output like "2015-11-23 23:00:43 WARNING Blah..."
		System.out.println("Find Sensor: waiting for devices...");
		// loop indefinitely

		while (true)
		{
			// check each drive
			for (int i = 0; i < letters.length; ++i)
			{
				boolean pluggedIn = sensors[i].canRead();
				// if the state has changed output a message
				if (pluggedIn != isDrive[i])
				{
					if (pluggedIn)
					{
						FileSystemView view = FileSystemView.getFileSystemView();
						File dir = new File(letters[i] + ":/");
						String name = view.getSystemDisplayName(dir);// .substring(0, 5);
						if (name.equals(Constants.SENSOR_NAME+ " (" + letters[i] + ":)")) 
						{
							nConnectedSensors++;
							ConnectionManager.getInstance().setConnected(true);
							ConnectionManager.getInstance().setNumberOfConnectedSensors(nConnectedSensors);
							ConnectionManager.getInstance().addSensorToList(letters[i]);
						}
					}
					else
					{
						if (nConnectedSensors != 0)
						{
							nConnectedSensors--;
							ConnectionManager.getInstance().setConnected(false);
							ConnectionManager.getInstance().setNumberOfConnectedSensors(nConnectedSensors);
							ConnectionManager.getInstance().removeSensorFromList(letters[i]);
						}

					}
					isDrive[i] = pluggedIn;
				}
			}
			// wait before looping
			try
			{
				// sleep time make constant
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				/* do nothing */ }

		}
	}

}
