package com.corvolution.cm2.fileadapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import com.corvolution.cm2.SensorNotFoundException;

public class BinaryFileAdapter
{	
	String path;
	
	public BinaryFileAdapter(String path){
		this.path = path;
	}
	
	public void writeBinaryFile( byte[] buffer)throws SensorNotFoundException{
		
			FileOutputStream outputStream;
			try
			{
				outputStream = new FileOutputStream(path);
				BufferedOutputStream out = new BufferedOutputStream(outputStream);
				outputStream.write(buffer);
				out.flush();
				outputStream.close();		
			}
			catch (IOException e)
			{
				throw new SensorNotFoundException("Sensor not found!");
			}
			
			
		
		
	}
	
	public byte[] readBinaryFile()throws SensorNotFoundException{
		byte[] buffer = new byte[33];
		try
		{			
			FileInputStream inputStream = new FileInputStream(path);
			BufferedInputStream bufferedOutStream = new BufferedInputStream(inputStream);
			int nRead = 0;
			CRC32 myCRC = new CRC32();
			while ((nRead = bufferedOutStream.read(buffer)) != -1)
			myCRC.update(buffer, 0, 31);
			inputStream.close();
		}
		catch (IOException ex)
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
		return buffer;
	}
}
