package com.corvolution.cm2;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SensorPropertyManager {
	private static Properties info, custom,status;
	private static SensorPropertyManager instance;

	public SensorPropertyManager(String path) {
		try {
			info = new Properties();
			custom = new Properties();
			status = new Properties();
			info.load(new FileInputStream(path+":/info.txt"));
			custom.load(new FileInputStream(path+":/custom.txt"));
			status.load(new FileInputStream(path+":/status.txt"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public String getInfoProperty(String property) {
		return info.getProperty(property);
	}
	public String getCustomProperty(String property) {
		return custom.getProperty(property);
	}
	public String getStatusProperty(String property) {
		return status.getProperty(property);
	}
	


}
