package com.corvolution.mesana.configurator;
import java.util.HashMap;
import java.util.List;

public class Printer{
	
	/**
	 * @uml.property  name="printList"
	 */
	public List<String> printList ;
	/**
	 * @uml.property  name="hmap"
	 */
	public HashMap<String , String> hmap = new HashMap<String, String>();
		
	
	public void setTemplate(String labelTemplate){
		
		String label;
	}
	
	//adding parameters to map e.g linkId and address
	public void addParameter(String key, String value){
		hmap.put(key, value);
	}

	public void print(){
		
	}
}


