package frameworkLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import frameworkLibrary.Config;
import frameworkLibrary.ControlData;
import frameworkLibrary.TestCaseExecutor;
import frameworkLibrary.Report;

public class ExecutionController {

	public static List<HashMap<String, String>> testCaseExecutionList = new ArrayList<HashMap<String, String>>();

	public static void readExecutionControllerExcel(){

		String testSuiteNames[] = Config.testSuiteNames.split(",");

		for(String testSuiteName:testSuiteNames){
			try
			{
				FileInputStream executionControllerExcelFile = new FileInputStream(new File("ExecutionController.xlsx"));

				//Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(executionControllerExcelFile);

				//Get desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheet(testSuiteName);

				//Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				Row row = rowIterator.next();
				int noOfexecutionParameters = row.getLastCellNum() - row.getFirstCellNum()/* + 1*/;

				String executionParameters[] = new String[noOfexecutionParameters];
				Iterator<Cell> cellIterator = row.cellIterator();

				int currColNo = 0;
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();
					executionParameters[currColNo] = cell.getStringCellValue();
					currColNo++;
				}

				while (rowIterator.hasNext())
				{
					Map<String, String> executionParametersMap = new HashMap<String, String>();
					row = rowIterator.next();
					//For each row, iterate through all the columns
					cellIterator = row.cellIterator();

					currColNo = 0;
					while (cellIterator.hasNext())
					{
						Cell cell = cellIterator.next();
						executionParametersMap.put(executionParameters[currColNo], cell.getStringCellValue());
						currColNo++;
					}


					if(executionParametersMap.get("Run").equalsIgnoreCase("Yes"))
						testCaseExecutionList.add((HashMap<String, String>) executionParametersMap);
				}

				workbook.close();
				executionControllerExcelFile.close();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}		

		ControlData.noOfTestCasesSetToRun = testCaseExecutionList.size();
		System.out.println("\nNo Of TestCases set to run:"+ ControlData.noOfTestCasesSetToRun);

	}

	public static void doTestExecution(){
		Report.createScreenshotDirectory();
		
		for(HashMap<String, String> executionParametersMap: testCaseExecutionList){
			
			ControlData.currTestCaseID = executionParametersMap.get("TC_ID");
			ControlData.currTestCaseName = executionParametersMap.get("TestCase_Name");
			
			TestCaseExecutor.executeTestCase(executionParametersMap);
		}
		
		Report.extentReports.flush();
	}
}
