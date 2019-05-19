package frameworkLibrary;

public class BatchFileConfig {
	
	public static void setConfigValue(String Value){
		String[] parameters =Value.split("=");
		if(parameters[0].equalsIgnoreCase("browserName")){
			Config.browserName=parameters[1];
		}else if(parameters[0].equalsIgnoreCase("Executiondelay")){
			long wait = Long.parseLong(parameters[1]);
			WebPage.sleepAtLeast(wait);
		}else if(parameters[0].equalsIgnoreCase("TestSuiteName")){
			Config.testSuiteNames=parameters[1];
		}else if(parameters[0].equalsIgnoreCase("Environment")){
			Config.environment = parameters[1];
		}else{
			System.out.println("Please check your batch command");
		}
		
	}

}
