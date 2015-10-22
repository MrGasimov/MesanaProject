package com.corvolution.mesana.configurator;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.swt.widgets.Display;

import gui.GuiBuilder;
import gui.OperatorAccess;


public class FindSensor implements Runnable {
	static String status;	
	PrintStream state; 
	ConnectionState stateObj;
	
	
	public void run(){
		
		sensorListener();
	}
	
	
	public void sensorListener()
    {
		
	    String[] letters = new String[]{ "A", "B", "C", "D", "E", "F", "G", "H", "I","J","K","L","M"};
	    File[] sensors = new File[letters.length];
	    boolean[] isDrive = new boolean[letters.length];

	    // init the file objects and the initial drive state
	    for ( int i = 0; i < letters.length; ++i )
        {
        sensors[i] = new File(letters[i]+":/");

        isDrive[i] = sensors[i].canRead();
        }

	    System.out.println("Find Sensor: waiting for devices...");
     
	     // loop indefinitely
	     while(true)
        {
        // check each drive 
        for ( int i = 0; i < letters.length; ++i )
            {
            boolean pluggedIn = sensors[i].canRead();

            // if the state has changed output a message
            if ( pluggedIn != isDrive[i] )
                {
                 if( pluggedIn ){
                	 status = "Sensor "+letters[i]+" has been plugged in";
                	 System.out.println("Sensor "+letters[i]+" has been plugged in");
                
                	 ConnectionState.setTrueState();
             	 }  
                 else{
                	 status = "Sensor "+letters[i]+" has been unplugged in";
                	 System.out.println("Sensor "+letters[i]+" has been unplugged");
                	 ConnectionState.setFalseState();
                	                	 
                	 //System.out.println(state);
                	 
                 }
                	
                 isDrive[i] = pluggedIn;
                
                }
            }
        	
	        // wait before looping
	        try { Thread.sleep(100); }
	        catch (InterruptedException e) { /* do nothing */ }
	       
        	}
	  
	     	
    }
	
	
}
