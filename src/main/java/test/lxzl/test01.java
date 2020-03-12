package test.lxzl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class test01 {
	
	public static void main(String[] args) throws ParseException {
		

		String holidayActivityTimeFrom = "2020-01-24 09:00:00";
		String holidayActivityTimeTo = "2020-02-24 20:00:00";

		String holidayTimeFrom = "";
		String holidayTimeTo = "";
		SimpleDateFormat sdf_old_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		sdf_old_time.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		
		SimpleDateFormat sdf_new_time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		holidayTimeFrom = sdf_new_time.format(sdf_old_time.parse(holidayActivityTimeFrom));
		holidayTimeTo = sdf_new_time.format(sdf_old_time.parse(holidayActivityTimeTo));
		
		System.out.println(holidayTimeFrom);
		System.out.println(holidayTimeTo);
	}
}
