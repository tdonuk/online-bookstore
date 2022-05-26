package com.tdonuk.scraper.util;

public class JsonValidator {
	private JsonValidator() {}
	
	public static String validate(String jsonData) {
		long opening = jsonData.chars().filter(ch -> ch == '{').count();
		long closing = jsonData.chars().filter(ch -> ch == '}').count();
		
		if(opening != closing) {
			int i = jsonData.lastIndexOf("}"); // unnecessary block closing
			jsonData = jsonData.substring(0, i);
		}
		
		return jsonData;
	}
}
