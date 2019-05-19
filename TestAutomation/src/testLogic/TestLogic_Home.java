package testLogic;

import frameworkLibrary.Config;
import frameworkLibrary.DataBook;
import frameworkLibrary.Locator;
import frameworkLibrary.Report;
import frameworkLibrary.WebPage;
import uiMap.Home;

public class TestLogic_Home extends WebPage{
	
	public TestLogic_Home(){
		super();
	}
	
	public void openApp(){
		String URL = Config.URL;
		
		driver.get(URL);
		waitForPageToLoad();
		
		if(isDisplayed(Home.txtGoogleSearch))
			Report.updateReport("User is navigated to Google home page", "Pass");
		else
			Report.updateReport("User is NOT navigated to Google home page", "Fail");
		
		
		
	}//EOM
	
	public static void enterSearchTerm(){
		String searchTerm = DataBook.getData(Config.testData_DefaultSheetName, "FirstName");
		typeIn(Home.txtGoogleSearch, searchTerm);
		
		Report.updateReport("Search term is entered", "Pass");
	}
}
