package frameworkLibrary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {

	public static String getTimeStamp(String formatString){
		
		DateFormat dateFormat = new SimpleDateFormat(formatString);
		String timeStamp = dateFormat.format(new Date());
		
		return timeStamp;
	}
	
}
