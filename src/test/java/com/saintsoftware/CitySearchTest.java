package com.saintsoftware;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Russell Saint Cyr
 *
 */
public class CitySearchTest extends DataSourceHelper {

    private HttpServer server;
    private WebTarget target;
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(CitySearchTest.class.getName());	

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();
        // set the target with the base URI
        target = c.target(Main.BASE_URI);
    }

    @SuppressWarnings("deprecation")
	@After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test that response status code = 200
     */
    @Test
    public void testGoodStatusCode() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "x");
    	Response response = webtarg.request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test that response status code = 404
     */
    @Test
    public void testBadStatusCode() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "xx");
    	Response response = webtarg.request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    /**
     * Test that the JSON suggestions response contains no results
     */
    @Test
    public void testNoResult() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "xx");
    	try {
    		// calling the line below should produce an exception
        	webtarg.request().get(String.class);
            assert false;
		} catch (NotFoundException e) {
            assert true;
		}
    }

    /**
     * Test that the JSON suggestions response contains 1 result for the letter 'x'
     */
    @Test
    public void testOneResult() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "x");
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        assertEquals(1, jsonArr.length());
    }

    /**
     * Test that the JSON suggestions response contains 762 results for the letter 's'
     */
    @Test
    public void testMultipleResults() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "s");
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        assertEquals(762, jsonArr.length());
    }

    /**
     * Test that the JSON response contains name attribute
     */
    @Test
    public void testAttributeName() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "x");
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        JSONObject city = jsonArr.getJSONObject(0);
        assert (city.get("name") == null) ? false : true;
    }

    /**
     * Test that the JSON response contains latitude attribute
     */
    @Test
    public void testAttributeLatitude() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "x");
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        JSONObject city = jsonArr.getJSONObject(0);
        assert (city.get("latitude") == null) ? false : true;
    }

    /**
     * Test that the JSON response contains longitude attribute
     */
    @Test
    public void testAttributeLongitude() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "x");
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        JSONObject city = jsonArr.getJSONObject(0);
        assert (city.get("longitude") == null) ? false : true;
    }
    
    /**
     * Test that the JSON response contains score attribute
     */
    @Test
    public void testAttributeScore() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "x");
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        JSONObject city = jsonArr.getJSONObject(0);
        assert (city.get("score") == null) ? false : true;
    }
    
    /**
     * Test that the JSON response contains distance attribute
     */
    @Test
    public void testAttributeDistance() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "x");
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        JSONObject city = jsonArr.getJSONObject(0);
        assert (city.get("distance") == null) ? false : true;
    }
    
    /**
     * Test that name attribute is formatted like "City, State/Province, Country"
     */
    @Test
    public void testNameAttributeFormat() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "Qué");
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        JSONObject city = jsonArr.getJSONObject(0);
        assertEquals("Québec, QC, Canada", city.get("name"));
    }
    
    /**
     * Test that distance is calculated accurately, Québec to Montréal
     */
    @Test
    public void testDistanceCalculation() {
    	WebTarget webtarg = target.path("suggestions").queryParam("q", "Qué").queryParam("latitude", MTL_LATITUDE).queryParam("longitude", MTL_LONGITUDE);
        String responseMsg = webtarg.request().get(String.class);
        JSONArray jsonArr = new JSONObject(responseMsg).getJSONArray("suggestions");
        JSONObject city = jsonArr.getJSONObject(0);
        // round the distance to ignore decimal precision
        assertEquals(233, Math.round(city.getDouble("distance")));
    }
}
