/**
 * 
 */
package com.saintsoftware.dataimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import com.saintsoftware.interfaces.DataSourceInterface;

/**
 * @author Russell Saint Cyr
 *
 */
public class PostgresDataSource implements DataSourceInterface {

	private final static Logger LOGGER = LogManager.getLogger(PostgresDataSource.class.getName());
	
	/**
	 * 
	 */
	public PostgresDataSource() {
		// LOGGER.debug("PostgresDataSource default constructor.");
		init();
	}

	/* (non-Javadoc)
	 * @see com.saintsoftware.interfaces.DataSourceInterface#init()
	 */
	@Override
	public void init() {
		LOGGER.debug("init() not implemented.");
	}

	/* (non-Javadoc)
	 * @see com.saintsoftware.interfaces.DataSourceInterface#getSuggestions(java.lang.String)
	 */
	@Override
	public JSONArray getSuggestions(String str, double latitude, double longitude) {
		LOGGER.debug("getSuggestions() not implemented.");
		return null;
	}

	/* (non-Javadoc)
	 * @see com.saintsoftware.interfaces.DataSourceInterface#close()
	 */
	@Override
	public void close() {
		LOGGER.debug("close() not implemented.");
	}

	/* (non-Javadoc)
	 * @see com.saintsoftware.interfaces.DataSourceInterface#test(java.lang.String)
	 */
	@Override
	public JSONArray test(String searchStr) {
		LOGGER.debug("test() not implemented.");
		return null;
	}

}
