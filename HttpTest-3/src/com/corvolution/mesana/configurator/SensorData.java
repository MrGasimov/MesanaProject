package com.corvolution.mesana.configurator;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SensorData implements RestApiConnector{
	static CloseableHttpClient httpclient = HttpClients.createDefault();
	private  String id = "";
	private  String state = "";
	private  String firmware = "";	
	private  String name = "";
	private  String type = "";


	public SensorData(){
		
		
	}
	
	public  String getSensorData(){
		
		return "id: " + id +"\r\n" +"state: "+ state + "\r\n"+"firmware: "+firmware +"\r\n"+"name: "+ name +"\r\n"+"type: "+type;
		
	}
	public String getID(){
		return id;
	}
	public  void getMethod(String id) {
		String sensorURL = "http://chili/mk/backend.mesana.com/api/v4/sensors/"+id;
		HttpGet httpGet = new HttpGet(sensorURL);
		try {

	        HttpResponse response = httpclient.execute(httpGet);
	        StatusLine statusLine = response.getStatusLine();
	        int statusCode = statusLine.getStatusCode();
	        
	        if (statusCode == 200) {
	        	
	        	HttpEntity entity = response.getEntity();
		        InputStream inputStream = entity.getContent();
		        String json = IOUtils.toString(inputStream, "UTF-8");	
		        JsonParser parser = new JsonParser();
		        JsonElement element = parser.parse(json);
		       
		        if (element.isJsonObject()) {		       
		        JsonObject jobj = element.getAsJsonObject();
		        id = jobj.get("id").getAsString();
		        state = jobj.get("state").getAsString();
	        	firmware = jobj.get("firmware").getAsString();	      
	        	name = jobj.get("name").getAsString();
	        	type = jobj.get("type").getAsString();
	        	
	        	}
	        }

	    }catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
}

