package at.fhj.itm.pswe.rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import at.fhj.itm.pswe.pagecrawler.MainCrawler;

@Stateless
@Path("/action")
public class ActionEndpoint {

	@GET
	@Path("/crawler/{url : .+}")
	public void startCrawler(@PathParam("url") String url) {
		System.out.println("URL: " + url);
		MainCrawler crawler = new MainCrawler(url);
		crawler.crawl();
	}

}