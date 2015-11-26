package com.corvolution.cm2.fileadapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class BinaryFileAdapter
{
	public void writeBinaryFile(String path, byte[] buffer){
		
		try
		{
			FileOutputStream outputStream = new FileOutputStream(path);
			BufferedOutputStream out = new BufferedOutputStream(outputStream);
			outputStream.write(buffer);
			out.flush();
			outputStream.close();
			
		}catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void readBinaryFile(String path){
		try
		{
			byte[] buffer = new byte[33];
			FileInputStream inputStream = new FileInputStream(path);
			BufferedInputStream bufferedOutStream = new BufferedInputStream(inputStream);
			int total = 0;
			int nRead = 0;
			CRC32 myCRC = new CRC32();
			while ((nRead = bufferedOutStream.read(buffer)) != -1)
			{
				total += nRead;
				System.out.println(nRead);
				System.out.println(total);
			}
			myCRC.update(buffer, 0, 31);
			inputStream.close();
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
