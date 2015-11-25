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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.CRC32;
import org.apache.commons.io.FileUtils;
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
	private String sensorVoltage;
	private String currentState;
	private String sensorSystemTime;
	private final static String VID = "01A1";
	private final static String PID = "01B1";
	private SensorConfiguration sensorConfiguration;
	private SensorPropertyManager pManager;

	// construct sensor object
	public Sensor(String path) throws IOException
	{
		sensorConfiguration = new SensorConfiguration();
		pManager = new SensorPropertyManager(path);
		this.sensorPath = path;
		readSensorInfo();
		if (new File(path + ":/config.cm2").exists())
		{
			readConfigFile();
		}
		readCustomFile();
		readFeedBackFile();
		readEncryptedParameters();
	}

	public String getSensorPath()
	{
		return sensorPath;
	}

	public String getDeviceName()
	{
		return deviceName;
	}

	public String getManufacturerName()
	{
		return manufacturerName;
	}

	public String getSerialNumber()
	{
		return serialNumber;
	}

	public Date getFlashDate()
	{
		return flashDate;
	}

	public double getSensorVoltage()
	{
		double voltage = Double.parseDouble(sensorVoltage);
		return voltage;
	}

	public String getFirwareVersion()
	{
		return firmwareVersion;
	}

	public ArrayList<String> fileReader(String absolutePath) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(absolutePath));
		String line = null;
		ArrayList<String> list = new ArrayList<String>();

		while ((line = reader.readLine()) != null)
		{
			list.add(line);
		}
		reader.close();
		return list;
	}

	// read sensor info file from sensor
	private void readSensorInfo() throws IOException
	{

		deviceName = pManager.getInfoProperty("DeviceName");
		manufacturerName = pManager.getInfoProperty("ManufacturerName");
		serialNumber = pManager.getInfoProperty("SerialNumber");
		firmwareVersion = pManager.getInfoProperty("FirmwareVersion");
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			flashDate = ft.parse(pManager.getInfoProperty("FlashDate"));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// read configuration file from sensor
	private void readConfigFile() throws IOException
	{
		String absolutePath = sensorPath + ":/config.cm2";
		try
		{
			byte[] buffer = new byte[21];
			FileInputStream inputStream = new FileInputStream(absolutePath);
			BufferedInputStream bufferedOutStream = new BufferedInputStream(inputStream);
			int total = 0;
			int nRead = 0;
			CRC32 myCRC = new CRC32();
			while ((nRead = bufferedOutStream.read(buffer)) != -1)
			{
				total += nRead;
				System.out.println(nRead);
				System.out.println(total);
			}
			myCRC.update(buffer, 0, 17);
			inputStream.close();
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

	}

	// write configuration file to sensor
	public void writeConfigFile() throws IOException
	{
		String absolutePath = sensorPath + ":/config.cm2";
		// ,sensorConfiguration.checksum[0],
		// sensorConfiguration.checksum[1],sensorConfiguration.checksum[2],sensorConfiguration.checksum[3]
		byte[] buffer = {SensorConfiguration.VERSION_MAJOR, SensorConfiguration.VERSION_MINOR,
				SensorConfiguration.startMode[0], sensorConfiguration.configSet[0],
				sensorConfiguration.recordDuration[0], sensorConfiguration.recordDuration[1],
				sensorConfiguration.recordDuration[2], sensorConfiguration.recordDuration[3],
				sensorConfiguration.recordDuration[4], sensorConfiguration.recordDuration[5],
				sensorConfiguration.startTime[0], sensorConfiguration.startTime[1], sensorConfiguration.startTime[2],
				sensorConfiguration.startTime[3], sensorConfiguration.startTime[4], sensorConfiguration.startTime[5],
				sensorConfiguration.latency};
		CRC32 myCRC = new CRC32();
		myCRC.update(buffer);
		FileOutputStream outputStream = new FileOutputStream(absolutePath);
		BufferedOutputStream out = new BufferedOutputStream(outputStream);
		outputStream.write(buffer);
		out.flush();
		outputStream.close();
	}

	public void writeTimeSyncFile() throws IOException
	{
		String absolutePath = sensorPath + ":/timesync.cm2";

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte year = 35;
		year += (byte) (cal.get(Calendar.YEAR) - 2015);
		byte month = (byte) cal.get(Calendar.MONTH);
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		byte hour = (byte) cal.get(Calendar.HOUR_OF_DAY);
		byte minute = (byte) cal.get(Calendar.MINUTE);
		byte second = (byte) cal.get(Calendar.SECOND);
		byte[] buffer = {year, month, day, hour, minute, second};

		FileOutputStream outputStream = new FileOutputStream(absolutePath);
		BufferedOutputStream out = new BufferedOutputStream(outputStream);
		outputStream.write(buffer);
		out.flush();
		outputStream.close();

	}

	public SensorConfiguration getConfiguration()
	{
		return this.sensorConfiguration;
	}

	// read measurement data from sensor
	public void readMeasurementFromSensor(String dest)
	{
		File destination = new File(dest);
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
		File encryptedCustomFile = new File(sensorPath + ":/encryptedCustom.txt");
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

	public void writeEncryptedParameters()
	{
		// key and inital vector must be at least 16 bytes--1st and 3rd arguments
		String text = "some text for encryption";

		try
		{
			sensorConfiguration.addEncryptedParameter("enryptedcustomIO", text.getBytes("UTF8"), "0123456789ABCDEF");
			File encryptedCustomFile = new File(sensorPath + ":/encryptedCustom.txt");
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

	}

	public void readCustomFile() throws IOException
	{
		String absolutePath = sensorPath + ":/custom.txt";
		sensorConfiguration.addParameter("LinkId", pManager.getCustomProperty("LinkId"));
	}

	public void readFeedBackFile() throws IOException
	{

		sensorVoltage = pManager.getStatusProperty("BatteryVoltage");
		currentState = pManager.getStatusProperty("CurrentState");
		sensorSystemTime = pManager.getStatusProperty("SystemTime");
	}

	public long getDataSize()
	{
		long size = FileUtils.sizeOf(new File(sensorPath + ":/data"));
		return size;
	}

	// method for time synchronizing
	public void synchronize()
	{
		try
		{
			writeTimeSyncFile();
			disconnect("restart");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// removing or restarting sensor
	public void disconnect(String command) throws IOException
	{
		// remove Sensor
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd \"C:/Users/Gasimov/Desktop\" && devcon.exe " + command + " *VID_05E3*");
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
