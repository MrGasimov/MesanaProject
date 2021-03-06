package com.corvolution.mesana.data;

import java.util.List;

import com.corvolution.mesana.rest.RestApiConnector;
import com.corvolution.mesana.utility.Constants;
import com.corvolution.mesana.utility.PropertyManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/** This class represents address data of customer.
 * @author Suleyman Gasimov
 *
 */
public class AddressData extends RestApiConnector
{

	private List<AddressData> addressList;
	TypeToken<List<AddressData>> token = new TypeToken<List<AddressData>>()
	{
	};
	private String salutation = "";
	private String firstName = "";
	private String lastName = "";
	private String city = "";
	private String street = "";
	private String zip = "";
	private String country = "";
	
	/**
	 * Initialization of a object given by measurement id.
	 *
	 * @param id of the given measurement
	 */
	public AddressData(String id)
	{
		setList(id);
	}
	
	/**
	 * Returns the list holding instances of this class.
	 *
	 * @return List - holds objects of this class
	 * 
	 */
	public List<AddressData> getList()
	{
		return addressList;
	}
	
	/**Returns customer information as String. 
	 * @return String
	 */
	public String getCustomerData()
	{
		if (salutation.equals("m"))
		{
			salutation = "Herr";
		}
		else
		{
			salutation = "Frau";
		}
		if (country.equalsIgnoreCase(PropertyManager.getInstance().getProperty(Constants.HOME_COUNTRY)))
		{
			country = "";
			city = city.toUpperCase();
		}
		else
		{
			city = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
		}
		country = country.toUpperCase();
		firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
		lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();

		return salutation + "\r\n" + firstName + " " + lastName + "\r\n" + street + "\r\n" + zip + " " + city + "\r\n"
				+ country;
	}

	/**This method only returns important informations for displaying to user.
	 * @return String
	 */
	public String guiAddressData()
	{
		String limiter = null;
		if (country.equalsIgnoreCase("Deutschland"))
		{
			country = "";
			limiter = ".";
		}
		return firstName + ", " + lastName + ", " + city + limiter + country;

	}

	/**
	 * This method initializes all class fields and adds instance to the list.
	 *
	 * @param mID the new list
	 */
	public void setList(String mID)
	{
		String addressUrl = PropertyManager.getInstance().getProperty("REST_PATH") + "measurements/" + mID + "/addresses?type=sensor";
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		addressList = gson.fromJson(getMethod(addressUrl), token.getType());
		String obj = gson.toJson(addressList.get(0));
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(obj);

		if (element.isJsonObject())
		{
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

}
