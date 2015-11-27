package com.corvolution.cm2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.CRC32;
import org.apache.commons.io.FileUtils;

import com.corvolution.cm2.configuration.SensorConfiguration;
import com.corvolution.cm2.fileadapter.ConfigFile;
import com.corvolution.cm2.fileadapter.CustomFile;
import com.corvolution.cm2.fileadapter.InfoFile;
import com.corvolution.cm2.fileadapter.StatusFile;
import com.corvolution.cm2.fileadapter.TimeSyncFile;

import java.util.Date;
import java.util.HashMap;

public class Sensor
{

	private String sensorPath;
	private String deviceName;
	private String manufacturerName;
	private String serialNumber;
	private String firmwareVersion;
	private Date flashDate;
	private String batteryVoltage;
	private String currentState;
	private String sensorSystemTime;
	private final static String VID = "VID_05E3";
	private final static String PID = "01B1";
	private SensorConfiguration sensorConfiguration;
	private InfoFile infoFile;
	private StatusFile statusFile;
	private CustomFile customFile;
	private ConfigFile configFile;
	private TimeSyncFile timeSyncFile;

	// construct sensor object
	public Sensor(String path) throws IOException
	{
		sensorConfiguration = new SensorConfiguration();
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
		return firmwareVersion;
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

	private void readSensorInfo() throws IOException
	{
		this.infoFile = new InfoFile(this.sensorPath);
		this.deviceName = this.infoFile.getProperty(InfoFile.DEVICE_NAME);
		this.manufacturerName = this.infoFile.getProperty(InfoFile.MANUFACTURER_NAME);
		this.serialNumber = this.infoFile.getProperty(InfoFile.SERIAL_NUMBER);
		this.firmwareVersion = this.infoFile.getProperty(InfoFile.FIRMWARE_VERSION);

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
		sensorConfiguration.addParameter("LinkId", this.customFile.getProperty(CustomFile.LINK_ID));

	}

	private void readFeedBackFile() throws IOException
	{
		this.statusFile = new StatusFile(this.sensorPath);
		this.batteryVoltage = this.statusFile.getProperty(StatusFile.BATTERY_VOLTAGE);
		this.currentState = this.statusFile.getProperty(StatusFile.CURRENT_STATE);
		this.sensorSystemTime = this.statusFile.getProperty(StatusFile.SYSTEM_TIME);

	}	
	// read measurement data from sensor
	public void readMeasurement(String dest)
		{
			File destination = new File(dest);
			// data in constant class define
			File source = new File(sensorPath + ":/data");
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
			// ecnrypted file in constant class
			File encryptedCustomFile = new File(this.sensorPath + ":" + File.separator + Constants.CM2_ENCRYPTED_FILE);
			if (encryptedCustomFile.exists())
			{
				try
				{
					FileInputStream ois = new FileInputStream(encryptedCustomFile);
					BufferedInputStream bis = new BufferedInputStream(ois);
					byte[] buffer = new byte[(int) encryptedCustomFile.length()];
					bis.read(buffer);
					sensorConfiguration.addEncryptedParameter("enryptedcustomIO", buffer, "0123456789ABCDEF");
					System.out.println(sensorConfiguration.getEncryptedParameter("enryptedcustomIO", "0123456789ABCDEF"));
					bis.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	
	
	// read configuration file from sensor
	private void readConfigFile() throws IOException
	{	
		String absolutePath = sensorPath + ":" + File.separator + Constants.CM2_CONFIG_FILE;
		this.configFile = new ConfigFile(absolutePath);
	}
			
	// write configuration file to sensor
	private void writeConfigFile() throws IOException
	{	
		String absolutePath = sensorPath + ":" + File.separator + Constants.CM2_CONFIG_FILE;
		this.configFile = new ConfigFile(absolutePath);	
		this.configFile.writeBinaryFile(sensorConfiguration);				
	}

	private void writeTimeSyncFile() throws IOException
	{
		String absolutePath = this.sensorPath + ":" + File.separator + Constants.CM2_TIMESYNC_FILE;
		this.timeSyncFile = new TimeSyncFile(absolutePath);
		timeSyncFile.writeBinaryFile();
	}

	public SensorConfiguration getConfiguration()
	{
		return this.sensorConfiguration;
	}

	public void setSensorConfiguration(SensorConfiguration sensorConfiguration)
	{
		this.sensorConfiguration = sensorConfiguration;
	}

	public void writeEncryptedParameters()
	{
		// key and inital vector must be at least 16 bytes--1st and 3rd arguments
		String text = "some text for encryption";

		try
		{
			sensorConfiguration.addEncryptedParameter("enryptedcustomIO", text.getBytes("UTF8"), "0123456789ABCDEF");
			File encryptedCustomFile = new File(this.sensorPath+":"+File.separator+Constants.CM2_ENCRYPTED_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(encryptedCustomFile));
			oos.writeObject(sensorConfiguration.getEncrypetdParameters().get("enryptedcustomIO"));
			oos.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void writeCustomeFile(String text)
	{
		//sensorConfiguration.getParameter("LinkId");
	}
	public long getDataSize()
	{
		long size = FileUtils.sizeOf(new File(this.sensorPath + ":" + File.separator + Constants.MEASUREMENT_FOLDER)); 
		return size;
	}

	// method for time synchronizing
	public void synchronize()
	{
		try
		{
			writeTimeSyncFile();
			devcon("restart");
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
			this.devcon("remove");
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
		System.out.println(command);
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
