package com.corvolution.cm2;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionManager {
	  
	public static boolean state;
	public static int numberOfConnectedSensors;
	private static List<Sensor>sensorList = new CopyOnWriteArrayList<>();
	private static String sensorPath;
		
	public static  void setState(boolean status){
		state = status;
	}	
	public static boolean getState(){
		return state;
	}	
	public static void setCounter(int count){
		numberOfConnectedSensors = count;
	}	
	public static int getCounter(){
		return numberOfConnectedSensors;
	}	
	
	//adds connected sensor path to array 
	public static void addSensorToList(String path) throws IOException{
		Sensor sensor = new Sensor(path);
		sensorList.add(sensor);
		
	}
	//deletes disconnected sensor from array
	public static void removeSensorFromList(String path){
		for(Sensor device:sensorList){
			if(device.getSensorPath().equals(path))
					sensorList.remove(device);
		}				
	}
		
	//starts thread for listening and notifies Manager about any connection
	public void startListener(){
		Thread sensorListener = new Thread(new ConnectionListener());
		sensorListener.start();
	}
	
	//returns list of connected sensors
	public static List<Sensor> getConnectedSensorsList(){
		return sensorList;
	}	
	

	public static Sensor currentSensor(int i){
			
		return sensorList.get(i);
	}
	
	
	//sum of all connected sensors mearuementData
	public static long measurementDataSize(String option){
		long dataSize = 0; 
		if(option.equalsIgnoreCase("all")){
			for(Sensor device:sensorList){
				dataSize += device.sizeOfSensorData();			
			}
		} else if(option.equalsIgnoreCase("single")){
			dataSize += sensorList.get(0).sizeOfSensorData();
		}
	
		return dataSize;
	}

	
}