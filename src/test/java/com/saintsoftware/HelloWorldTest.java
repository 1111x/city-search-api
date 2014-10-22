package com.saintsoftware;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Russell Saint Cyr
 *
 */
public class HelloWorldTest {

    private HttpServer server;
    private WebTarget target;
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(HelloWorldTest.class.getName());	

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
     * Test to see that the JSON message "{"Hello":"World!"}" is sent in the response.
     */
    @Test
    public void testHelloWorld() {
        String responseMsg = target.path("hello").request().get(String.class);
        String jsonValue = new JSONObject(responseMsg).getString("Hello");
        assertEquals("World!", jsonValue);
    }

}
