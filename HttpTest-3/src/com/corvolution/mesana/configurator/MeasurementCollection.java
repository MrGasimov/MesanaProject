package com.corvolution.mesana.configurator;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MeasurementCollection implements RestApiConnector {
	
	CloseableHttpClient httpclient = HttpClients.createDefault();
	public List <Measurement> measList;
	TypeToken<List<Measurement>> token = new TypeToken<List<Measurement>>(){};
	
	public void sortListbyDate(){
		Collections.sort(measList, new CustomComparator());
		
	} 
	public void priorityFilter(){
		Collections.sort(measList, new FilterComparator());
	}
	@Override
	public void getMethod(String id) {
		
		String sURL ="http://chili/mk/backend.mesana.com/api/v4/measurements?state=WAIT_FOR_CONFIG";
		HttpGet httpGet = new HttpGet(sURL);
	    
	    try {

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
		        measList = gson.fromJson(json, token.getType());
		        sortListbyDate();
		        
	        }
	        

	    }catch (Exception e) {
        	e.printStackTrace();
        }
		
	}
	
	
	

}
