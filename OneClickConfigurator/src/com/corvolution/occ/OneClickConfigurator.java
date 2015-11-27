package com.corvolution.occ;

import com.corvolution.cm2.connection.ConnectionManager;
import com.corvolution.cm2.connection.SensorNotifier;

/**
 * OneClickConfigurator is a very, very small and lightweight command line implementation for sensor configuration using
 * the CM200 library.
 * 
 * @author kirst
 *
 */
public class OneClickConfigurator
{

	public static void main(String[] args)
	{
		Thread usbListener = new Thread(new SensorNotifier());
		usbListener.start();
		ConnectionManager.getInstance().addSensorListener(new MySensorListener());
	}

}
