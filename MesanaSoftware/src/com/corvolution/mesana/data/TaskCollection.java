package com.corvolution.mesana.data;

import java.util.List;

import com.corvolution.mesana.configurator.PropertyManager;
import com.corvolution.mesana.rest.RestApiConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**This class represents object holding Task instances.
 * @author Suleyman Gasimov
 *
 */
public class TaskCollection extends RestApiConnector
{
	private List<Task> mTaskList;
	private List<Task> sTaskList;
	TypeToken<List<Task>> token = new TypeToken<List<Task>>()
	{
	};

	/**
	 * Constructs and initializes instance of this class.
	 *
	 * @param mID the m id
	 * @param sID the s id
	 */
	public TaskCollection(String mID, String sID)
	{
		setList(mID, sID);
	}

	/**
	 * Returns list holding measurement tasks.
	 *
	 * @return List<Task>
	 */
	public List<Task> getMeasTask()
	{
		return mTaskList;
	}

	/**
	 * Returns list holding sensor tasks.
	 *
	 * @return List<Task>
	 */
	public List<Task> getSensorTask()
	{
		return sTaskList;
	}

	/**
	 * This method initializes measurement task instances, sensor task instances and stores them appropriate lists.  
	 *
	 * @param mID the m id
	 * @param sID the s id
	 */
	public void setList(String mID, String sID)
	{
		String mURL = PropertyManager.getInstance().getProperty("REST_PATH")+"measurements/" + mID + "/logs?type=TASK_OPEN";
		String sURL = PropertyManager.getInstance().getProperty("REST_PATH")+"sensors/" + sID + "/logs";
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mTaskList = gson.fromJson(getMethod(mURL), token.getType());
		sTaskList = gson.fromJson(getMethod(sURL), token.getType());
	}

}
