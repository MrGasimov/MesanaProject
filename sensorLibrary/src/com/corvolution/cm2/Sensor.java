package com.corvolution.cm2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import java.util.Date;

public class Sensor {
		
		private String sensorPath;
		private String deviceName;
		private String manufacturerName;
		private String serialNumber;
		private String firmwareVersion;		
		private String flashDate;
		private double sensorVoltage;		
		private SensorConfiguration sensorConfiguration;	
		
		//construct sensor object		
		public Sensor(String path) throws IOException{			
			this.sensorPath = path;
			readSensorInfo(path);
			String fileName =":/config.txt" ;
			readConfigFile(path, fileName);					
		}
			
		
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
		
		public String getFlashDate(){
			return flashDate;
		}			
		
		public double getSensorVoltage(){
			return sensorVoltage;
		}
		
		//read sensor info file from sensor
		private void readSensorInfo(String path) throws IOException{
			 
			String sensorInfo = ":/info.txt";
			BufferedReader reader = new BufferedReader( new FileReader(path+sensorInfo));
			String line = null;
			List<String> list = new ArrayList<String>();		
			
			while( ( line = reader.readLine() ) != null ) {
				list.add(line);        	        
			}				    
			 reader.close();			 
			deviceName =list.get(0);
			manufacturerName = list.get(1);
			serialNumber = list.get(2).substring(13, 23);
			firmwareVersion = list.get(3);
			flashDate =  list.get(4);					
		}
		
		
		//read configuration file from sensor
		private void readConfigFile(String sensorPath,String fileName) throws IOException{
			String absolutePath = sensorPath+fileName;
			try {
				byte[] buffer = new byte[1000];
				FileInputStream inputStream = new FileInputStream(absolutePath);
				int total = 0;
		        int nRead = 0;
		        
		        while((nRead = inputStream.read(buffer)) != -1) {
	                // Convert to String so we can display it.
	                // Of course you wouldn't want to do this with
	                // a 'real' binary file.
	                //System.out.println(new String(buffer));
		        	System.out.println(nRead);
		        	total += nRead;
	                
	            }  
		        
		        inputStream.close();  				
			}catch(FileNotFoundException ex){
					ex.printStackTrace();
	          	}catch(IOException ex) {
	                ex.printStackTrace();
	            }
			
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
		public void writeConfigFile(String destPath) throws IOException{
									
			String bytes = null ;						 		    
			Files.write(Paths.get(destPath), bytes.getBytes());
			File source = new File(sensorPath+":/confige.txt");					   
		}
		
		//read measurement data from sensor		
		public void readMeasurementFromSensor(String dest){			
			File destination = new File(dest);
			File source =  new File(sensorPath+":/project");				
			try {
				FileUtils.copyDirectoryToDirectory(source,destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 
		}
		
		public long getSizeOfData(){
			long size = FileUtils.sizeOf(new File(sensorPath+":/project"));
			return size;
		}
		
		public void setSensorVoltage(double sensorVoltage) {
			this.sensorVoltage = sensorVoltage;
		}
		
		public void setFlashDate(String flashDate) {
			this.flashDate = flashDate;
		}
		

		public void update(){
				this.restartUsb();
			}

			
		public void disconnect() throws IOException{
			this.restartUsb();
		}

		private void restartUsb(){
			// Writing timesync
			// devcon restart
		}


}
