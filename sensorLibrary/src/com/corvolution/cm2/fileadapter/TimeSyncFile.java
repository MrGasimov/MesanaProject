package com.corvolution.cm2.fileadapter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

public class TimeSyncFile extends BinaryFileAdapter
{
	public TimeSyncFile(String path)
	{
		super(path);
	}

	public void writeBinaryFile(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);				 
		
		byte year = (byte) (cal.get(Calendar.YEAR) - 2000); // Year 0 is mapped to year 2000
		byte month = (byte) cal.get(Calendar.MONTH + 1);
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		byte hour = (byte) cal.get(Calendar.HOUR_OF_DAY);
		byte minute = (byte) cal.get(Calendar.MINUTE);
		byte second = (byte) cal.get(Calendar.SECOND);
		byte[] buffer = {year, month, day, hour, minute, second};
		
		super.writeBinaryFile(buffer);
		
	}
}
