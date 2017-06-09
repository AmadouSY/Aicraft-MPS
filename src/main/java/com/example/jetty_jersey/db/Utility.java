package com.example.jetty_jersey.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utility
{
	private static Logger log = LogManager.getLogger(Utility.class.getName());
	private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	/**
	 * This method convert dd/MM/yyyy to date object
	 * 
	 * @param strDateInDb
	 * @return date
	 */
	public static Date convertDateString(String strDateInDb)
	{
		Date date = new Date();
		try
		{
			synchronized (df)
			{
				date = df.parse(strDateInDb);
			}
		} catch (ParseException e)
		{
			log.error("Unrecognized date format (not yyyy/MM/dd HH:mm):" + strDateInDb);
		}
		return date;
	}

	/**
	 * Reverse method of convertDateString with the same formatter
	 * 
	 * @param date
	 * @return string with format yyyy/MM/dd HH:mm
	 */
	public static String convertDateToString(Date date)
	{
		String s = "";
		synchronized (df)
		{
			s = df.format(date);
		}
		return s;
	}

	/**
	 * This method convert string to boolean
	 * If string is TRUE/true/True, it return boolean true. Otherwise, it return false
	 * 
	 * @param strBoolInDb
	 * @return boolean
	 */
	public static boolean convertBoolString(String strBoolInDb)
	{
		if ("true".equals(strBoolInDb.trim().toLowerCase()))
			return true;
		else
			return false;
	}

	/**
	 * This method convert string to int
	 * Note that if string contains none-digit char, it will return 0 with an error in the log
	 * 
	 * @param strIntInDb
	 * @return integer
	 */
	public static int convertIntString(String strIntInDb)
	{
		if (strIntInDb.length() == 0)
			return -1;
		int i = 0;
		try
		{
			i = Integer.parseInt(strIntInDb);
		} catch (NumberFormatException e)
		{
			log.error("Incorrect integer format :" + strIntInDb);
		}
		return i;
	}

	/**
	 * In case we need to display float value nicely...
	 * 
	 * @param str
	 * @return float
	 */
	public static float convertFloatString(String str)
	{
		return Float.parseFloat(str);
	}
}
