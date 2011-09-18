package com.mot.multicore.expenses;

import java.util.Locale;

public enum Month {
	January(1), February(2), March(3), April(4), May(5), June(6), July(7), August(
			8), September(9), October(10), November(11), December(12);

	@SuppressWarnings("unused")
	private int value;

	Month(int i) {
		value = i;
	}

	public static String getMonth(int i, Locale loc) {
		boolean isEng = false;

		String m = null;
		switch (i) {
		case 1:
			m = (isEng ? "Jan" : "Sty");
			break;

		case 2:
			m = (isEng ? "Feb" : "Lut");
			break;
		case 3:
			m = (isEng ? "Mar" : "Mar");
			break;

		case 4:
			m = (isEng ? "Apr" : "Kwi");
			break;
		case 5:
			m = (isEng ? "May" : "Maj");
			break;
		case 6:
			m = (isEng ? "Jun" : "Cze");
			break;
		case 7:
			m = (isEng ? "Jul" : "Lip");
			break;
		case 8:
			m = (isEng ? "Aug" : "Sie");
			break;
		case 9:
			m = (isEng ? "Sep" : "Wrz");
			break;
		case 10:
			m = (isEng ? "Oct" : "Paz");
			break;
		case 11:
			m = (isEng ? "Nov" : "Lis");
			break;
		case 12:
			m = (isEng ? "Dec" : "Gru");
			break;
		default:
			m = (isEng ? "Jan" : "Sty");
			break;
		}

		return m;
	}

}
