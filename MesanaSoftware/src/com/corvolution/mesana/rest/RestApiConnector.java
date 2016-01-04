package com.corvolution.mesana.rest;
import java.io.InputStream;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.corvolution.mesana.configurator.PropertyManager;


/**
 * The Class RestApiConnector.
 */
public class RestApiConnector {
		
	/** The httpclient. */
	CloseableHttpClient httpclient = HttpClients.createDefault();	
	
	/** The conf. */
	PropertyManager conf = new PropertyManager();
	
	
	/**
	 * Gets the method.
	 *
	 * @param URL the url
	 * @return the method
	 */
	public String getMethod(String URL){
		CloseableHttpResponse response;
		HttpGet httpGet = new HttpGet(URL);
		
		//httpGet.setConfig(params);
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
	 * Post method.
	 *
	 * @param json the json
	 * @param url the url
	 * @throws Exception the exception
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
	 * Put method.
	 *
	 * @param json the json
	 * @param url the url
	 * @throws Exception the exception
	 */
	public void putMethod(String json, String url) throws Exception {		
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
