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
/**
* 
* This class represents single connected sensor
* and provides appropriate methods for retrieving, setting and configuring sensor.
*
* @author  Suleyman Gasimov 
* @version 1.0
* @since   07.01.2016 
* 
*/
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
	private SensorConfiguration writeSensorConfiguration;
	private SensorConfiguration readSensorConfiguration;
	private InfoFile infoFile;
	private StatusFile statusFile;
	private CustomFile customFile;
	private ConfigFile configFile;
	private TimeSyncFile timeSyncFile;
	
	/**
	   * Only constructor for constructing sensor object when it is connected over usb port.
	   * This constructor reads info, config, feedback and encrypted files to initialize sensor 
	   * @param path to connected sensor.
	 * @throws SensorNotFoundException if sensor connection failed or disconnected
	   */
	public Sensor(String path) throws SensorNotFoundException
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
	}
	
	
	/**This method returns current state of sensor.
	 * @return state as string
	 */
	public String getCurrentState()
	{
		return this.currentState;
	}

	/**This method returns system time of the sensor
	 * @return time as string
	 */
	public String getSensorSystemTime()
	{
		return this.sensorSystemTime;
	}
	
	/**This method returns Battery Voltage of the connected sensor as a double primitive type
	 * @return double
	 */
	public double getBatteryVoltage()
	{	
		
		double voltage = Double.parseDouble(batteryVoltage);
		return voltage;
	}

	/**This method returns firmware version of the connected sensor 
	 * @return String
	 */
	public String getFirwareVersion()
	{
		return this.firmwareVersion;
	}

	/**This method returns path to the connected sensor 
	 * @return String
	 */
	public String getSensorPath()
	{
		return this.sensorPath;
	}

	/**This method returns device name of the connected sensor
	 * @return String
	 */
	public String getDeviceName()
	{
		return this.deviceName;
	}

	/**This method returns Manufacture name of the connected sensor
	 * @return String
	 */
	public String getManufacturerName()
	{
		return this.manufacturerName;
	}
	
	/**This method returns serial number of the connected sensor
	 * @return String
	 */
	public String getSerialNumber()
	{

		return this.serialNumber;
	}
	
	/**This method returns last flash date of the connected sensor
	 * @return String
	 */
	public Date getFlashDate()
	{

		return this.flashDate;
	}
	/**This method returns configuration interface version of the connected sensor, which defines compatibility of the sensor with the configuration set
	 * @return String
	 */
	public String getConfigurationInterfaceVersion()
	{
		return configurationInterfaceVersion;
	}
	
	/**This method reads info file from sensor to initialize sensor details 
	 * @throws ParseException, if parsing time format was not successful. 
	 * @throws IOException if file not found or file could be opened, ParseException 
	 */
	private void readSensorInfo() throws SensorNotFoundException
	{	
		this.infoFile = new InfoFile(this.sensorPath);
		if(infoFile.equals(null))
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
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
			e.printStackTrace();
		}
		
	}

	/**This method reads custom text file from sensor to retrieve link id.For reading additional data from custom file, additional data details must be added to customFile class 
	 * @throws SensorNotFoundException, if sensor connection failed or  sensor is disconnected.
	 */
	private void readCustomFile() throws SensorNotFoundException 
	{
		this.customFile = new CustomFile(this.sensorPath);
		if(customFile.equals(null))
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
		readSensorConfiguration.addParameter("LinkId", this.customFile.getProperty(CustomFile.LINK_ID));
		

	}

	/**This method reads feedback text file from sensor to initialize sensor state information
	 * @throws SensorNotFoundException , if sensor connection failed or  sensor is disconnected.
	 */
	private void readFeedBackFile() throws SensorNotFoundException
	{
		this.statusFile = new StatusFile(this.sensorPath);
		if(statusFile.equals(null))
		{
			throw new SensorNotFoundException("Sensor not Found");
		}
		this.batteryVoltage = this.statusFile.getProperty(StatusFile.BATTERY_VOLTAGE);
		this.currentState = this.statusFile.getProperty(StatusFile.CURRENT_STATE);
		this.sensorSystemTime = this.statusFile.getProperty(StatusFile.SYSTEM_TIME);

	}

	/**This method copies measurement data from sensor to specified destination directory
	 * @param dest where data to be copied
	 * @throws SensorNotFoundException if sensor connection failed or disconnected
	 */
	public void readMeasurement(String dest) throws SensorNotFoundException
	{
		File destination = new File(dest);
		File source = new File(sensorPath + ":" + File.separator + Constants.MEASUREMENT_FOLDER);
		try
		{
			FileUtils.copyDirectoryToDirectory(source, destination);
		}
		catch (IOException e)
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
	}

	/**This method reads encrypted data from sensor with given password
	 * @param password for reading encrypted parameters
	 * @throws SensorNotFoundException if sensor connection failed or disconnected
	 */
	public void readEncryptedParameters(String password) throws SensorNotFoundException
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
				readSensorConfiguration.addEncryptedParameter(password, buffer, "0123456789ABCDEF");
				bis.close();
			}
			catch (IOException e)
			{
				throw new SensorNotFoundException("Sensor not found!");
			}
		}

	}
	
	
	
	/**This method reads config binary file from the connected sensor.
	 * @throws SensorNotFoundException if sensor connection failed or disconnected
	 */
	public void readConfigFile()  throws SensorNotFoundException
	{
		String absolutePath = sensorPath + ":" + File.separator + Constants.CM2_CONFIG_FILE;
		this.configFile = new ConfigFile(absolutePath);
		if(configFile.equals(null))
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
	}

	/**This method writes configuration file to the connected sensor.Before calling this method configuration must be set
	 * @throws SensorNotFoundException if sensor connection failed or disconnected
	 */
	public void writeConfigFile()throws SensorNotFoundException
	{
		String absolutePath = sensorPath + ":" + File.separator + Constants.CM2_CONFIG_FILE;
		this.configFile = new ConfigFile(absolutePath);
		if(configFile.equals(null))
		{
			throw new SensorNotFoundException("Sensor not found");
		}
		this.configFile.writeBinaryFile(this.writeSensorConfiguration);
	}

	
	/**This method writes time synchronization binary file to the connected sensor, so sensor could synchronize itself after detaching
	 * @throws SensorNotFoundException if sensor connection failed or disconnected
	 */
	public void writeTimeSyncFile() throws SensorNotFoundException
	{
		String absolutePath = this.sensorPath + ":" + File.separator + Constants.CM2_TIMESYNC_FILE;
		this.timeSyncFile = new TimeSyncFile(absolutePath);
		if(timeSyncFile.equals(null))
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
		timeSyncFile.writeBinaryFile();
	}

	
	
	/**This method retrieves SensorConfiguration object for writing configuration File to the sensor 
	 * @return SensorConfiguration writeSensorConfiguration
	 */
	public SensorConfiguration getWriteConfiguration()
	{
		return this.writeSensorConfiguration;
	}

	/**This method also returns SensorConfiguration object, but for reading configuration File from sensor
	 * @return SensorConfiguration readSensorConfiguration
	 */
	public SensorConfiguration getReadConfiguration()
	{
		return this.readSensorConfiguration;
	}

	/**This method sets SensorConfiguration config object by  application
	 * @param config SensorConfiguration object for writing configuration sensor by user
	 */
	public void setSensorConfiguration(SensorConfiguration config)
	{
		this.writeSensorConfiguration = config;
	}

	/**This method writes encypted parameters to sensor by application. 
	 *@param password for encryption
	 *@throws SensorNotFoundException if sensor connection failed or disconnected
	 */
	public void writeEncryptedParameters(String password)throws SensorNotFoundException
	{
		// key and initialization vector must be at least 16 bytes--1st and 3rd arguments
		String text = writeSensorConfiguration.getParameter("encrypted");

		try
		{
			writeSensorConfiguration.addEncryptedParameter(password, text.getBytes("UTF8"),
					"0123456789ABCDEF");
			File encryptedCustomFile = new File(this.sensorPath + ":" + File.separator + Constants.CM2_ENCRYPTED_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(encryptedCustomFile));
			oos.writeObject(writeSensorConfiguration.getEncrypetdParameters().get("enryptedcustomIO"));
			oos.close();
		}
		catch (IOException e)
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
	}

	/**This method writes parameters to custom file for sensor configuration set by application, for example LinkId of a given measurement.
	 * @throws SensorNotFoundException if writing data was not successfull or sensor is disconnected
	 */
	public void writeCustomFile() throws SensorNotFoundException
	{
		String data = null;
		for (HashMap.Entry<String, String> entry : writeSensorConfiguration.getParametersMap().entrySet())
		{
			data = data + entry.getKey() + "=" + entry.getValue() + "\r\n";

		}
		this.customFile = new CustomFile(sensorPath);
		if(customFile.equals(null))
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
		customFile.writeCustomData(data, sensorPath);
	}

	/**This method retrieves size of measurement data from sensor
	 * @return long. size of measurement data
	 */
	public long getDataSize()
	{
		long size = FileUtils.sizeOf(new File(this.sensorPath + ":" + File.separator + Constants.MEASUREMENT_FOLDER));
		return size;
	}

	/**This method allows application to check correctness of configuration before configuring sensor
	 * @return true if configuration is compatible with a connected sensor otherwise false
	 */
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
	
	/**This method writes current system time to sensor for synchronizing
	 * @throws SensorNotFoundException if sensor connection failed or disconnected
	 */
	public void synchronize() throws SensorNotFoundException
	{
		try
		{
			writeTimeSyncFile();
			devcon(Constants.RESTART_COMMAND);
		}
		catch (IOException e)
		{
			throw new SensorNotFoundException("Sensor not found!");
		}

	}

	
	/**This method writes current system time and remove sensor from system
	 * @throws SensorNotFoundException if synchronizing  or removing sensor failed
	 */
	public void disconnect() throws SensorNotFoundException
	{
		try
		{
			this.synchronize();
			this.devcon(Constants.REMOVE_COMMAND);
		}
		catch (IOException e)
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
	}

	
	private void devcon(String devconCommand) throws IOException
	{
		// remove Sensor
		String command = "cd \"C:/Users/" + System.getProperty("user.name") + "/Desktop\" && devcon.exe "
				+ devconCommand + " *" + Constants.VID + "*";
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
		}
	}

}
