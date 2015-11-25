package com.corvolution.cm2;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

public class UsbListener implements Runnable
{

	public void run()
	{
		sensorListener();
	}

	public void sensorListener()
	{
		int counter = 0;
		String[] letters = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"};
		File[] sensors = new File[letters.length];
		boolean[] isDrive = new boolean[letters.length];

		// init the file objects and the initial drive state
		for (int i = 0; i < letters.length; ++i)
		{
			sensors[i] = new File(letters[i] + ":/");

			isDrive[i] = sensors[i].canRead();
		}
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
						String name = view.getSystemDisplayName(dir).substring(0, 5);
						if (name.equals("STICK"))
						{
							counter++;
							ConnectionManager.setState(true);
							ConnectionManager.setCounter(counter);
							try
							{
								ConnectionManager.getInstance().addSensorToList(letters[i]);
							}
							catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println(ConnectionManager.getConnectedSensorsList());
						}
					}
					else
					{
						if (counter != 0)
						{
							counter--;
							ConnectionManager.setState(false);
							ConnectionManager.setCounter(counter);
							ConnectionManager.getInstance().removeSensorFromList(letters[i]);
							System.out.println(ConnectionManager.getConnectedSensorsList());
						}

					}
					isDrive[i] = pluggedIn;
				}
			}
			// wait before looping
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				/* do nothing */ }

		}
	}

}
