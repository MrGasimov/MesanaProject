package com.corvolution.mesana.configurator;
import java.io.InputStream;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author gasimov
 *
 */
public class AddressData implements RestApiConnector {
	
	static CloseableHttpClient httpclient = HttpClients.createDefault();
	public List <AddressData> addressList;
	TypeToken<List<AddressData>> token = new TypeToken<List<AddressData>>(){};

	private String salutation = "";
	private String firstName = "";
	private String lastName = "";
	private String city = "";
	private String street = "";
	private String zip = "";
	private String country = "";

	/**
	 * Constructor constructs the sensor address object of given measurement ID.
	 * @param id Measurement ID
	 */
	public AddressData(String id) {

		getMethod(id);
	}

	public String getCustomerData() {
		if(salutation.equals("m")){
			salutation ="Herr";
		}else {
			salutation = "Frau";
		}
		
		if(country.equalsIgnoreCase("Deutschland")){
			country = "";
		}else{
			country = country.toUpperCase();
		}
		firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
		lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
		city = city.toUpperCase();
		
		return salutation + "\r\n" + firstName + "\r\n" + lastName + "\r\n" + street + "\r\n" + zip
				+ "\r\n" + city + "\r\n" + country;
	}

	public String guiAddressData() {

		return firstName + " " + "\r\n" + lastName + " " + "\r\n" + city + " " + "\r\n" + country;

	}

	@Override
	public void getMethod(String measurementID) {
		
		String addressUrl = "http://chili/mk/backend.mesana.com/api/v4/measurements/" + measurementID
				+ "/addresses?type=sensor";

		HttpGet httpGet = new HttpGet(addressUrl);
		try {
			
			HttpResponse response = httpclient.execute(httpGet);
	        StatusLine statusLine = response.getStatusLine();
	        int statusCode = statusLine.getStatusCode();
	      
			
			if (statusCode == 200) {
				
				GsonBuilder builder = new GsonBuilder();
	            Gson gson = builder.create();
	        	HttpEntity entity = response.getEntity();
		        InputStream inputStream = entity.getContent();
		        String json = IOUtils.toString(inputStream, "UTF-8");	
		        addressList = gson.fromJson(json,token.getType());
		        String obj = gson.toJson(addressList.get(0));			
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(obj);

				if (element.isJsonObject()) {
					JsonObject jobj = element.getAsJsonObject();					
					salutation = jobj.get("salutation").getAsString();
					firstName = jobj.get("firstName").getAsString();
					lastName = jobj.get("lastName").getAsString();
					city = jobj.get("city").getAsString();					
					street = jobj.get("street").getAsString();
					zip = jobj.get("zip").getAsString();
					country = jobj.get("country").getAsString();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
