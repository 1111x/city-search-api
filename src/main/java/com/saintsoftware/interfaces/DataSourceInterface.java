/**
 * 
 */
package com.saintsoftware.interfaces;

import org.json.JSONArray;

/**
 * @author Russell Saint Cyr
 *
 */
public interface DataSourceInterface {

	void init();
	JSONArray getSuggestions(String str, double latitude, double longitude);
	void close();
	JSONArray test(String searchStr);
	   
}
