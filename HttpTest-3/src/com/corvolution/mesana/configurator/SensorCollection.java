package com.corvolution.mesana.configurator;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SensorCollection implements RestApiConnector{
	
	/**
	 * @uml.property  name="sURL"
	 */
	public String sURL ="http://chili/mk/backend.mesana.com/api/v4/sensors/";	
	/**
	 * @uml.property  name="sensorList"
	 */
	private List<SensorData> sensorList;
	/**
	 * @uml.property  name="token"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	TypeToken<List<SensorData>> token = new TypeToken<List<SensorData>>(){};
	
	public  List<SensorData> getList(){
		return sensorList;
	}
	
	@Override
	public void getMethod(String para1){
		// TODO Auto-generated method stub
		
		HttpGet httpGet = new HttpGet(sURL);		
		
		try{
        HttpResponse response = httpclient.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        System.out.println(statusCode);
       
        if (statusCode == 200) {
        	GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
        	HttpEntity entity = response.getEntity();
	        InputStream inputStream = entity.getContent();
	        String json = IOUtils.toString(inputStream, "UTF-8");	
	        sensorList = gson.fromJson(json, token.getType());
	       
        }
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	
	
	
}
