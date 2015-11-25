package com.corvolution.occ;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.corvolution.cm2.ConnectionManager;
import com.corvolution.cm2.Sensor;
import com.corvolution.cm2.SensorConfiguration;
import com.corvolution.cm2.SensorEvent;
import com.corvolution.cm2.SensorListener;
import java.util.Scanner;

public class MySensorListener implements SensorListener
{
	public MySensorListener()
	{
	}

	@Override
	public void sensorConnection(SensorEvent e)
	{
		System.out.println("Sensor verbunden in Laufwerk " + e.getSensorPath());
		List<Sensor> sensorList = ConnectionManager.getConnectedSensorsList();

		Scanner scanner = new Scanner(System.in);
		String startTimeStr;
		do
		{
			System.out.print("Bitte Startzeit (heute!) in der Form 'HH:MM' eingeben: ");
			startTimeStr = scanner.next();
		} while (startTimeStr.length() != 5);
	    
	    int hours = Integer.parseInt(startTimeStr.substring(0, 2));
	    int minutes = Integer.parseInt(startTimeStr.substring(3, 5));

	    String durationStr;
		do
		{
			System.out.print("Bitte Aufnahmedauer in der Form 'HH:MM' eingeben: ");
			durationStr = scanner.next();
		} while (durationStr.length() != 5);
	    
	    int duration = Integer.parseInt(durationStr.substring(0, 2)) * 60 + Integer.parseInt(durationStr.substring(3, 5));
	    scanner.close();
		
		
		System.out.println("Verbundene Sensoren: " + sensorList.size());
		System.out.println("Startzeit: " + hours + ":" + minutes);
		
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(new Date());
		startTime.set(Calendar.HOUR_OF_DAY, hours);
		startTime.set(Calendar.MINUTE, minutes);		
		
		if (sensorList.size() == 1)
		{
			Sensor sensor = sensorList.get(0);

			// Write sensor starting time and recording duration
			SensorConfiguration config = new SensorConfiguration();
			config.setRecordingStartTime(startTime.getTime());
			config.setRecordingDuration(duration);
			sensor.setSensorConfiguration(config);
			
			System.out.println("Disconnecting sensor...");
			sensor.disconnect();
			System.out.println("Sensor disconnected. Please remove sensor.");
		}
	}
}
