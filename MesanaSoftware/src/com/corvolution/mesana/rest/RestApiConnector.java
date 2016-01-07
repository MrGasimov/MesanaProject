package com.corvolution.mesana.rest;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.corvolution.mesana.configurator.PropertyManager;


/**
 *This class represents manager for getting, sending data and pushing changes to server.
 */
public class RestApiConnector {
		
	CloseableHttpClient httpclient = HttpClients.createDefault();	
	PropertyManager conf = new PropertyManager();
	/**
	 * This method gets data from server.
	 *
	 * @param URL - to get data from.
	 * @return data as string from server 
	 */
	public String getMethod(String URL){
		CloseableHttpResponse response;
		HttpGet httpGet = new HttpGet(URL);
		
		String json=null;
		try {

			response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			
			if (statusCode == 200) {				
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				json = IOUtils.toString(inputStream, "UTF-8");
				
			}
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
		
		
	}
		
	/**
	 * This method pushes changes to server.
	 *
	 * @param json - string argument to push to server.
	 * @param url - to server that data would be pushed.
	 * @throws Exception , if connection to server failed or server could not response.
	 */
	public void postMethod(String json, String url) throws Exception {		
		
		HttpPost httpPost = new HttpPost(url);
		//httpPost.setConfig(params);
		httpPost.addHeader("Content-Type", "application/json");
		
		httpPost.setEntity(new StringEntity(json));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		} else {
			System.out.println(response.getStatusLine().getStatusCode());

		}

		response.close();
	}
	
	/**
	 * This method sends data to server.
	 *
	 * @param json data as string to send to server
	 * @param url path to server that data would be sent
	 * @throws IOException during IO processes 
	 * @throws ClientProtocolException Signals an error in the HTTP protocol.
	 * 
	 */
	public void putMethod(String json, String url) throws ClientProtocolException, IOException{		
		CloseableHttpResponse response ;
		HttpPut httpPut = new HttpPut(url);
		httpPut.addHeader("Content-Type", "application/json");
		httpPut.setEntity(new StringEntity(json));
		response = httpclient.execute(httpPut);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		} else {
			System.out.println(response.getStatusLine().getStatusCode());
		}
		response.close();
		
	}
	}
