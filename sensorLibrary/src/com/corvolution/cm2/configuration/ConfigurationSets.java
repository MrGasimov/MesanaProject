package com.corvolution.cm2.configuration;

import java.util.ArrayList;

/**This class is used to construct and hold configurationSet instances
 * @author Suleyman Gasimov
 *
 */
public class ConfigurationSets
{
	private ArrayList <ConfigurationSet> configSetList = new ArrayList<>();
	
	/**Default constructor for constructing and adding to list a default sensor configurationSet
	 * 
	 */
	public ConfigurationSets(){
		configSetList.add(new ConfigurationSet((byte)1, "mesana", "default mesana configuration set", "1.0"));
	}
	
	/**This method retrieves list holding configurationSets instances
	 * @return
	 */
	public ArrayList<ConfigurationSet> getConfigSetList(){
		return configSetList;
	}
}
