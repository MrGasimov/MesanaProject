package com.corvolution.cm2.connection;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class SensorNotifier implements Runnable
{

	public void run()
	{
		sensorListener();
	}

	public void sensorListener()
	{
		int nConnectedSensors = 0;
		// String letters = "ABCDEFGH";
		String[] letters = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "O", "P",
				"Y"};
		File[] sensors = new File[letters.length];
		boolean[] isDrive = new boolean[letters.length];

		// init the file objects and the initial drive state
		for (int i = 0; i < letters.length; ++i)
		{
			sensors[i] = new File(letters[i] + ":/");

//			isDrive[i] = sensors[i].canRead();
			isDrive[i] = false;
		}
		// TODO Create Logging Class with output like "2015-11-23 23:00:43 INFO    Blah..."
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
						if (name.equals("STICK" + " (" + letters[i] + ":)")) // TODO use constant
						{
							nConnectedSensors++;
							ConnectionManager.getInstance().setConnected(true);
							ConnectionManager.getInstance().setNumberOfConnectedSensors(nConnectedSensors);
							try
							{
								ConnectionManager.getInstance().addSensorToList(letters[i]);
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
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
