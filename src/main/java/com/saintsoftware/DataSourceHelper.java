package com.saintsoftware;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataSourceHelper {
	
	private final static Logger LOGGER = LogManager.getLogger(DataSourceHelper.class.getName());
	private static final String[] provinces = {null, "AB", "BC", "MB", "NB", "NL", null, 
		"NS", "ON", "PE", "QC", "SK", "YT", "NT", "NU"};
	protected final static double MTL_LATITUDE = 45.50884;
	protected final static double MTL_LONGITUDE = -73.58781;
	
	public static void main(String[] args) {
		DataSourceHelper helper = new DataSourceHelper();
		LOGGER.debug(helper.getDisplayName("Quebec", "10", "CA"));
		LOGGER.debug(helper.getDistance(32.9697, -96.80322, MTL_LATITUDE, MTL_LONGITUDE, 'K') + " Kilometers\n");
	}

	public String getDisplayName(String name, String admin1, String country) {
		if (country.equals("US")) {
			return name + ", " + admin1 + ", USA";
		} else {
			return name + ", " + provinces[Integer.parseInt(admin1)] + ", Canada";
		}
	}

	protected double getScore(double distance) {
		if (distance==0) return 0;
		double score = 0;
		double maxCircumference = 20039; // max kilometers along equator
		score = distance/maxCircumference;
		// closer results will have lowest difference, i.e. closer to 0, but the score = a confidence
		// level where 1 not 0 is high, so we need to reverse the figure by subtracting it from 1
		return (1-score);
	}
	/*
	 * Calculate the distance between two points.
	 * Function provided by geodatasource.com
	 * */
	protected double getDistance(double lat1, double lon1, double lat2, double lon2, char unit) {
	  double theta = lon1 - lon2;
	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	  dist = Math.acos(dist);
	  dist = rad2deg(dist);
	  dist = dist * 60 * 1.1515;
	  if (unit == 'K') {
	    dist = dist * 1.609344;
	  } else if (unit == 'N') {
	  	dist = dist * 0.8684;
	    }
	  return (dist);
	}

	/*
	 * Convert decimal degrees to radians.
	 * Function provided by geodatasource.com
	 */
	private double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*
	 * Convert radians to decimal degrees.
	 * Function provided by geodatasource.com
	 */
	private double rad2deg(double rad) {
	  return (rad * 180 / Math.PI);
	}

}
