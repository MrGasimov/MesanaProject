package com.corvolution.cm2.configuration;
import java.util.ArrayList;

/**This class represents start modes of sensor configuration defined by software designer
 * @author gasimov
 *
 */
public class StartModes
{
	private ArrayList <StartMode> startModeList = new ArrayList<>();
	
	/**Constructs class instance with three start mode objects. This class holds all defined mode's.
	 * 
	 */
	public StartModes(){
		startModeList.add(new StartMode((byte)1, "IMMEDIATELY", "starts measuring after disconnection from pc", "1.0"));
		startModeList.add(new StartMode((byte)2, "AFTER_ATTACHING", "starts measuring after attachment", "1.0"));
		startModeList.add(new StartMode((byte)4, "DEFINED_TIME", "starts measuring at defined time", "1.0"));
	}
	
	/**This method returns list holding start modes defined by sensor library
	 * @return ArrayList 
	 */
	public ArrayList<StartMode> getStartModeList(){
		return startModeList;
	}
}
