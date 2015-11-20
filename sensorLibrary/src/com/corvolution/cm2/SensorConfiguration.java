package com.corvolution.cm2;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class SensorConfiguration {

	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 1;
	public static final byte[] startMode = { 1, 2, 3 };
	public byte[] configSet = { 1, 2, 3 };
	public byte[] recordDuration = { 2, 3, 4, 5, 6, 7 };
	public byte[] startTime = { 1, 3, 1, 1, 1, 5 };
	public byte latency = 0;
	public byte[] checksum = { 2, 2, 2, 2 };
	private HashMap<String, HashMap> configSetMap;
	private HashMap<String, String> addParameters;
	private HashMap<String,byte[]> encryptedParameters;

	
	public SensorConfiguration() {
		configSetMap = new HashMap<>();
		addParameters = new HashMap<>();
		encryptedParameters = new HashMap<>();
		// constructing default configset which is number 1--> configset[0]
		ConfigurationSet configSet = new ConfigurationSet();
		// default set for configFile
		configSetMap.put("1", configSet.getSet());
	}

	private static byte[] encrypt( String skey,byte[] message, String ivx) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
 
		SecretKeySpec keySpec = new SecretKeySpec(skey.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivx.getBytes());
 
		Cipher cipher = getCypher(keySpec, ivSpec, Cipher.ENCRYPT_MODE);
 
		// encrypt using the cypher
		byte[] raw = cipher.doFinal(message);
		
		// converts to base64 for easier display.
		//BASE64Encoder encoder = new BASE64Encoder();
		//String base64 = encoder.encode(raw);
 
		return raw;
	}
	
	public static String decrypt(byte[] encrypted, String skey, String ivx) throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException   {
		 
		 
		SecretKeySpec keySpec = new SecretKeySpec(skey.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivx.getBytes());
 
		Cipher cipher = getCypher(keySpec, ivSpec, Cipher.DECRYPT_MODE);
 
		// decode the BASE64 coded message
		//BASE64Decoder decoder = new BASE64Decoder();
		//byte[] raw = decoder.decodeBuffer(encrypted);
		
		// decode the message
		byte[] stringBytes = cipher.doFinal(encrypted);
		
		// converts the decoded message to a String
		String clear = new String(stringBytes, "UTF8");
		
		return clear;
	}
	
	public static Cipher getCypher(SecretKeySpec keySpec,IvParameterSpec ivSpec, int mode) throws InvalidKeyException  {
		// Get a cipher object.
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("invalid algorithm", e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException("invalid padding", e);
		}
		try {
			cipher.init(mode, keySpec, ivSpec);
		
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException("invalid algorithm parameter.", e);
		}
		return cipher;
	}
	
	
	// set linkId to add parameter file
	public void addParameter(String key, String value) {
		this.addParameters.put(key, value);
	}

	public String getParameter(String key) {
		return addParameters.get(key);
	}

	// set linkId for configuration file
	public void addEncryptedParameter(String key, byte[] value, String password) {					
		try {
			this.encryptedParameters.put(key,encrypt(key,value,password));
		} catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
	}

	public String getEncryptedParameter(String key, String password) {		
		String para = null;
		try {
			para = decrypt(encryptedParameters.get(key),key,password);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return para; 
	}

	// returns map of parameters
	public HashMap<String, String> getParametersMap() {
		return addParameters;
	}
	
	// returns map of  encrypted parameters 
	public HashMap<String, byte[]> getEncrypetdParameters() {
		return encryptedParameters;
	}
	
	
	public HashMap<String, HashMap> getConfigurationSet() {
		return configSetMap;
	}

	public void setConfigurationSet(ConfigurationSet configurationSet, String byteNumber, ArrayList rateList) {
		configurationSet = new ConfigurationSet(byteNumber, rateList);
	}

}
