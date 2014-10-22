/**
 * 
 */
package com.saintsoftware.interfaces;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Russell Saint Cyr
 *
 */
public interface RouteInterface  {
	
	Response handleGet(UriInfo info);
	String handlePost();
	String handlePut();
	String handleDelete();
}
