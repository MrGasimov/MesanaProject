package com.corvolution.mesana.data;
import java.util.List;

import com.corvolution.mesana.rest.RestApiConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TaskCollection extends RestApiConnector {
	
	private List<Task> mTaskList;
	private List<Task> sTaskList;	
	TypeToken<List<Task>> token = new TypeToken<List<Task>>() {
	};
	
	public TaskCollection(String mID, String sID){
		setList(mID, sID);
	}
	
	public List<Task> getMeasTask(){
		return mTaskList;
	}
	public List<Task> getSensorTask(){
		return sTaskList;
	}
	public void setList(String mID, String sID){
		String mURL = "http://chili/mk/backend.mesana.com/api/v4/measurements/"+mID+"/logs?type=TASK_OPEN";
		String sURL = "http://chili/mk/backend.mesana.com/api/v4/sensors/"+sID+"/logs";
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mTaskList = gson.fromJson(getMethod(mURL), token.getType());
		sTaskList = gson.fromJson(getMethod(sURL), token.getType());
		
		
	}
	
}
