package at.fhj.itm.pswe.rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import at.fhj.itm.pswe.pagecrawler.MainCrawler;

@Stateless
@Path("/action")
public class ActionEndpoint {

	@GET
	@Path("/crawler/{url : .+}")
	public Response startCrawler(@PathParam("url") String url) {
		System.out.println("URL: " + url);
		Thread t = new Thread(new MainCrawler(url));
		
		t.start();
		return Response.ok().build();
	}

}