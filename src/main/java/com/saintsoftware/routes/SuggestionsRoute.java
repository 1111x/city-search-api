package com.saintsoftware.routes;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.saintsoftware.RouteHelper;
import com.saintsoftware.interfaces.DataSourceInterface;
import com.saintsoftware.interfaces.RouteInterface;

/**
 * @author Russell Saint Cyr
 *
 * Root resource (exposed at "suggestions" path)
 */
@Path("suggestions")
public class SuggestionsRoute extends RouteHelper implements RouteInterface {

	private static final Logger LOGGER = LogManager.getLogger(SuggestionsRoute.class.getName());	
	
	public SuggestionsRoute() {
		// LOGGER.debug("SuggestionsRoute default constructor.");
		loadProps(); // in RouteHelper
	}
	
    public static void main(String[] args) {
		try {
			// call constructors and load props
			@SuppressWarnings("unused")
			SuggestionsRoute sugg = new SuggestionsRoute();
			// try to instantiate the class
			Class<?> clazz = Class.forName(dataSourceImpl);
			DataSourceInterface datasource = (DataSourceInterface) clazz.newInstance();
			LOGGER.debug("dataSourceImpl=" + dataSourceImpl);
			// default to que for searches
			String cityName = (args.length == 0) ? "que" : args[0];
			LOGGER.debug("cityName=" + cityName);
			// call getSuggestions() method
			JSONArray arr = datasource.getSuggestions(cityName, 0, 0);
			LOGGER.debug(arr);
			// call close() method if implemented
			datasource.close();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return String that will be returned as a application/json response.
     */
    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleGet(@Context UriInfo info) {
    	String cityName = info.getQueryParameters().getFirst("q");
    	// get coordinates, defaulting to invalid coordinates 
    	double latitude = (info.getQueryParameters().getFirst("latitude") == null) ? 91 :
    		Double.parseDouble(info.getQueryParameters().getFirst("latitude"));
    	if (latitude != 0) LOGGER.debug("latitude={}", latitude);
    	double longitude = (info.getQueryParameters().getFirst("longitude") == null) ? 181 :
    		Double.parseDouble(info.getQueryParameters().getFirst("longitude"));
    	if (longitude != 0) LOGGER.debug("longitude={}", longitude);
    	// create default JSON response
    	String jsonError = "{\"suggestions\":[]}";
    	// ensure we have parts of the city name
    	if (cityName == null || cityName.trim().equals("")) {
    		LOGGER.debug("No q argument passed to web service.");
    		return Response.status(Response.Status.NOT_FOUND).entity(jsonError).build();
    	}
		try {
			// call constructors and load props
			@SuppressWarnings("unused")
			SuggestionsRoute sugg = new SuggestionsRoute();
			// try to instantiate the class
			Class<?> clazz = Class.forName(dataSourceImpl);
			DataSourceInterface datasource = (DataSourceInterface) clazz.newInstance();
			LOGGER.debug("dataSourceImpl=" + dataSourceImpl);
			// call getSuggestions() method
			JSONArray arr = datasource.getSuggestions(cityName, latitude, longitude);
			// call close() method if implemented
			datasource.close();
			// report if empty
			if (arr == null || arr.length() == 0) {
				LOGGER.debug("0 results returned from search.");
				return Response.status(Response.Status.NOT_FOUND).entity(jsonError).build();
			} else {
				LOGGER.debug("{} results returned from search.", arr.length());
				// add "suggestions" key
				JSONObject jsonFinal = new JSONObject().put("suggestions", arr);
				// return the final JSON object
				return Response.status(Response.Status.OK).entity(jsonFinal.toString()).build();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(jsonError).build();
		}        
    }

    @POST
    @Override
	public String handlePost() {
		LOGGER.debug("handlePost() not implemented.");
		return null;
	}

	@PUT
    @Override
	public String handlePut() {
		LOGGER.debug("handlePut() not implemented.");
		return null;
	}

	@DELETE
	@Override
	public String handleDelete() {
		LOGGER.debug("handleDelete() not implemented.");
		return null;
	}

}
