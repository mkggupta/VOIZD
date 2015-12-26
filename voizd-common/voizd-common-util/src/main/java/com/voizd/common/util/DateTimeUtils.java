package com.voizd.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.enumeration.DateTimeFormatEnum;

public class DateTimeUtils {
	 private static Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);
	
	public static Date getDateObject(String strDate, DateTimeFormatEnum formatEnum) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatEnum.getFormat());
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			logger.error("Exception in parsing date " + strDate + " format " + formatEnum);
			return null;
		}
	}
	public static Long getTimeInSecond(String strTime) {
		String[] strArray = strTime.split(":");
		Long timeInSec= 0l;
		if(strArray.length==3){
			String st = strArray[0];
			String st1 = strArray[1];
			String st2 = strArray[2];
			timeInSec =Long.parseLong(st)*60*60+Long.parseLong(st1)*60+Long.parseLong(st2);
		}
		return timeInSec;
	}
	public static int getAge(Date date){
		int age = 0;
		Calendar birthCal = Calendar.getInstance();
		birthCal.setTime(date);
		Calendar curCal = Calendar.getInstance();
		age = curCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
		logger.debug("getAge() : age : "+age);
		return age;
	}
	public static String getGmtTime() {
		Calendar cal = Calendar.getInstance();
		int gmtoffset = cal.get(Calendar.DST_OFFSET) + cal.get(Calendar.ZONE_OFFSET);
		Date GMTDate = new Date(System.currentTimeMillis() - gmtoffset);
		SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
	    formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
		String gmtTime = formatter.format(GMTDate);
		return gmtTime;

	}
	

	  public static Date getGMTDate() throws ParseException {
			Calendar cal = Calendar.getInstance();
			int gmtoffset = cal.get(Calendar.DST_OFFSET) + cal.get(Calendar.ZONE_OFFSET);
			Date GMTDate = new Date(System.currentTimeMillis() - gmtoffset);
			SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
		    formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
			String gmtTime = formatter.format(GMTDate);
			logger.debug("getGMTDate() : gmtTime : "+gmtTime+" , ");
			Date date = convertToDate(gmtTime);
			logger.debug("getGMTDate() : date : "+date+" , ");
	        return date;
	    }
	
	  public static Date convertToDate(String dateStr) throws ParseException {
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        return simpleDateFormat.parse(dateStr);
	    }
	    
	    /**
	     * Returns a string the represents the passed-in date parsed
	     * according to the passed-in format.  Returns an empty string
	     * if the date or the format is null.
	     **/
	    public static String format(Date aDate, SimpleDateFormat aFormat) {
	        if (aDate == null || aFormat == null ) { return ""; }
	        synchronized (aFormat) {
	            return aFormat.format(aDate);
	        }
	    }
	    
	    
}
