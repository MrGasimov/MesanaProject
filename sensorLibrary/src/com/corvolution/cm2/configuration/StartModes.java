package com.corvolution.cm2.configuration;

import java.util.ArrayList;

public class StartModes
{
	private ArrayList <StartMode> startModeList = new ArrayList<>();
	
	public StartModes(){
		startModeList.add(new StartMode((byte)1, "IMMEDIATELY", "starts measuring after disconnection from pc", "1.0"));
		startModeList.add(new StartMode((byte)2, "AFTER_ATTACHING", "starts measuring after attachment", "1.0"));
		startModeList.add(new StartMode((byte)4, "DEFINED_TIME", "starts measuring at defined time", "1.0"));
	}
	
	public ArrayList<StartMode> getStartModeList(){
		return startModeList;
	}
}
