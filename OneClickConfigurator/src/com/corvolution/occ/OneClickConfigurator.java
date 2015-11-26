package com.corvolution.occ;

import com.corvolution.cm2.ConnectionManager;
import com.corvolution.cm2.UsbListener;

/**
 * OneClickConfigurator is a very, very small and lightweigt command line implementation for sensor configuration using
 * the CM200 library.
 * 
 * @author kirst
 *
 */
public class OneClickConfigurator
{

	public static void main(String[] args)
	{
		Thread usbListener = new Thread(new UsbListener());
		usbListener.start();
		ConnectionManager.getInstance().addSensorListener(new MySensorListener());
	}

}
