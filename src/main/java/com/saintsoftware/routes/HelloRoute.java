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
import org.json.JSONObject;

import com.saintsoftware.RouteHelper;
import com.saintsoftware.interfaces.RouteInterface;

/**
 * @author Russell Saint Cyr
 *
 * Root resource (exposed at "hello" path)
 */
@Path("hello")
public class HelloRoute extends RouteHelper implements RouteInterface {
	
	private static final Logger LOGGER = LogManager.getLogger(HelloRoute.class.getName());		
	
	public HelloRoute() {
		// LOGGER.debug("HelloRoute default constructor.");
		loadProps(); // in RouteHelper
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
    	// present string via hello.world property if it's there
    	String propHelloWorld = (configProp.getProperty("hello.world") == null) ? "Hello World!" : configProp.getProperty("hello.world");
    	String myString = new JSONObject().put("Hello", propHelloWorld).toString();
    	return Response.status(Response.Status.OK).entity(myString).build();
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
