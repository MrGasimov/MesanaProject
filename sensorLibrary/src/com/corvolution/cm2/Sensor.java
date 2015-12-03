package com.corvolution.cm2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import com.corvolution.cm2.configuration.SensorConfiguration;
import com.corvolution.cm2.fileadapter.ConfigFile;
import com.corvolution.cm2.fileadapter.CustomFile;
import com.corvolution.cm2.fileadapter.InfoFile;
import com.corvolution.cm2.fileadapter.StatusFile;
import com.corvolution.cm2.fileadapter.TimeSyncFile;

public class Sensor
{

	private String sensorPath;
	private String deviceName;
	private String manufacturerName;
	private String serialNumber;
	private String firmwareVersion;
	private Date flashDate;
	private String configurationInterfaceVersion;
	private String batteryVoltage;
	private String currentState;
	private String sensorSystemTime;
	private final static String VID = "VID_05E3";
	private final static String PID = "01B1";
	private SensorConfiguration writeSensorConfiguration;
	private SensorConfiguration readSensorConfiguration;
	private InfoFile infoFile;
	private StatusFile statusFile;
	private CustomFile customFile;
	private ConfigFile configFile;
	private TimeSyncFile timeSyncFile;

	// construct sensor object
	public Sensor(String path) throws IOException
	{
		readSensorConfiguration = new SensorConfiguration();
		this.sensorPath = path;
		readSensorInfo();
		if (new File(this.sensorPath + ":" + File.separator + Constants.CM2_CONFIG_FILE).exists())
		{
			readConfigFile();
		}
		readCustomFile();
		readFeedBackFile();
		readEncryptedParameters();
	}

	public double getBatteryVoltage()
	{
		double voltage = Double.parseDouble(batteryVoltage);
		return voltage;
	}

	public String getFirwareVersion()
	{
		return this.firmwareVersion;
	}

	public String getSensorPath()
	{
		return this.sensorPath;
	}

	public String getDeviceName()
	{
		return this.deviceName;
	}

	public String getManufacturerName()
	{
		return this.manufacturerName;
	}

	public String getSerialNumber()
	{

		return this.serialNumber;
	}

	public Date getFlashDate()
	{

		return this.flashDate;
	}

	public String getConfigurationInterfaceVersion()
	{
		return configurationInterfaceVersion;
	}

	private void readSensorInfo() throws IOException
	{
		this.infoFile = new InfoFile(this.sensorPath);
		this.deviceName = this.infoFile.getProperty(InfoFile.DEVICE_NAME);
		this.manufacturerName = this.infoFile.getProperty(InfoFile.MANUFACTURER_NAME);
		this.serialNumber = this.infoFile.getProperty(InfoFile.SERIAL_NUMBER);
		this.firmwareVersion = this.infoFile.getProperty(InfoFile.FIRMWARE_VERSION);
		this.configurationInterfaceVersion = this.infoFile.getProperty(InfoFile.CONFIGURATION_INTERFACE_VERSION);

		SimpleDateFormat ft = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
		try
		{
			this.flashDate = ft.parse(this.infoFile.getProperty(InfoFile.FLASH_DATE));
		}
		catch (ParseException e)
		{
			System.err.println("Flash date has wrong format.");
			this.flashDate = null;
		}
		catch (Exception e)
		{
			this.flashDate = null;
		}
	}

	private void readCustomFile() throws IOException
	{
		this.customFile = new CustomFile(this.sensorPath);
		readSensorConfiguration.addParameter("LinkId", this.customFile.getProperty(CustomFile.LINK_ID));
		// for reading additional data from custom file, additional data details must be added to customFile class

	}

	private void readFeedBackFile() throws IOException
	{
		this.statusFile = new StatusFile(this.sensorPath);
		this.batteryVoltage = this.statusFile.getProperty(StatusFile.BATTERY_VOLTAGE);
		this.currentState = this.statusFile.getProperty(StatusFile.CURRENT_STATE);
		this.sensorSystemTime = this.statusFile.getProperty(StatusFile.SYSTEM_TIME);

	}

	// read measurement data from sensor
	public void readMeasurement(String dest) throws IOException
	{
		File destination = new File(dest);
		File source = new File(sensorPath + ":" + File.separator + Constants.MEASUREMENT_FOLDER);
		try
		{
			FileUtils.copyDirectoryToDirectory(source, destination);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void readEncryptedParameters()
	{
		File encryptedCustomFile = new File(this.sensorPath + ":" + File.separator + Constants.CM2_ENCRYPTED_FILE);
		if (encryptedCustomFile.exists())
		{
			try
			{
				FileInputStream ois = new FileInputStream(encryptedCustomFile);
				BufferedInputStream bis = new BufferedInputStream(ois);
				byte[] buffer = new byte[(int) encryptedCustomFile.length()];
				bis.read(buffer);
				readSensorConfiguration.addEncryptedParameter("enryptedcustomIO", buffer, "0123456789ABCDEF");
				bis.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	// read configuration file from sensor
	public void readConfigFile()
	{
		String absolutePath = sensorPath + ":" + File.separator + Constants.CM2_CONFIG_FILE;
		this.configFile = new ConfigFile(absolutePath);
	}

	// write configuration file to sensor
	public void writeConfigFile()
	{
		String absolutePath = sensorPath + ":" + File.separator + Constants.CM2_CONFIG_FILE;
		this.configFile = new ConfigFile(absolutePath);
		this.configFile.writeBinaryFile(this.writeSensorConfiguration);
	}

	public void writeTimeSyncFile() throws IOException
	{
		String absolutePath = this.sensorPath + ":" + File.separator + Constants.CM2_TIMESYNC_FILE;
		this.timeSyncFile = new TimeSyncFile(absolutePath);
		timeSyncFile.writeBinaryFile();
	}

	public SensorConfiguration getWriteConfiguration()
	{
		return this.writeSensorConfiguration;
	}

	public SensorConfiguration getReadConfiguration()
	{
		return this.readSensorConfiguration;
	}

	public void setSensorConfiguration(SensorConfiguration config)
	{
		this.writeSensorConfiguration = config;
	}

	public void writeEncryptedParameters()
	{
		// key and inital vector must be at least 16 bytes--1st and 3rd arguments
		String text = "some text for encryption";

		try
		{
			writeSensorConfiguration.addEncryptedParameter("enryptedcustomIO", text.getBytes("UTF8"),
					"0123456789ABCDEF");
			File encryptedCustomFile = new File(this.sensorPath + ":" + File.separator + Constants.CM2_ENCRYPTED_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(encryptedCustomFile));
			oos.writeObject(writeSensorConfiguration.getEncrypetdParameters().get("enryptedcustomIO"));
			oos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();

		}

	}

	public void writeCustomFile()
	{
		String data = null;
		for (HashMap.Entry<String, String> entry : writeSensorConfiguration.getParametersMap().entrySet())
		{
			data = data + entry.getKey() + "=" + entry.getValue() + "\r\n";

		}
		this.customFile = new CustomFile(sensorPath);
		customFile.writeCustomData(data, sensorPath);
	}

	public long getDataSize()
	{
		long size = FileUtils.sizeOf(new File(this.sensorPath + ":" + File.separator + Constants.MEASUREMENT_FOLDER));
		return size;
	}

	public boolean checkConfiguration()
	{
		boolean compatible = false;
		if (writeSensorConfiguration.getStartMode().getCompatibleConfigurationVersion() == configurationInterfaceVersion)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(writeSensorConfiguration.getRecordingStartTime());
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			if (1 <= day && day <= 31 && 0 <= hour && hour <= 23 && 0 <= minute && minute <= 59)
			{
				if (writeSensorConfiguration.getConfigurationSet().equals("mesana"))
				{
					compatible = true;
				}
			}
		}
		return compatible;
	}
	// method for time synchronizing
	public void synchronize()
	{
		try
		{
			writeTimeSyncFile();
			devcon(Constants.RESTART_COMMAND);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void disconnect()
	{
		try
		{
			this.writeConfigFile();
			this.synchronize();
			this.devcon(Constants.REMOVE_COMMAND);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// removing or restarting sensor

	private void devcon(String devconCommand) throws IOException
	{
		// remove Sensor
		String command = "cd \"C:/Users/" + System.getProperty("user.name") + "/Desktop\" && devcon.exe "
				+ devconCommand + " *" + VID + "*";
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true)
		{
			line = r.readLine();
			if (line == null)
			{
				break;
			}
			System.out.println(line);
		}
	}

}
