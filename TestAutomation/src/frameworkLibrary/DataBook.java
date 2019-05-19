package frameworkLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataBook {

	public static String defaultSheetName = Config.testData_DefaultSheetName;

	public static Row findTestDataRow(XSSFSheet sheet, boolean isIteratedData){

		//Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = null, testCaseStartRow = null, testCaseEndRow = null, testCaseCurrRow = null;
		Cell cell;
		while (rowIterator.hasNext())
		{
			row = rowIterator.next();
			cell = row.getCell(0);
			if(ControlData.currTestCaseID.equalsIgnoreCase(cell.getStringCellValue())){
				testCaseStartRow = cell.getRow();
				if(rowIterator.hasNext()){
					String cellContents = "";
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						cell = row.getCell(0);
						if(cell!=null){
							cellContents = cell.getStringCellValue();
							if(!ControlData.currTestCaseID.equalsIgnoreCase(cellContents)){
								testCaseEndRow = sheet.getRow(row.getRowNum()-1);
								break;
							} else 
								testCaseEndRow = sheet.getRow(row.getRowNum());
						} else 
							testCaseEndRow = sheet.getRow(row.getRowNum());

					}
				}
				else
					testCaseEndRow = testCaseStartRow;

				break;
			}

		}

		if(isIteratedData){
			for(int i = testCaseStartRow.getRowNum(); i<=testCaseEndRow.getRowNum(); i++){
				cell = sheet.getRow(i).getCell(1);
				if((ControlData.methodCount.get(ControlData.currTestMethod)+"").equalsIgnoreCase(cell.getStringCellValue())){
					testCaseCurrRow = cell.getRow();
					break;
				}
			}
		} else {
			testCaseCurrRow = testCaseStartRow;
		}


		return testCaseCurrRow;
	}

	public static Cell findTestDataCell(XSSFWorkbook workbook, String sheetName, String colName, boolean isIteratedData){

		//Get first/desired sheet from the workbook
		XSSFSheet sheet = workbook.getSheet(sheetName);

		Row testCaseCurrRow = findTestDataRow(sheet, isIteratedData);

		Cell cell, testDataCell=null;

		Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();

		while (cellIterator.hasNext())
		{
			cell = cellIterator.next();
			if(colName.equalsIgnoreCase(cell.getStringCellValue()))
				testDataCell = testCaseCurrRow.getCell(cell.getColumnIndex());

		}


		return testDataCell;
	}

	public static String getData(String colName){
		return getData(defaultSheetName, colName);
	}

	public static String getData(String colName, boolean isIteratedData){
		return getData(defaultSheetName, colName, isIteratedData);
	}


	public static String getData(String sheetName, String colName){
		return getData(sheetName, colName, true);
	}


	public static String getData(String sheetName, String colName, boolean isIteratedData){

		String data = null;

		Cell testDataCell = null;

		FileInputStream testSetExcelFile = null;
		try {
			testSetExcelFile = new FileInputStream(new File(ControlData.currTestSetExcelWorkbookFileName));

			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(testSetExcelFile);

			testDataCell = findTestDataCell(workbook, sheetName, colName, isIteratedData);
			data = testDataCell.getStringCellValue();


//			if(data.length()>0)
//				if(data.charAt(0)=='@')
//					data = SharedDataBook.getData(data.substring(1), colName);

			workbook.close();
			testSetExcelFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;

	}


	public static void putData(String colName, String data){
		putData(defaultSheetName, colName, data);
	}

	public static void putData(String colName, String data, boolean isIteratedData){
		putData(defaultSheetName, colName, data, isIteratedData);
	}

	public static void putData(String sheetName, String colName, String data){
		putData(sheetName, colName, data, true);
	}


	public static void putData(String sheetName, String colName, String data, boolean isIteratedData){
		Cell testDataCell = null;

		FileInputStream testSetExcelFile = null;
		try {
			testSetExcelFile = new FileInputStream(new File(ControlData.currTestSetExcelWorkbookFileName));

			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(testSetExcelFile);

			testDataCell = findTestDataCell(workbook, sheetName, colName, isIteratedData);
			String cellData = testDataCell.getStringCellValue();

//			if(cellData.length()>0)
//				if(cellData.charAt(0)=='@')
//					SharedDataBook.putData(cellData.substring(1), colName, data);
//				else
//					testDataCell.setCellValue(data);
//			else
				testDataCell.setCellValue(data);

			testSetExcelFile.close();

			FileOutputStream outFile =new FileOutputStream(new File(ControlData.currTestSetExcelWorkbookFileName));
			workbook.write(outFile);
			outFile.close();
			workbook.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static HashMap<String, String> getDataMap(String sheetName){
		HashMap<String, String> dataMap = new HashMap<String, String>();

		String data = null;

		boolean isIteratedData = true;

		FileInputStream testSetExcelFile = null;
		try {
			testSetExcelFile = new FileInputStream(new File(ControlData.currTestSetExcelWorkbookFileName));

			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(testSetExcelFile);

			//Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			Row testCaseCurrRow = findTestDataRow(sheet, isIteratedData);

			Cell cell, testDataCell=null;

			Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();

			while (cellIterator.hasNext())
			{
				cell = cellIterator.next();
				String key = cell.getStringCellValue();
				testDataCell = testCaseCurrRow.getCell(cell.getColumnIndex());
				data = testDataCell.getStringCellValue();
//				if(data.length()>0)
//					if(data.charAt(0)=='@')
//						data = SharedDataBook.getData(data.substring(1), key);

				dataMap.put(key, data);
			}


			workbook.close();
			testSetExcelFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataMap;

	}




}


