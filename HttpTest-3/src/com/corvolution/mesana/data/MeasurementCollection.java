package com.corvolution.mesana.data;
import java.util.Collections;
import java.util.List;
import com.corvolution.mesana.configurator.CustomComparator;
import com.corvolution.mesana.configurator.HighFilter;
import com.corvolution.mesana.configurator.LowFilter;
import com.corvolution.mesana.configurator.MediumFilter;
import com.corvolution.mesana.rest.RestApiConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MeasurementCollection extends  RestApiConnector {	
		
	private List<Measurement> measList;	
	String sURL = "http://chili/mk/backend.mesana.com/api/v4/measurements?state=WAIT_FOR_CONFIG";
	TypeToken<List<Measurement>> token = new TypeToken<List<Measurement>>() {
	};
	
	
	public List<Measurement> getList(){
		return measList;
	}
	
	public void sortListbyDate() {
		Collections.sort(getList(), new CustomComparator());

	}

	public void highPriorityfilter() {
		Collections.sort(measList, new 	HighFilter());
	}
	
	public void mediumPriorityfilter() {
		Collections.sort(measList, new 	MediumFilter());
	}
	
	public void lowPriorityfilter() {
		Collections.sort(measList, new 	LowFilter());
	}
	//get id of measurement of a given position in list 
	public String getMesID(int index){
		String id= measList.get(index).getID();
		return id;
	}
	public void setList(){		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		measList = gson.fromJson(getMethod(sURL), token.getType());
	
	}
	

}
