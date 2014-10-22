/**
 * 
 */
package com.saintsoftware.dataimpl;

import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.saintsoftware.DataSourceHelper;
import com.saintsoftware.interfaces.DataSourceInterface;

/**
 * @author Russell Saint Cyr
 *
 */
public class MongoDataSource extends DataSourceHelper implements DataSourceInterface {

	private final static Logger LOGGER = LogManager.getLogger(MongoDataSource.class.getName());
	
	public MongoDataSource() {
		// LOGGER.debug("MongoDataSource default constructor.");
		init();
	}

	/* (non-Javadoc)
	 * @see com.saintsoftware.data.DataSources#init()
	 */
	@Override
	public void init() {
		LOGGER.debug("init() not implemented.");	
	}

	/* (non-Javadoc)
	 * @see com.saintsoftware.data.DataSources#test()
	 */
	@Override
	public JSONArray test(String searchStr) {
		return getSuggestions(searchStr, 0, 88);
	}

	/* (non-Javadoc)
	 * @see com.saintsoftware.data.DataSources#getSuggestions(java.lang.String)
	 */
	@Override
	public JSONArray getSuggestions(String searchStr, double latitude, double longitude) {
		LOGGER.debug("Searching for {}", searchStr);
		// declare JSONArray
		JSONArray arrSuggestions = new JSONArray();
		// return empty array if search string is null or empty
		if (searchStr == null || searchStr.trim().equals("")) return arrSuggestions;
		// create data hooks for logging
		final long startTime = System.currentTimeMillis();
		// connect to Mongo
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return arrSuggestions; // null
		}
		// get database. will be created if it isn't there.
		DB db = mongoClient.getDB( "busbud" );
		LOGGER.debug("Connection time: {} ms.", (System.currentTimeMillis() - startTime));
		// get collection : cities
		DBCollection coll = db.getCollection("cities");
		// compile regex for search based on arguments
		Pattern regex = Pattern.compile("^" + searchStr, Pattern.CASE_INSENSITIVE);
		// search against ascii and name fields
		DBObject clause1 = new BasicDBObject("name", regex);  
		DBObject clause2 = new BasicDBObject("ascii", regex);    
		BasicDBList or = new BasicDBList();
		or.add(clause1);
		or.add(clause2);
		DBObject query = new BasicDBObject("$or", or);
		// just return the fields we need
		BasicDBObject fields = new BasicDBObject("_id", 0);
		fields.append("name", 1);
		fields.append("lat", 1);
		fields.append("long", 1);
		fields.append("ascii", 1);
		fields.append("country", 1);
		fields.append("admin1", 1);
		// get cursor
		DBCursor cursor = coll.find(query, fields);
		LOGGER.debug("Find time: {} ms.", (System.currentTimeMillis() - startTime));	
		try {
			// begin loop
			while(cursor.hasNext()) {
				BasicDBObject obj = (BasicDBObject) cursor.next();
				// LOGGER.debug("BasicDBObject={}", obj);
				// convert to JSON object
				JSONObject jsonObj = new JSONObject(obj);
				// change to full name
				String fullName = getDisplayName(obj.getString("name"), obj.getString("admin1"), obj.getString("country"));
				// compute distance if we have valid coordinates
				double distance = 0;
				if (latitude <= 90 && latitude >= -90 && longitude <= 180 && longitude >= -180) {
					distance = getDistance(obj.getDouble("lat"), obj.getDouble("long"), 
						latitude, longitude, 'K');
				}
				jsonObj.put("distance", distance);
				// get score by using distance
				jsonObj.put("score", getScore(distance));
				// add modified lat & long
				jsonObj.put("latitude", obj.getDouble("lat"));
				jsonObj.put("longitude", obj.getDouble("long"));
				// remove un-needed fields once we've used them
				jsonObj.remove("lat");
				jsonObj.remove("long");
				jsonObj.remove("admin1");
				jsonObj.remove("country");
				jsonObj.remove("name");
				jsonObj.remove("ascii");
				// Finally, add back modified name
				jsonObj.put("name", fullName);
				// add to overall array
				arrSuggestions.put(jsonObj);
				// LOGGER.debug("modified JSONObject={}", jsonObj);
			} // end loop
			LOGGER.debug("Loop time: {} ms.", (System.currentTimeMillis() - startTime));
		} finally {
		   cursor.close();
		}
		LOGGER.debug("Total execution time: {} ms.", (System.currentTimeMillis() - startTime));
		// TODO: sort the result by score
		// return result
		return arrSuggestions;
	}

	/* (non-Javadoc)
	 * @see com.saintsoftware.data.DataSources#close()
	 */
	@Override
	public void close() {
		LOGGER.debug("close() not implemented.");	
	}

	public static void main(String[] args) {
		MongoDataSource dataSource = new MongoDataSource();
		JSONArray arr = dataSource.test("Qué");
		if (arr.length() == 0) {
			LOGGER.debug("JSONArray was empty");
		} else {
			LOGGER.debug("JSONArray had {} results: {}", arr.length(), arr);
		}
	}

}
