package com.corvolution.cm2.configuration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class represents configuration object for sensor. For configuring sensor  instance of this class must be initialized. 
 * @author Suleyman Gasimov
 *
 */
public class SensorConfiguration implements ConfigurationInterface_v1_0
{
	private HashMap<String, String> additionalParameters;
	private HashMap<String, byte[]> encryptedParameters;
	private Date recordingStartTime;
	private int recordingDuration;
	private ConfigurationSet configurationSet;
	private StartMode startMode;
	private String configInterfaceVersionMajor;
	private String configInterfaceVersionMinor;

	/**Default constructor.
	 * 
	 */
	public SensorConfiguration()
	{
		additionalParameters = new HashMap<>();
		encryptedParameters = new HashMap<>();
	}

	/**Method for setting version  of Configuration Interface  for sensor configuration
	 * @param major part of Configuration Interface version
	 * @param minor part of Configuration Interface version
	 */
	public void setConfigurationInterfaceVersion(String major, String minor)
	{
		this.configInterfaceVersionMajor = major;
		this.configInterfaceVersionMinor = minor;
	}

	/**This method returns major part of configuration interface version as a byte
	 * @return byte
	 */
	public byte getConfigurationInterfaceVersionMajor()
	{
		return  Byte.parseByte(configInterfaceVersionMajor); 
	}
	
	/**This method returns minor part of configuration interface version as a byte
	 * @return byte
	 */
	public byte getConfigurationInterfaceVersionMinor()
	{
		return  Byte.parseByte(configInterfaceVersionMinor); 
	}
	
	/**This method sets startMode of configuration given by application
	 * 
	 */
	public void setStartMode(StartMode startMode)
	{
		this.startMode = startMode;

	}

	/**This method returns startMode set by application for configuring sensor
	 * @return StartMode
	 */
	public StartMode getStartMode()
	{
		return this.startMode;
	}
	
	/**This method returns startMode set by application for configuring sensor
	 * @return StartMode
	 */
	public ConfigurationSet getConfigurationSet()
	{
		return configurationSet;
	}
	
	/**
	 * This method sets configurationSet given by application
	 *
	 */
	public void setConfigurationSet(ConfigurationSet configurationSet)
	{

		this.configurationSet = configurationSet;
	}

	/**
	 * This method sets recording start time. Parameter is given by application
	 *
	 */
	public void setRecordingStartTime(Date date)
	{
		this.recordingStartTime = date;
	}
	
	/**
	 * This method returns recording start time of the sensor  which set by application
	 *@return Date
	 */
	public Date getRecordingStartTime()
	{
		return this.recordingStartTime;
	}
	
	/**
	 * This method sets recording duration for the sensor.Parameter is given by application
	 *
	 */
	public void setRecordingDuration(int duration)
	{
		this.recordingDuration = duration;
	}
	
	/**
	 * This method returns recording duration of the sensor set by application
	 *@return int recording duration
	 */
	public int getRecordingDuration()
	{

		return this.recordingDuration;
	}
	
	
	/**This method encrypts data which is given as byte array.For encryption password and initialization vector must be passed to method. Password and initialization vector must be at least 16 bytes. 
	 * @param skey password is given by application 
	 * @param byte[] message data to be encrypted 
	 * @param ivx initialization vector
	 * @return byte[] encrypted data as byte array
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 */
	private static byte[] encrypt(String skey, byte[] message, String ivx)
			throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
	{
		
		SecretKeySpec keySpec = new SecretKeySpec(skey.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivx.getBytes());

		Cipher cipher = getCypher(keySpec, ivSpec, Cipher.ENCRYPT_MODE);

		// encrypt using the cypher
		byte[] raw = cipher.doFinal(message);

		return raw;
	}

	/**This method decrypts data which is given as byte array.For decryption password and initialization vector must be passed to method. Password and initialization vector must be same when used for encryption and length at least 16 bytes. 
	 * @param encrypted data to be decrypted
	 * @param skey password for decryption.Same used for encryption
	 * @param ivx initialization vector 
	 * @return String decrypted data
	 * @throws InvalidKeyException This is the exception for invalid Keys (invalid encoding, wrong length, uninitialized, etc).
	 * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	 * @throws IllegalBlockSizeException This exception is thrown when the length of data provided to a block cipher is incorrect, i.e., does not match the block size of the cipher.
	 * @throws BadPaddingException This exception is thrown when a particular padding mechanism is expected for the input data but the data is not padded properly.
	 */
	public static String decrypt(byte[] encrypted, String skey, String ivx)
			throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException
	{

		SecretKeySpec keySpec = new SecretKeySpec(skey.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivx.getBytes());

		Cipher cipher = getCypher(keySpec, ivSpec, Cipher.DECRYPT_MODE);

		// decode the message
		byte[] stringBytes = cipher.doFinal(encrypted);

		// converts the decoded message to a String
		String data = new String(stringBytes, "UTF8");

		return data;
	}

	private static Cipher getCypher(SecretKeySpec keySpec, IvParameterSpec ivSpec, int mode) throws InvalidKeyException
	{ 
		// Get a cipher object.
		Cipher cipher;
		try
		{
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException("invalid algorithm", e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new RuntimeException("invalid padding", e);
		}
		try
		{
			cipher.init(mode, keySpec, ivSpec);

		}
		catch (InvalidAlgorithmParameterException e)
		{
			throw new RuntimeException("invalid algorithm parameter.", e);
		}
		return cipher;
	}

	/**This method adds any given parameter by application to hash map 
	 * @param key of value to be added to HashMap
	 * @param value of parameter given by
	 */
	public void addParameter(String key, String value)
	{
		this.additionalParameters.put(key, value);
	}

	/**This method returns parameter value given with key from hash map. Parameter added by application
	 * @param key of value from parameters HashMap
	 * @return parameter from hashMap 
	 */
	public String getParameter(String key)
	{
		return additionalParameters.get(key);
	}
	
	
	/**This method adds encrypted parameters to hash map password and initialization vector as a key argument
	 * @param key initialization vector
	 * @param value byte array of data
	 * @param password for encryption
	 */
	public void addEncryptedParameter(String key, byte[] value, String password)
	{
		try
		{
			this.encryptedParameters.put(key, encrypt(key, value, password));
		}
		catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e)
		{
			e.printStackTrace();
		}
	}

	/**This method returns encrypted parameter from hash map added by application. For getting parameter password and initialization vector (key) arguments must be passed to method.
	 * @param key initialization vector  
	 * @param password for decryption
	 * @return String encrypted parameter
	 */
	public String getEncryptedParameter(String key, String password)
	{
		String para = null;
		try
		{
			para = decrypt(encryptedParameters.get(key), key, password);
		}
		catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e)
		{
			e.printStackTrace();
		}
		return para;
	}

	/**This method returns hash map of parameters added by application
	 * @return HashMap of parameters 
	 */
	public HashMap<String, String> getParametersMap()
	{
		return additionalParameters;
	}

	/**This method returns hash map of encrypted parameters added by application
	 * @return HashMap of encrypted parameters
	 */
	public HashMap<String, byte[]> getEncrypetdParameters()
	{
		return encryptedParameters;
	}

	

}
