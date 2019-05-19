package frameworkLibrary;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


public class WebDriverFactory {

	private static WebDriver driver = null;
	private static String userDirectory = System.getProperty("user.dir");	
	
	public static void createWebDriverObject(String browserName){
		
		//resourcesPath = resourcesPath.replaceAll("(?:[^\\\\]+\\Z)", "Resources");
		if(browserName.equalsIgnoreCase("IE")){
			System.setProperty("webdriver.ie.driver", userDirectory+File.separator+"Resources"+File.separator+"IEDriverServer.exe");
			DesiredCapabilities ieCapabilities = null;
			ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setBrowserName("internet explorer");
			ieCapabilities.setVersion("11");
			ieCapabilities.setCapability("nativeEvents",false);
			driver = new InternetExplorerDriver(ieCapabilities);
		}
		else if(browserName.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", userDirectory+File.separator+"Resources"+File.separator+"chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions", "--no-sandbox");
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();
		}
		else
			System.out.println("Please check the browser name given, supports only chrome and ie");
	}//EOM
	
	public static WebDriver getWebDriverObject(){
		return driver;
	}//EOM
	
	public static void destroyWebDriverObject(){
		if(Config.closeBrowserAfterExecution.equalsIgnoreCase("Yes")){
			driver.close();
			driver.quit();	
			driver = null;
		}
	}//EOM
}
