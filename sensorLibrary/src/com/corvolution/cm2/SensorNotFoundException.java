package com.corvolution.cm2;

/**This class is custom exception for sensor library. When sensor is disconnected while initializing or configuring this exception is thrown by specific methods with message stating that Sensor not found.
 * @author Suleyman Gasimov
 *
 */
public class SensorNotFoundException extends Exception
{
	private String message;
	private static final long serialVersionUID = -3666409772339775926L;
	
		
	/**This constructor constructs object with a given String parameter as information about exception
	 * @param message about exception
	 */
	public SensorNotFoundException(String message)
	{
		super(message);
		this.message = message;
	}
	
	/**Second constructor constructs object with given Throwable parameter 
	 * @param cause of exception
	 */
	public SensorNotFoundException(Throwable cause)
	{
		super(cause);
	}
	/**Third constructor constructs object with several parameters as a alternate option.
	 * @param message - info about exception
	 * @param cause - the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.) 
	 * @see Throwable 
	 * @param enableSuppression - whether or not suppression is enabled or disabled
	 * @param writableStackTrace - whether or not the stack trace should be writable
	 */
	public SensorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message,cause,enableSuppression,writableStackTrace);
		this.message = message;
	}
	
	/**This method retrieves message holding information about exception.
	 * @return message
	 */
	public String getMessage()
	{
		return message;
	}

		
}
