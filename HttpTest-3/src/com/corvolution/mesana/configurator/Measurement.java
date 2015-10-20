package com.corvolution.mesana.configurator;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Measurement implements RestApiConnector  {
	static CloseableHttpClient httpclient = HttpClients.createDefault();
	private  String id ="";
	private  String state ="";
	private  String stateChange = "";
	private  String linkId ="";
	private  String deliveryDate ="";
	private  String sensorId ="";
	private  String priority = "";

	
	
	
	public  String getMeasurementData(){
		
		return "ID: " +id +"\r\n"+"state: "+state+"\r\n"+"stateChange: "+stateChange+"\r\n"+"linkId: "+linkId+"\r\n"+"deliveryDate: "+deliveryDate+"\r\n"+"sensorID: " +sensorId+"\r\n"+"priority: "+priority;
		
	}
	
	public Date getDate(){
		DateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(deliveryDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}
	
	public String getPriority(){
		
		return priority;		
	}
	public String getID(){
		return id;
	}
	
	public void getMethod(String id) { 
			String sURL ="http://private-28ab5-mesana.apiary-mock.com/api/v3/measurements/EA32F01AD3193/";
			
			
			HttpGet httpGet = new HttpGet(sURL);
		    
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
			        	id =jobj.get("id").getAsString();
			        	state = jobj.get("state").getAsString();
			        	stateChange = jobj.get("stateChange").getAsString();
			        	linkId = jobj.get("linkId").getAsString();
			        	deliveryDate = jobj.get("name").getAsString();
			        	sensorId = jobj.get("sensorID").getAsString();      
			        	priority = jobj.get("priority").getAsString();
			        	}
		        }

		    }catch (Exception e) {
	        	e.printStackTrace();
	        }
		 		
		
	}
	
	public  void putMethod(String state,String user ) throws ClientProtocolException, IOException{
		 String putServerUri = "http://chili/mk/backend.mesana.com/api/v4/measurements/55154F1543B54/";
			try {
				 
				HttpPut httpPut = new HttpPut(putServerUri);
				httpPut.addHeader("Content-Type", "application/json");
				httpPut.addHeader("Accept", "application/json");				
				httpPut.setEntity(new StringEntity("{\"state\":\"" + state + "\",\"user\":\"" +user + "\",\"sensorId\":\"" +00047+"\"}", 
	                     ContentType.create("application/json")));				
				HttpResponse response = httpclient.execute(httpPut);
				
				if (response.getStatusLine().getStatusCode() != 200) {
	                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
	            }else{
	            	System.out.println(response.getStatusLine().getStatusCode());
	            	
	            }
				
			} finally {
				httpclient.close();
			}
	 }
	


	
	 
}
