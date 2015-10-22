package com.corvolution.mesana.configurator;
import java.io.*;
import org.apache.commons.io.FileUtils;

public class ConfigSensor {
	static String source = "C:/Users/Gasimov/file.txt";
	static String dest  = "E:/file.txt";
	
	public static void streamCopy() throws IOException {
		
		
		
		File  sourceFile= new File(source);
		File destFile = new File(dest);
		InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(sourceFile);
	        os = new FileOutputStream(destFile);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
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
	
	public static boolean checkSensorConnected(){
		boolean connected;
		File sensorFile = new File("E:/SensorInfo.txt");
		connected = sensorFile.exists();
		return connected;
		
	}
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
	

	public static void copyApache(File source , File dest ) throws IOException {
	    FileUtils.copyFile(source, dest);
	}
	
	
	
	
}
