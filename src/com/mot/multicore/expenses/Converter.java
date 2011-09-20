package com.mot.multicore.expenses;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;


public class Converter {

	private static final String TAG = Converter.class.getName();

	/**
	 * Converter from string to double
	 * 
	 * @param toConv
	 *            - string to convert
	 * @return converted value
	 */
	public static Double toDouble(String toConv) {
		Double value = 0.0;
		if (null == toConv || toConv.length() == 0)
			return value;
		
		try {
			String replacedDots = toConv.replace(".", "");
			String replacedCommas = replacedDots.replace(",", ".");

			value = Double.valueOf(replacedCommas);

		} catch (Exception e) {
			return 0.0;
		}
		return value;
	}

	public static Date toDate(String date) {
		Date d = new Date();

		String[] split = date.split("-");
		if (split.length > 2) {
			Integer year = Integer.parseInt(split[2]) - 1900;
			Integer month = Integer.parseInt(split[1]) - 1;
			Integer day = Integer.parseInt(split[0]);
			d = new Date(year, month, day);
		}

		return d;
	}
	
	public static String toMonth(int i)
	{
		Locale l = Locale.getDefault();
		return Month.getMonth(i, l);
	}

	public static Calendar toCalendar(String dataKsiegowania) {
		Log.v(TAG, dataKsiegowania);
		Calendar cal = Calendar.getInstance();
		String[] date = dataKsiegowania.split("-");
		cal.set(Integer.valueOf(date[2]), Integer.valueOf(date[1]),
				Integer.valueOf(date[0]));
		return cal;
	}

}
