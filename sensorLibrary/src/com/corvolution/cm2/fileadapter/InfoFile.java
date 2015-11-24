package com.corvolution.cm2.fileadapter;

import java.io.File;

import com.corvolution.cm2.Constants;

/**
 * Connects a CM2 info file for reading the info properties defined as static final constants.
 * @author kirst
 *
 */
public class InfoFile extends TextFileAdapter
{
	public static final String DEVICE_NAME = "DeviceName";
	public static final String MANUFACTURER_NAME = "ManufacturerName";
	public static final String SERIAL_NUMBER = "SerialNumber";
	public static final String FIRMWARE_VERSION = "FirmwareVersion";
	public static final String FLASH_DATE = "FlashDate";

	/**
	 * Connects a CM2 info file for reading the info properties defined as static final constants.
	 * @param sensorPath Path to sensor
	 */
	public InfoFile(String sensorPath)
	{
		super(sensorPath + ":" + File.separator + Constants.CM2_INFO_FILE);
	}

}
