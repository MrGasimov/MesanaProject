package com.corvolution.cm2.configuration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SensorConfiguration
{
	
	private HashMap<String, HashMap> configSetMap;
	private HashMap<String, String> addParameters;
	private HashMap<String, byte[]> encryptedParameters;
	private Date recordingStartTime;
	private int durationMinutes;

	public SensorConfiguration()
	{
		configSetMap = new HashMap<>();
		addParameters = new HashMap<>();
		encryptedParameters = new HashMap<>();
		// constructing default configset which is number 1--> configset[0]
		ConfigurationSet configSet = new ConfigurationSet();
		// default set for configFile
		configSetMap.put("1", configSet.getSet());
	}

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

	public static String decrypt(byte[] encrypted, String skey, String ivx)
			throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException
	{

		SecretKeySpec keySpec = new SecretKeySpec(skey.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivx.getBytes());

		Cipher cipher = getCypher(keySpec, ivSpec, Cipher.DECRYPT_MODE);

		// decode the message
		byte[] stringBytes = cipher.doFinal(encrypted);

		// converts the decoded message to a String
		String clear = new String(stringBytes, "UTF8");

		return clear;
	}

	public static Cipher getCypher(SecretKeySpec keySpec, IvParameterSpec ivSpec, int mode) throws InvalidKeyException
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

	// add parameter to parameter hashmap
	public void addParameter(String key, String value)
	{
		this.addParameters.put(key, value);
	}

	public String getParameter(String key)
	{
		return addParameters.get(key);
	}

	// set linkId for configuration file
	public void addEncryptedParameter(String key, byte[] value, String password)
	{
		try
		{
			this.encryptedParameters.put(key, encrypt(key, value, password));
		}
		catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getEncryptedParameter(String key, String password)
	{
		String para = null;
		try
		{
			para = decrypt(encryptedParameters.get(key), key, password);
		}
		catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return para;
	}

	// returns map of parameters
	public HashMap<String, String> getParametersMap()
	{
		return addParameters;
	}

	// returns map of encrypted parameters
	public HashMap<String, byte[]> getEncrypetdParameters()
	{
		return encryptedParameters;
	}

	public HashMap<String, HashMap> getConfigurationSet()
	{
		return configSetMap;
	}

	public void setConfigurationSet(ConfigurationSet configurationSet, String byteNumber, ArrayList rateList)
	{
		configurationSet = new ConfigurationSet(byteNumber, rateList);
	}

	/**
	 * Set the recording starting point. Only day, hour and minute will be assessed.
	 * 
	 * @param date
	 *            Starting point of the next measurement
	 */
	public void setRecordingStartTime(Date date)
	{
		this.recordingStartTime = date;
	}

	/**
	 * Set the recording duration for the next measurement.
	 * 
	 * @param duration
	 *            Duration of the next measurement in minutes
	 */
	public void setRecordingDuration(int duration)
	{
		this.durationMinutes = duration;
	}

	public Date getRecordingStartTime()
	{
		return this.recordingStartTime;
	}

	public int getDurationMinutes()
	{
		return this.durationMinutes;
	}
}
