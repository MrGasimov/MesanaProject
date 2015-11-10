package com.corvolution.cm2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

import com.corvolution.mesana.configurator.PropertyManager;

import java.util.Date;

public class Sensor {
		
		/** 
		 * @uml.property name="sensorPath"
		 */
		private String sensorPath;
		private String deviceName;
		private String manufacturerName;
		private String serialNumber;
		private String firmwareVersion;		
		private Date flashDate;
		private double sensorVoltage;		
		PropertyManager manager = new PropertyManager();
		private SensorConfiguration sensorConfiguration;	
		//construct sensor object
		public Sensor(String path) throws IOException{
			
			sensorPath = path;
			readSensorInfo(path);
			String fileName =":/config.txt" ;
			readConfigFile(path, fileName);					
		}
			
		/** 
		 * @uml.property  name="sensorPath"
		 */
		public String  getSensorPath(){
			return sensorPath;
		}
		
		public String getDeviceName(){
			return deviceName;	
		}
		
		public String getManufacturerName(){
			return manufacturerName;	
		}
		
		public String getSerialNumber(){
			return serialNumber;	
		}
		x
		/** 
		 * @uml.property  name="flashDate"
		 */
		public Date getFlashDate(){
			return flashDate;
		}			
		/** 
		 * @uml.property  name="sensorVoltage"
		 */
		public double getSensorVoltage(){
			return sensorVoltage;
		}
		
		//read sensor info file from sensor
		private void readSensorInfo(String path) throws IOException{
			 String sensorInfo = ":/info.txt";					    		    
			 deviceName = readConfigFile(path,sensorInfo).get(0);
			 manufacturerName = readConfigFile(path,sensorInfo).get(1);
			 serialNumber = readConfigFile(path,sensorInfo).get(2).substring(13, 23);
			 firmwareVersion = readConfigFile(path,sensorInfo).get(3);
			 flashDate =  readConfigFile(path,sensorInfo).get(4);					
		}
		
		
		//read configuration file from sensor
		private List<String> readConfigFile(String sensorPath,String fileName) throws IOException{
			
			BufferedReader reader = new BufferedReader( new FileReader(sensorPath+ fileName));
			String line = null;
			List<String> list = new ArrayList<String>();		
		    while( ( line = reader.readLine() ) != null ) {	    
		    	list.add(line);        	        
		    }	
		    
		    reader.close();
		    return list;
		}
		
		//create new configuration object for writing to sensor
		public void setConfiguration(SensorConfiguration sensorConfiguration){					
			this.sensorConfiguration = sensorConfiguration;
//			return configObject;	
		}
		
		public SensorConfiguration getConfiguration(){
			return this.sensorConfiguration;
		}
		
		//write configuration file to sensor
		private void writeConfigFile() throws IOException{
			
			String configContent = newConfigFile().getLinkId() + "\r\n" + newConfigFile().getSampleRate();
			System.out.println(configContent+" config content");
			Files.write(Paths.get(sensorPath+":/config.txt"), configContent.getBytes());
			File source = new File(sensorPath+":/confige.txt");					   
		}
		

		
		//read measurement data from sensor		
		public void readMeasurementFromSensor(File dest, String measurementName){			
			
			File source =  new File(sensorPath+":/project");
				
			try {
				FileUtils.copyDirectoryToDirectory(source,dest);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		}
		
		public long getSizeOfData(){
			long size = FileUtils.sizeOf(new File(sensorPath+":/project"));
			return size;
		}
		/**
		 * Setter of the property <tt>sensorVoltage</tt>
		 * @param sensorVoltage  The sensorVoltage to set.
		 * @uml.property  name="sensorVoltage"
		 */
		public void setSensorVoltage(double sensorVoltage) {
			this.sensorVoltage = sensorVoltage;
		}
		/**
		 * Setter of the property <tt>flashDate</tt>
		 * @param flashDate  The flashDate to set.
		 * @uml.property  name="flashDate"
		 */
		public void setFlashDate(Date flashDate) {
			this.flashDate = flashDate;
		}
		{
		}

		/**
		 * Setter of the property <tt>sensorPath</tt>
		 * @param sensorPath  The sensorPath to set.
		 * @uml.property  name="sensorPath"
		 */
		public void setSensorPath(File sensorPath) {
			this.sensorPath = sensorPath;
		}

			
			/**
			 */
			public void update(){
				this.restartUsb();
			}

				
				/**
				 */
				public void disconnect(){
					 this.writeConfigFile();
					this.restartUsb();
				}

					
					/**
					 */
					private void restartUsb(){
						// Writing timesync
						// devcon restart
					}
		

}
