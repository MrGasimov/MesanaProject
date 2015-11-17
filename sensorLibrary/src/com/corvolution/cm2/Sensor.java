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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
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
		private String sensorVoltage;
		private String currentState;
		private String sensorSystemTime;
				
		private SensorConfiguration sensorConfiguration;	
		
		//construct sensor object		
		public Sensor(String path) throws IOException{
			sensorConfiguration = new SensorConfiguration();
			this.sensorPath = path;
			readSensorInfo();
			if(new File(path+":/config.cm2").exists()){
				readConfigFile();
			}			
			readCustomFile();
			readFeedBackFile();
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
		
		public String getSensorVoltage(){
			return sensorVoltage;
		}
		public String getFirwareVersion(){
			return  firmwareVersion;
		}
		
		public ArrayList<String> fileReader(String absolutePath) throws IOException{
			BufferedReader reader = new BufferedReader( new FileReader(absolutePath));
			String line = null;
			ArrayList<String> list = new ArrayList<String>();		
			
			while( ( line = reader.readLine() ) != null ) {
				list.add(line);        	        
			}				    
			 reader.close();
			return list;			 
		}
		
		//read sensor info file from sensor
		private void readSensorInfo() throws IOException{
			 
			String absolutePath =sensorPath+":/info.txt";			 
			deviceName =fileReader(absolutePath).get(0);
			manufacturerName = fileReader(absolutePath).get(1);
			serialNumber = fileReader(absolutePath).get(2);
			firmwareVersion = fileReader(absolutePath).get(3);
			flashDate =  fileReader(absolutePath).get(4);					
		}
		
		
		//read configuration file from sensor
		private void readConfigFile() throws IOException{
			String absolutePath = sensorPath+":/config.cm2";
			try {
				byte[] buffer = new byte[21];
				FileInputStream inputStream = new FileInputStream(absolutePath);
				BufferedInputStream out = new BufferedInputStream(inputStream);
				int total = 0;
		        int nRead = 0;
		        
		        while((nRead = inputStream.read(buffer)) != -1) {
		        	total += nRead;	
		        	System.out.println(nRead);
	            }  
		        
		        inputStream.close();  				
			}catch(FileNotFoundException ex){
					ex.printStackTrace();
	          	}catch(IOException ex) {
	                ex.printStackTrace();
	            }
			
		}
		
		//write configuration file to sensor		
		public void writeConfigFile() throws IOException{
			String absolutePath =sensorPath+":/config.cm2";
			
			byte[] buffer = {SensorConfiguration.VERSION_MAJOR,SensorConfiguration.VERSION_MINOR,SensorConfiguration.startMode[0],
							 sensorConfiguration.configSet[0],sensorConfiguration.recordDuration[0],
							 sensorConfiguration.recordDuration[1],sensorConfiguration.recordDuration[2],
							 sensorConfiguration.recordDuration[3],sensorConfiguration.recordDuration[4],
							 sensorConfiguration.recordDuration[5],sensorConfiguration.startTime[0],
							 sensorConfiguration.startTime[1],sensorConfiguration.startTime[2],
							 sensorConfiguration.startTime[3],sensorConfiguration.startTime[4],
							 sensorConfiguration.startTime[5],sensorConfiguration.latency,sensorConfiguration.checksum[0],
							 sensorConfiguration.checksum[1],sensorConfiguration.checksum[2],sensorConfiguration.checksum[3]};
			
			FileOutputStream outputStream = new FileOutputStream(absolutePath);
            BufferedOutputStream out = new BufferedOutputStream(outputStream);
            outputStream.write(buffer);
            out.flush();
            outputStream.close();               									 		   					   
		}
	
		public void writeTimeSyncFile()throws IOException{
			String absolutePath =sensorPath+":/timesync.cm2";
			
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);			
			byte year = 35;
			year += (byte) (cal.get(Calendar.YEAR)-2015);
			byte month = (byte) cal.get(Calendar.MONTH);
			byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
			byte hour  = (byte) cal.get(Calendar.HOUR_OF_DAY); 
			byte minute  = (byte) cal.get(Calendar.MINUTE); 
			byte second  = (byte) cal.get(Calendar.SECOND);  
			byte[] buffer = {year,month,day, hour, minute,second};
			
			FileOutputStream outputStream = new FileOutputStream(absolutePath);
            BufferedOutputStream out = new BufferedOutputStream(outputStream);
            outputStream.write(buffer);
            out.flush();
            outputStream.close();               							
			
			
		}
		public SensorConfiguration getConfiguration(){
			return this.sensorConfiguration;
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
		
		
		public void readCustomFile() throws IOException{
			 String absolutePath =sensorPath+":/custom.txt";			
			 sensorConfiguration.addParameter("LinkId", fileReader(absolutePath).get(0).substring(7,13));
			
		}
		
		public void readFeedBackFile() throws IOException{
			String absolutePath =sensorPath+":/status.txt";			
			sensorVoltage = fileReader(absolutePath).get(0).substring(15, 18);
			currentState = fileReader(absolutePath).get(1).substring(13, 22);
			sensorSystemTime = fileReader(absolutePath).get(2).substring(11, 30);
		}

		
		public long getDataSize(){
			long size = FileUtils.sizeOf(new File(sensorPath+":/project"));
			return size;
		}
		
		//method for time synchronizing
		public void synchronize(){
			try {
				writeTimeSyncFile();
				disconnect("restart");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
		}

		//removing sensor	
		public void disconnect(String command) throws IOException{
			//remove Sensor			
			ProcessBuilder builder = new ProcessBuilder( "cmd.exe", "/c", "cd \"C:/Users/Gasimov/Desktop\" && devcon.exe "+command+" *VID_05E3*");
			builder.redirectErrorStream(true);
			Process p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String line;
		     while (true) {
		        line = r.readLine();
	            if (line == null) { break; }
	            System.out.println(line);
	        }
		}


}