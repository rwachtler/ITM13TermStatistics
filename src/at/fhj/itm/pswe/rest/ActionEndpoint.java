package at.fhj.itm.pswe.rest;


import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
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
	
	@Resource
	private ManagedThreadFactory mtf;
		
	@Inject
	MainCrawler mc;
	
	
	
	// TODO Rewrite to post, add depth param
	@GET
	@Path("/crawler/{id}")
	public Response startCrawler(@PathParam("id") int id) {
		System.out.println("ID: " + id);
		Website ws = em.find(Website.class, id);
		
		/*Word w=new Word();
		w.setActive(true);
		w.setText("hello");
		//em.persist(w);
		Container c= new Container();
		c.setAmount(10);
		c.setWord(w);
		c.setLogDate("01.10.2015");
		c.setWebsite(ws);
		
		em.persist(c);
		*/
		
		//tb.executeCrawl(ws.getDomain(), 2);
		
		mc.setDepth(2);
		mc.setUrl(ws.getDomain());
		System.out.println("MC: "+mc.getUrl());
		mc.crawl();
		//Thread t = mtf.newThread(mc);
		//t.start();
		
		return Response.ok().build();
	}

}