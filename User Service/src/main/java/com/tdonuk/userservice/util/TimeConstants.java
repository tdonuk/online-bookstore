package com.tdonuk.userservice.util;

public class TimeConstants {
	private TimeConstants() {}
	
	public static final long ONE_MINUTE = 1 * 1000 * 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long ONE_DAY = ONE_HOUR * 24;
    public static final long ONE_MONTH = ONE_DAY * 30;
    
    /**
     * Converts the number of minutes to miliseconds. 
     * @param mins number of minutes to convert into miliseconds
     */
    public static long minutes(int mins) {
    	return mins * ONE_MINUTE;
    }
}
