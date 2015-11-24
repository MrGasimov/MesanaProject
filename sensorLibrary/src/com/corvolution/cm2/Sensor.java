package com.corvolution.cm2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.CRC32;
import org.apache.commons.io.FileUtils;

import com.corvolution.cm2.fileadapter.InfoFile;

import java.util.Date;

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
	private final static String VID = "VID_05E3";
	private final static String PID = "01B1";
	private SensorConfiguration sensorConfiguration;
	private InfoFile infoFile;

	// construct sensor object
	public Sensor(String path) throws IOException
	{
		sensorConfiguration = new SensorConfiguration();
		this.sensorPath = path;
		readSensorInfo();
		if (new File(path + ":/config.cm2").exists())
		{
			readConfigFile();
		}
		readCustomFile();
		readFeedBackFile();
	}

	public String getSensorPath()
	{
		return sensorPath;
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

	public String getSensorVoltage()
	{
		return this.sensorVoltage;
	}

	public String getFirwareVersion()
	{
		return this.firmwareVersion;
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

	/**
	 * Read sensor info file from sensor
	 * @throws IOException
	 */
	private void readSensorInfo() throws IOException
	{
		this.infoFile = new InfoFile(this.sensorPath);
		this.deviceName = this.infoFile.getProperty(InfoFile.DEVICE_NAME);
		this.manufacturerName = this.infoFile.getProperty(InfoFile.MANUFACTURER_NAME);
		this.serialNumber = this.infoFile.getProperty(InfoFile.SERIAL_NUMBER);
		this.firmwareVersion = this.infoFile.getProperty(InfoFile.FIRMWARE_VERSION);

		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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

	// read configuration file from sensor
	private void readConfigFile() throws IOException
	{
		//TODO please implement like readSensorInfo()
//		String absolutePath = sensorPath + ":/config.cm2";
//		try
//		{
//			byte[] buffer = new byte[21];
//			FileInputStream inputStream = new FileInputStream(absolutePath);
//			BufferedInputStream out = new BufferedInputStream(inputStream);
//			int total = 0;
//			int nRead = 0;
//
//			while ((nRead = inputStream.read(buffer)) != -1)
//			{
//				total += nRead;
//				System.out.println(nRead);
//			}
//
//			inputStream.close();
//		}
//		catch (FileNotFoundException ex)
//		{
//			ex.printStackTrace();
//		}
//		catch (IOException ex)
//		{
//			ex.printStackTrace();
//		}

	}

	// write configuration file to sensor
	private void writeConfigFile() throws IOException
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

	private void writeTimeSyncFile() throws IOException
	{
		String absolutePath = sensorPath + ":/timesync.cm2";

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		byte year = (byte) (cal.get(Calendar.YEAR) - 2000); // Year 0 is mapped to year 2000
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

	private void readCustomFile() throws IOException
	{
		//FIXME Use functions similar to java properties file!
//		String absolutePath = sensorPath + ":/custom.txt";
//		sensorConfiguration.addParameter("LinkId", fileReader(absolutePath).get(0).substring(7, 13));

	}

	private void readFeedBackFile() throws IOException
	{
		//FIXME Use functions similar to java properties file!
//		String absolutePath = sensorPath + ":/status.txt";
//		sensorVoltage = fileReader(absolutePath).get(0).substring(15, 18);
//		currentState = fileReader(absolutePath).get(1).substring(13, 22);
//		sensorSystemTime = fileReader(absolutePath).get(2).substring(11, 30);
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
			devcon("restart");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	
	public void disconnect()
	{
		this.synchronize();
		try
		{
			this.writeConfigFile();
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
		String command = "cd \"C:/Users/" + System.getProperty("user.name") + "/Desktop\" && devcon.exe " + devconCommand + " *" + VID + "*";
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
