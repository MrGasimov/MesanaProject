package com.corvolution.mesana.configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Printer{
	
	public List<String> printList ;
	public HashMap<Integer, String> hmap = new HashMap<Integer, String>();
	
	public void printLabel(){
		
	}
	
	public void setTemplate(){
		ConfigApp conf =  new ConfigApp();
		conf.getProperty("LABEL_TEMPLATE");
	}
	
	public void addParameter(){
		
	}

	public void print(){
		
	}
}


