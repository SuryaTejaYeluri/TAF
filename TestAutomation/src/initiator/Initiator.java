package initiator;


import frameworkLibrary.BatchFileConfig;
import frameworkLibrary.Config;
import frameworkLibrary.ExecutionController;
import frameworkLibrary.Report;
import frameworkLibrary.StaticDataManager;

public class Initiator {

	public static void main(String[] args) {

		Config.readConfigFile();

		if(args.length > 0)
			for (String param : args)
				BatchFileConfig.setConfigValue(param);	

		ExecutionController.readExecutionControllerExcel();
		Report.generateReportTemplate();
		StaticDataManager.readConfigFile();
		ExecutionController.doTestExecution();
		Report.openTestSummaryReport();
	}

}
