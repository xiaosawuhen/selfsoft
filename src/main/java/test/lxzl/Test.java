package test.lxzl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.chrono.JapaneseDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Test {

	public static void main(String[] args) {

		SimpleDateFormat sdf_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf_time.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		
		Calendar cal = Calendar.getInstance();
		String systemTime = sdf_time.format(cal.getTime());
		System.out.println(systemTime);
		
		JapaneseDate japaneseDate = JapaneseDate.now();
//		String systemTime2 = sdf_time.format(japaneseDate);
		
//		japaneseDate.
		
		System.out.println(japaneseDate);
		

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        //calendar.getTime() 返回的是Date类型，也可以使用calendar.getTimeInMillis()获取时间戳
        System.out.println("北京时间: " + sdf_time.format(calendar.getTime()));
		

		
//		Calendar.getInstance(TimeZone.getTimeZone(TimeZone))

	    Calendar cCalendar = Calendar.getInstance();
	    System.out.println(cCalendar.getTime());
	    System.out.println(cCalendar.getDisplayName(Calendar.ERA, Calendar.LONG, Locale.getDefault()) + ": " + cCalendar.get(Calendar.YEAR));
	     
	    Locale jLocale = new Locale("ja", "jp", "JP");
	    Calendar jCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), jLocale);
		String systemTime1 = sdf_time.format(jCalendar.getTime());
		System.out.println(systemTime1);
	    System.out.println(jCalendar.getTime());
//	    System.out.println(jCalendar.getDisplayName(Calendar.ERA, Calendar.LONG, jLocale) + ": " + jCalendar.get(Calendar.YEAR));
		
	    
		String message = MessageFormat.format("123","qew");
		System.out.println(message);
	}
}
