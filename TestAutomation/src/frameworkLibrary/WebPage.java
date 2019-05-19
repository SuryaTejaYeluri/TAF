package frameworkLibrary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WebPage {
	public static WebDriver driver;

	protected WebPage(){
		driver = WebDriverFactory.getWebDriverObject();
	}


	public static void captureScreenshotAndSave(){
		File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		try {
			ControlData.currScreenshotName = ControlData.currTestcaseScreenshotDirectory + File.separator + ControlData.testcaseScreenshotCount++ + ".png";
			FileUtils.copyFile(src, new File(ControlData.currScreenshotName));
		}catch (IOException e){}
	}
	
	
	public static void snooze(long secs){	
		try {
			Thread.sleep(secs);
		} catch (InterruptedException e) {
			System.out.println("Sleep Interrupted (Exception Caused)");
			e.printStackTrace();
		}
	}


	public static void sleepAtLeast(long totalMilliSecondsToWait) {
		long startTime = System.currentTimeMillis();
		long milliSecondsLeft = totalMilliSecondsToWait;
		while (milliSecondsLeft > 0) {
			try {
				Thread.sleep(milliSecondsLeft);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long currentTime = System.currentTimeMillis();
			milliSecondsLeft = totalMilliSecondsToWait - (currentTime - startTime);
		}
	}

	public static void refreshPage(){
		driver.navigate().refresh();
	}

	public static boolean compareData(String dataName, String expected, String actual){

		return compareData(dataName, expected, actual, "Info", "Fail");
	}

	public static boolean compareData(String dataName, String expected, String actual, String failStatus){

		return compareData(dataName, expected, actual, "Info", failStatus);

	}

	public static boolean compareData(String dataName, String expected, String actual, String passStatus, String failStatus){

		if(expected.equalsIgnoreCase(actual)){
			//Report.updateTestStepLog(dataName, "Exp: "+ expected+" <br>Actl: " + actual, passStatus);
			return true;
		} else {
			//Report.updateTestStepLog(dataName, "Exp: "+ expected+" <br>Actl: " + actual, failStatus);
			return false;
		}

		//System.out.println(dataName+" :\n"+ "Expected: \n"+ expected+ "\nActual: \n"+ actual);

	}

	public static void verifyRegExCompliance(String dataName, String regEx, String text){

		verifyRegExCompliance(dataName, regEx, text, "Info", "Fail");

	}

	public static void verifyRegExCompliance(String dataName, String regEx, String text, String failStatus){

		verifyRegExCompliance(dataName, regEx, text, "Info", failStatus);

	}

	public static boolean verifyRegExCompliance(String dataName, String regEx, String text, String passStatus, String failStatus){

		if(text.matches(regEx)){
			//Report.updateTestStepLog(dataName, "Exp: "+ regEx+" <br>Actl: " + text + " are matching", passStatus);
			return true;
		} else {
			//Report.updateTestStepLog(dataName, "Exp: "+ regEx+" <br>Actl: " + text + " are NOT matching", failStatus);
			return false;
		}

	}

	public static boolean selectDropDownValue(Locator locator, String option){
		boolean optionchanged;
		String oldOption, newOption;
		Select select = new Select(locateElement(locator));
		oldOption = select.getFirstSelectedOption().getText();
		select.selectByVisibleText(option);
		snooze(1000);
		newOption = select.getFirstSelectedOption().getText();

		optionchanged = !newOption.equalsIgnoreCase(oldOption);
		return optionchanged;

	}


	public static void verifyListOptions(WebElement listElement, String listName, String[] expectedListOptions){
		Select list = new Select(listElement);
		List<WebElement> actualListOptions = list.getOptions();
		if(expectedListOptions.length == actualListOptions.size()){
			int count = 0;
			String expected = "", actual = "";
			for(WebElement actualListOption:actualListOptions){
				expected = expected + expectedListOptions[count];
				actual = actual + actualListOption.getText();
				count++;
			}
			compareData(listName, expected, actual);
		} else {
			String dataName, expected, actual;
			expected = "";
			actual = "";
			dataName = listName+ " Options";
			for(String expectedListOption:expectedListOptions){
				expected = expected + "," + expectedListOption;
			}

			for(WebElement actualListOption:actualListOptions){
				actual = actual + "," + actualListOption.getText();
			}
			compareData(dataName, expected.substring(1), actual.substring(1));
		}
	}


	public static void verifyListOptions(Locator locator, String listName, String[] expectedListOptions){
		verifyListOptions(locateElement(locator), listName, expectedListOptions);
	}


	public static void verifySelectedListOption(Locator locator, String listName, String expectedSelectedOption){
		Select list = new Select(locateElement(locator));
		String dataName, expected, actual;
		dataName = listName+ " Selected Option";
		expected = expectedSelectedOption;
		actual = list.getFirstSelectedOption().getText();
		compareData(dataName, expected, actual);
	}


	public static By findBy(Locator locator){
		By by = null;

		if(locator.key.equalsIgnoreCase("xpath"))
			return By.xpath(locator.value);
		else if(locator.key.equalsIgnoreCase("id"))
			return By.id(locator.value);
		else if(locator.key.equalsIgnoreCase("name"))
			return By.name(locator.value);
		else if(locator.key.equalsIgnoreCase("className"))
			return By.className(locator.value);
		else if(locator.key.equalsIgnoreCase("linkText"))
			return By.linkText(locator.value);
		else if(locator.key.equalsIgnoreCase("partialLinkText"))
			return By.partialLinkText(locator.value);
		else if(locator.key.equalsIgnoreCase("tagName"))
			return By.tagName(locator.value);
		else if(locator.key.equalsIgnoreCase("cssSelector"))
			return By.cssSelector(locator.value);
		else  
			System.out.println("Check Locator Key spelling");

		return by;
	}

	public static ArrayList<WebElement> locateElements(Locator locator, String ...targetReplacementArray){

		List<WebElement> elements = null;
		locator = constructLocator(locator, targetReplacementArray);
		elements = driver.findElements(findBy(locator));

		return (ArrayList<WebElement>) elements;

	}


	public static int getElementsCount(Locator locator, String ...targetReplacementArray){

		return locateElements(locator,targetReplacementArray).size();
	}
	
	public static int getWidth(Locator locator, String ...targetReplacementArray){

		return ((WebElement) locateElements(locator,targetReplacementArray)).getSize().width;
	}


	public static boolean isElementPresent(Locator locator, String ...targetReplacementArray){

		boolean elementPresent = true;

		locator = constructLocator(locator, targetReplacementArray);

		try {
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			driver.findElement(findBy(locator));
		} catch (Exception e) {
			elementPresent = false;
		} finally {
			driver.manage().timeouts().implicitlyWait(Config.waitTimeInSeconds, TimeUnit.SECONDS);
		}

		return elementPresent;
	}


	public static Locator constructLocator(Locator locator, String ...targetReplacementArray){

		String value = locator.value;
		int targetReplacementArrayLength = targetReplacementArray.length;

		if(targetReplacementArrayLength>0){
			if((targetReplacementArrayLength%2) == 0){
				for(int i=0; i<targetReplacementArrayLength; i=i+2){
					value = value.replace(targetReplacementArray[i], targetReplacementArray[i+1]);
				}
				locator = new Locator(locator.key, value);
			} else {
				System.out.println("The targets + replacements should be in pairs (No of targets + replacements arguments should be even in number)");
				return null;
			}
		}

		return locator;
	}

	public static WebElement locateElement(Locator locator, String ...targetReplacementArray){

		WebElement element = null;		

		locator = constructLocator(locator, targetReplacementArray);

		element = driver.findElement(findBy(locator));
		return element;

	}
	

	public static boolean isDisplayed(Locator locator, String... targetReplacementArray){
		if(isElementPresent(locator,targetReplacementArray))
			return locateElement(locator,targetReplacementArray).isDisplayed();
		else
			return false;
	}


	public static boolean isEnabled(Locator locator, String... targetReplacementArray){
		return locateElement(locator,targetReplacementArray).isEnabled();
	}

	
	public static boolean isSelected(Locator locator, String... targetReplacementArray){
		return locateElement(locator,targetReplacementArray).isSelected();
	}


	public static void clickOn(Locator locator, String... targetReplacementArray){
		locateElement(locator, targetReplacementArray).click();
	}


	public static void hoverOn(Locator locator, String... targetReplacementArray){

		WebElement element = locateElement(locator,targetReplacementArray);
		hoverOnElementWebDriver(element);

	}

	public static void hoverOnElementJavaScript(WebElement element){

		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(mouseOverScript, element);
	}

	public static void hoverOnElementWebDriver(WebElement element){

		Actions actions = new Actions(driver);
		actions.moveToElement(element).build().perform();

	}


	public static void clickOnElementJavaScript(WebElement element){

		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('click', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onclick');}";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(mouseOverScript, element);
	}


	public static void appendText(Locator locator, String text, String ...targetReplacementArray){

		WebElement element = locateElement(locator,targetReplacementArray);
		element.sendKeys(text);

	}

	public static void typeIn(Locator locator, String text, String ...targetReplacementArray){

		WebElement element = locateElement(locator,targetReplacementArray);
		element.clear();
		element.sendKeys(text);

	}

	public static void selectCheckBox(Locator locator, String ...targetReplacementArray){

		WebElement element = locateElement(locator,targetReplacementArray);
		if(!element.isSelected())
			element.click();

	}


	public static void deSelectCheckBox(Locator locator, String ...targetReplacementArray){

		WebElement element = locateElement(locator,targetReplacementArray);
		if(element.isSelected())
			element.click();

	}


	public static String getTextOf(Locator locator, String ...targetReplacementArray){

		return locateElement(locator,targetReplacementArray).getText();

	}

	public static String getAttributeValueOf(Locator locator, String attributeName, String ...targetReplacementArray){

		return locateElement(locator,targetReplacementArray).getAttribute(attributeName);
	}


	public static void displayOverlay(Locator locator, String target, String replacement){
		String key = locator.key;
		String value = locator.value.replace(target, replacement);
		displayOverlay(new Locator(key,value));
	}


	public static void displayOverlay(Locator locator){
		setAttributeValue(locator, "style", "display: none;", "display: block;");
	}

	public static void setAttributeValue(Locator locator, String attributeName, String attributeValueTarget, String attributeValueReplacement){
		String oldAttValue = getAttributeValueOf(locator, attributeName);

		if((oldAttValue!=null) && (oldAttValue.equals("")))
			setAttributeValue(locateElement(locator), attributeName, attributeValueReplacement);
		else if(oldAttValue.contains(attributeValueTarget))
			setAttributeValue(locateElement(locator), attributeName, oldAttValue.replace(attributeValueTarget, attributeValueReplacement));
		else
			setAttributeValue(locateElement(locator), attributeName, oldAttValue + " " +attributeValueReplacement);
	}


	public static void setAttributeValue(Locator locator, String attributeName, String attributeValue){
		setAttributeValue(locateElement(locator), attributeName, attributeValue);
	}
	
	public static void setAttributeValue(Locator locator, String attributeName, String attributeValue, String ...targetReplacementArray){
		setAttributeValue(locateElement(locator, targetReplacementArray), attributeName, attributeValue);
	}

	public static void setAttributeValue(WebElement element, String attributeName, String attributeValue){
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", 
				element, attributeName, attributeValue);
	}


	public static String getSelectedOption_List(Locator locator, String ...targetReplacementArray){

		Select lst = new Select(locateElement(locator,targetReplacementArray));
		return lst.getFirstSelectedOption().getText();
	}



	public static String getHiddenTextOf(Locator locator, String ...targetReplacementArray){

		return getHiddenTextOf(locateElement(locator,targetReplacementArray));

	}


	public static String getHiddenTextOf(WebElement element){

		return (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", element);

	}


	public static void scrollIntoView(Locator locator, String ...targetReplacementArray){
		scrollIntoView(locateElement(locator,targetReplacementArray));
	}
	
	public static void scrollIntoView(WebElement element){
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}
	
	
	public static void  waitForVisibleElement(Locator locator, String ...targetReplacementArray){
		int timeOutInSeconds = 30;
		waitForVisibleElement(locator, timeOutInSeconds,targetReplacementArray);
	}

	public static void  waitForVisibleElement(Locator locator, int timeOutInSeconds, String ...targetReplacementArray){
		
		if(driver instanceof InternetExplorerDriver){
			int secs = 0;
			while(secs<=timeOutInSeconds){
				snooze(1000);
				secs++;
				if(isDisplayed(locator,targetReplacementArray))
					break;
				else
					continue;
			}
		}
		else{
			Wait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until(ExpectedConditions.visibilityOf(locateElement(locator,targetReplacementArray)));
		}
		
		snooze(1000);
	}


	public static void waitForPageToLoad() {
		snooze(100);
		if(driver instanceof InternetExplorerDriver){
			int secs = 0;
			while(secs<=30){
				snooze(1000);
				secs++;
				if(((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"))
					break;
				else
					continue;
			}
		}
		else{
			ExpectedCondition<Boolean> expectation = new
					ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
				}
			};

			WebDriverWait wait = new WebDriverWait(driver,30);
			try {
				wait.until(expectation);
			} catch(Throwable error) {
				System.out.println("Exception: Timeout waiting for Page Load Request to complete.");
				error.printStackTrace();
			}
		}

		snooze(100);
	} 



	public static void rightClickOn(Locator locator, String... targetReplacementArray){

		WebElement element = locateElement(locator,targetReplacementArray);
		Actions actions = new Actions(driver);
		actions.contextClick(element).build().perform();
	}

	public static void doubleClickOn(Locator locator, String... targetReplacementArray){  

		WebElement element = locateElement(locator,targetReplacementArray);
		Actions actions = new Actions(driver);
		actions.doubleClick(element).build().perform();

	}
	
	public static String switchTo(){

		String winHandleBefore = driver.getWindowHandle();

		for(String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);			
		}

		return winHandleBefore;
	}

	//In this method we are closing all other window except parent window
	public static void switchBackTo(String winHandleBefore){

		for(String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			String currWindow =driver.getWindowHandle();

			if(!(currWindow.equals(winHandleBefore))){
				driver.close();
			}
		}
		driver.switchTo().window(winHandleBefore);
	} 


}
