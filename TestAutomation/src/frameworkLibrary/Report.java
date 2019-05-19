package frameworkLibrary;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import frameworkLibrary.ControlData;

public class Report {

	public static ExtentReports extentReports;
	public static ExtentHtmlReporter extentHtmlReporter;
	
	public static void generateReportTemplate(){
		
		String userDirectory = System.getProperty("user.dir");
		ControlData.reportPath = userDirectory + File.separator + "Reports";
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
		LocalDateTime localDateTime = LocalDateTime.now();
		
		String dateTimeStamp = dateTimeFormatter.format(localDateTime);
		ControlData.reportFileName = Config.projectName + "_" + dateTimeStamp + ".html";
		
		ControlData.reportFileNameWithPath = ControlData.reportPath + File.separator + ControlData.reportFileName;
		
		extentHtmlReporter = new ExtentHtmlReporter(ControlData.reportFileNameWithPath);
		extentReports = new ExtentReports();
		extentReports.attachReporter(extentHtmlReporter);
		
		extentHtmlReporter.config().setChartVisibilityOnOpen(false);
		extentHtmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		extentHtmlReporter.config().setDocumentTitle(Config.projectName + "ExtentReports");
		extentHtmlReporter.config().setReportName(Config.projectName);
		extentHtmlReporter.config().setTheme(Theme.STANDARD);
	}
	
	
	public static void updateReport(String stepDescription, String testStepResultStatus){
		
		if(testStepResultStatus.equalsIgnoreCase("Pass")){
			TestCaseExecutor.extentTest.pass(stepDescription);
			if(Config.passedStepsScreenshot){
				
				WebPage.captureScreenshotAndSave();				
				try {
					TestCaseExecutor.extentTest.addScreenCaptureFromPath(ControlData.currScreenshotName);	
				} catch (IOException e) {}
			}
		}else if(testStepResultStatus.equalsIgnoreCase("Fail")){
			TestCaseExecutor.extentTest.fail(stepDescription);
			if(Config.failedStepsScreenshot){
				WebPage.captureScreenshotAndSave();				
				try {
					TestCaseExecutor.extentTest.addScreenCaptureFromPath(ControlData.currScreenshotName);
				} catch (IOException e) {}
			}
		}else if(testStepResultStatus.equalsIgnoreCase("Warning")){
			TestCaseExecutor.extentTest.warning(stepDescription);		
			try {
				WebPage.captureScreenshotAndSave();	
				TestCaseExecutor.extentTest.addScreenCaptureFromPath(ControlData.currScreenshotName);
			} catch (IOException e) {}
		}else if(testStepResultStatus.equalsIgnoreCase("Info"))
			TestCaseExecutor.extentTest.info(stepDescription);
	}
	
	public static void createDirectory(String directoryPath){

		File directory = new File(directoryPath);
		if(!directory.exists())
			directory.mkdirs();

	}
	
	public static void createCurrTestCaseScreenshotDirectory(){
		ControlData.currTestcaseScreenshotDirectory = ControlData.screenshotDirectory + File.separator + ControlData.currTestCaseID;
		createDirectory(ControlData.currTestcaseScreenshotDirectory);
	}
	
	public static void createScreenshotDirectory(){
		ControlData.screenshotDirectory = ControlData.reportFileNameWithPath.replace(".html", "");
		createDirectory(ControlData.screenshotDirectory);
	}
	
	public static void openTestSummaryReport(){

		if(Desktop.isDesktopSupported()){
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.open(new File(ControlData.reportFileNameWithPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
