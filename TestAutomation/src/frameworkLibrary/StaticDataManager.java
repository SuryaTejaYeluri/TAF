package frameworkLibrary;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import frameworkLibrary.Config;

public class StaticDataManager {

	public static HashMap<String, String> data;
	
	public static void readConfigFile(){
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
	 
			input = new FileInputStream(Config.staticData_DefaultFileName+".prop");
	 
			// load a properties file
			prop.load(input);
	 
			Enumeration<?> propertyKeys = prop.keys();
			data = new HashMap<String, String>();
			while(propertyKeys.hasMoreElements()){
				String key = (String)propertyKeys.nextElement();
				String value = prop.getProperty(key);
				data.put(key, value);
				
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
