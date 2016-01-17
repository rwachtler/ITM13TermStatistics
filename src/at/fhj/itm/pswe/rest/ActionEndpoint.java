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

import org.jboss.ejb3.annotation.TransactionTimeout;

import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.pagecrawler.MainCrawler;
import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;

@Stateless
@Path("/action")
public class ActionEndpoint {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	@Resource
	private ManagedThreadFactory mtf;
	
	@Inject
	Analyzer analyzer;

	
	MainCrawler mc;

	// TODO Rewrite to post, add depth param
	@GET
	@Path("/crawler/{id}")
	@TransactionTimeout(3600)
	public Response startCrawler(@PathParam("id") int id) {
		System.out.println("ID: " + id);
		Website ws = em.find(Website.class, id);
		mc=new MainCrawler();
		mc.setDepth(2);
		mc.setUrl(ws.getDomain());
		mc.setAnalyzer(analyzer);
		System.out.println("MC: " + mc.getUrl());

		Thread t = mtf.newThread(mc);
		t.start();

		return Response.ok().build();
	}

}