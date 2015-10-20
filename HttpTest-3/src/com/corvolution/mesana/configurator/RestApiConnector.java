package com.corvolution.mesana.configurator;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.reflect.TypeToken;

public interface RestApiConnector {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public void getMethod(String para1);
		

	}
