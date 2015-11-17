package com.corvolution.mesana.configurator;
import java.io.*;
import org.apache.commons.io.FileUtils;

public class ConfigSensor {
	static String source = "C:/Users/Gasimov/file.txt";
	static String dest  = "E:/file.txt";
	

	
	
	//returns true if configuration was successfull otherwise false
	public static boolean checkConfigState(){
		boolean success = false;
		File sourceFile = new File(source);
		File destFile = new File (dest);
		if(sourceFile.length()== destFile.length()){
			success = true;
		}
		return success;
	}
	
	//checks mock station for any connected sensor
	public static boolean checkSensorConnected(){
		boolean connected = false;
		File sensorFile = new File("E:/SensorInfo.txt");
		connected = sensorFile.exists();
		return connected;
		
	}
	
	
	//read configuration file from sensor
	public static String readConfigFile(String file) throws IOException{
	
			BufferedReader reader = new BufferedReader( new FileReader (file));
			String line = null;
			StringBuilder  stringBuilder = new StringBuilder();
		    String ls = System.getProperty("line.separator");
		
		    while( ( line = reader.readLine() ) != null ) {
		        stringBuilder.append( line );
		        stringBuilder.append( ls );
		    }
		    
		 reader.close();
		 return stringBuilder.toString();
	}
	
	public static String getSensorID() throws IOException{
		
		String sensorID = readConfigFile("E:/info.txt").substring(5, 10).trim();
		
		return sensorID;
		
	}
	
	
	
	
	
}