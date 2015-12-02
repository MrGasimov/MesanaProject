package com.corvolution.cm2.fileadapter;

import java.io.File;

import com.corvolution.cm2.Constants;
public class InfoFile extends TextFileAdapter
{
	public static final String DEVICE_NAME = "DeviceName";
	public static final String MANUFACTURER_NAME = "ManufacturerName";
	public static final String SERIAL_NUMBER = "SerialNumber";
	public static final String FIRMWARE_VERSION = "FirmwareVersion";
	public static final String FLASH_DATE = "FlashDate";
	public static final String CONFIGURATION_INTERFACE_VERSION = "configurationInterfaceVersion";

	public InfoFile(String sensorPath)
	{
		super(sensorPath + ":" + File.separator + Constants.CM2_INFO_FILE);
	}

}
