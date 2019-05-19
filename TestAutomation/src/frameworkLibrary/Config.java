package frameworkLibrary;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	//Settings
	public static String closeBrowserAfterExecution;
	public static int waitTimeInSeconds;
	public static String testSuiteNames;

	//Default sheet names
	public static String staticData_DefaultFileName;
	public static String testData_DefaultSheetName;

	//Project Details
	public static String projectName;	
	public static boolean passedStepsScreenshot;
	public static boolean failedStepsScreenshot;

	//Browser Config
	public static String browserName;
	
	//Website Config
	public static String URL;
	public static String environment;

	public static void readConfigFile(){
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("config.prop");

			// load a properties file
			prop.load(input);

			// get the property value 
			projectName = prop.getProperty("Project_Name");

			staticData_DefaultFileName = prop.getProperty("StaticData_DefaultFileName");
			testData_DefaultSheetName = prop.getProperty("testData_DefaultSheetName");

			if(prop.getProperty("PassedSteps").equalsIgnoreCase("True"))
				passedStepsScreenshot = true;
			else
				passedStepsScreenshot = false;

			if(prop.getProperty("FailedSteps").equalsIgnoreCase("True"))
				failedStepsScreenshot = true;
			else
				failedStepsScreenshot = false;

			waitTimeInSeconds = Integer.parseInt(prop.getProperty("WaitTimeInSeconds"));			

			closeBrowserAfterExecution = prop.getProperty("CloseBrowserAfterExecution");

			testSuiteNames = prop.getProperty("TestSuiteName");

			browserName = prop.getProperty("BrowserName");
			
			environment = prop.getProperty("Environment");
			
			URL = prop.getProperty("URL");

		}catch (IOException ex) {
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
