package at.fhj.itm.pswe.rest;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.pagecrawler.MainCrawler;

@Stateless
@Path("/action")
public class ActionEndpoint {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	// TODO Rewrite to post, add depth param
	@GET
	@Path("/crawler/{id}")
	public Response startCrawler(@PathParam("id") int id) {
		System.out.println("ID: " + id);
		Website ws = em.find(Website.class, id);
		Thread t = new Thread(new MainCrawler(ws.getDomain(), 2));

		t.start();
		return Response.ok().build();
	}

}