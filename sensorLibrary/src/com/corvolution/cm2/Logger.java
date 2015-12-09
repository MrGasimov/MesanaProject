package com.corvolution.cm2;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**This class functions as logger for library.Instead of logging information to file it only prints given messages on display
 * @author Suleyman Gasimov
 *
 */
public  class Logger
{
	public static String log=null;
	
	public static void printLog(String msg)
	{
		Logger.log = msg;
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);	
		Calendar calendar = Calendar.getInstance();
        Date date =  calendar.getTime();
		System.out.println(log +" "+sdf.format(date));
	}	
}
