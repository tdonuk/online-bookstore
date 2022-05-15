package com.tdonuk.scraper.util;

public class NumericUtils {
	private NumericUtils() {}
	
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}
