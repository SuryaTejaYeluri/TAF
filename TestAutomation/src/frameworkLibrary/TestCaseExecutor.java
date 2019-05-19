package frameworkLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;

import com.aventstack.extentreports.ExtentTest;

import frameworkLibrary.ControlData;
import frameworkLibrary.Report;
import frameworkLibrary.WebDriverFactory;
import frameworkLibrary.WebPage;

public class TestCaseExecutor {

	static boolean stopCurrTestCaseExecution;
	public static ExtentTest extentTest;

	public static void executeTestCase(HashMap<String, String> executionParametersMap){


		try
		{
			WebDriverFactory.createWebDriverObject(Config.browserName);			
			ControlData.methodCount = new HashMap<String, Integer>();
			
			extentTest = Report.extentReports.createTest(ControlData.currTestCaseID, ControlData.currTestCaseName);
			Report.createCurrTestCaseScreenshotDirectory();
			stopCurrTestCaseExecution = false;
			
			//Initializing the screenshot count
			ControlData.testcaseScreenshotCount = 1;

			ControlData.currTestSetExcelWorkbookFileName = "DataSheets"+ File.separator +executionParametersMap.get("TestSet")+".xlsx";
			FileInputStream testSetExcelFile = new FileInputStream(new File(ControlData.currTestSetExcelWorkbookFileName));

			//Create Workbook instance holding reference to .xlsx file
			ControlData.currTestSetExcelWorkbook = new XSSFWorkbook(testSetExcelFile);

			//Get first/desired sheet from the workbook
			XSSFSheet sheet = ControlData.currTestSetExcelWorkbook.getSheet("TestFlow");

			//Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();

			Row row, testCaseRow = null;
			Cell cell;
			while (rowIterator.hasNext())
			{
				row = rowIterator.next();
				cell = row.getCell(0);
				if(ControlData.currTestCaseID.equalsIgnoreCase(cell.getStringCellValue()))
					testCaseRow = cell.getRow();
			}


			Iterator<Cell> cellIterator = testCaseRow.cellIterator();
			cell = cellIterator.next();
			while (cellIterator.hasNext()) {
				if (stopCurrTestCaseExecution) 
					break;
				cell = cellIterator.next();
				String methodName = cell.getStringCellValue();
				if (methodName != null)
					if (!methodName.equalsIgnoreCase(""))
						executeTestMethod(methodName);
					else
						break;
				else
					break;
			}
			testSetExcelFile.close();

			WebDriverFactory.destroyWebDriverObject();
			
			//Re-initializing the screenshot count
			ControlData.testcaseScreenshotCount = 1;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 

	}
	
	public static void executeTestMethod(String methodName){
		String exceptionMessage;

		try {			

			ControlData.currTestMethod = methodName;

			//Report function for updating method name.
			Report.updateReport("<b><i><font style=\"color: blue\">"+ methodName + "</font></i></b>", "Info");

			if(ControlData.methodCount.containsKey(methodName)){
				int count = ControlData.methodCount.get(methodName);
				count++;
				ControlData.methodCount.put(methodName, count);
			}
			else
				ControlData.methodCount.put(methodName, 1);

			List<Class> businessLogicClasses = getClasses(WebPage.class.getClassLoader(),"testLogic");

			for(Class businessLogicClass:businessLogicClasses){

				Object businessclassObj = businessLogicClass.newInstance();

				try {
					Method method  = businessLogicClass.getDeclaredMethod(methodName);

					method.invoke(businessclassObj);
					break;

				} catch (NoSuchMethodException e) {}
			}			


		} catch (NoSuchElementException  e) {			
			exceptionMessage = e.getMessage();
			//Report function for updating exception 
		} catch (StaleElementReferenceException   e) {
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		} catch (TimeoutException  e) {
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		} catch (ElementNotVisibleException  e) {			
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		} catch (NoSuchWindowException  e) {			
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		}catch (IllegalAccessException e) {
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		} catch (IllegalArgumentException e) {
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		} catch (InvocationTargetException e) {
			exceptionMessage = e.getCause().getMessage();
			//Report function for updating exception
			stopCurrTestCaseExecution = true;
		} catch (SecurityException e) {
			exceptionMessage = e.getCause().getMessage();
			//Report function for updating exception
		} catch (InstantiationException e) {
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		} catch (ClassNotFoundException e) {
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
			//Report function for updating exception
		} 
	}
	
	
	public static List<Class> getClasses(ClassLoader classLoader,String myPackage) throws Exception{

		List<Class> classes = new ArrayList<Class>();
		URL packageURL = classLoader.getResource(myPackage);

		BufferedReader bufferedReader    = new BufferedReader(new InputStreamReader(packageURL.openStream()));

		String fileName = null;
		while ((fileName = bufferedReader.readLine()) != null) {
			if(fileName.endsWith(".class"))
				classes.add(Class.forName(myPackage+"."+fileName.substring(0,fileName.lastIndexOf('.'))));
		}

		return classes;
	}
}
