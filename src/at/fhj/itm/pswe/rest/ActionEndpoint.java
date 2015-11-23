package at.fhj.itm.pswe.rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Stateless
@Path("/action")
public class ActionEndpoint{
	
	@GET
	@Path("/crawl/{url: .+}")
	public void startCrawl(@PathParam("url") String url){
		System.out.println("URL: " + url);
	}
}