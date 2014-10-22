package com.saintsoftware;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Russell Saint Cyr
 *
 */
public class RouteHelper {

	protected Properties configProp = new Properties();
	protected static String dataSourceImpl = null;
    private final static Logger LOGGER = LogManager.getLogger(RouteHelper.class.getName());

	private static final String PROP_DATA_SOURCE_IMPL = "dataSourceImpl";
	
	public RouteHelper() {
		// LOGGER.debug("RouteHelper default constructor.");
	}
	
    public void loadProps() {
    	String propFile = "config.properties";
    	InputStream in = this.getClass().getClassLoader().getResourceAsStream(propFile);
        try {
            configProp.load(in);
            LOGGER.debug("Properties loaded from " + propFile + ".");
    		dataSourceImpl = configProp.getProperty(PROP_DATA_SOURCE_IMPL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
