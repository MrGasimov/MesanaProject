package com.corvolution.mesana.rest;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.corvolution.mesana.configurator.PropertyManager;


public class RestApiConnector {
	
	public static CloseableHttpClient httpclient = HttpClients.createDefault();
	PropertyManager conf = new PropertyManager();
	public String getMethod(String URL){
		
		HttpGet httpGet = new HttpGet(URL);
		String json=null;
		try {

			HttpResponse response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {				
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				json = IOUtils.toString(inputStream, "UTF-8");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
		
	public void postMethod(String json, String url) throws Exception {		
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("Accept", "application/json");
		httpPost.setEntity(new StringEntity(json));
		HttpResponse response = httpclient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		} else {
			System.out.println(response.getStatusLine().getStatusCode());

		}

		//httpclient.close();

	}
	
	public void putMethod(String json, String url) throws Exception {		
		
		HttpPut httpPut = new HttpPut(url);
		httpPut.addHeader("Content-Type", "application/json");
		httpPut.addHeader("Accept", "application/json");
		httpPut.setEntity(new StringEntity(json));
		HttpResponse response = httpclient.execute(httpPut);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		} else {
			System.out.println(response.getStatusLine().getStatusCode());
		}

		//httpclient.close();

	}
	}