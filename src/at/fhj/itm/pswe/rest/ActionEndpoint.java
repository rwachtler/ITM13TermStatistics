package at.fhj.itm.pswe.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			Date current=df.parse(df.format(cal.getTime()));
			System.out.println("Current "+current.toString());
			System.out.println("ws: "+df.parse(ws.getLast_crawldate()).toString());
		
			
			if(df.parse(ws.getLast_crawldate()).before(current)){
				
				mc=new MainCrawler();
				mc.setDepth(2);
				mc.setUrl(ws.getDomain());
				mc.setAnalyzer(analyzer);
				System.out.println("MC: " + mc.getUrl());

				Thread t = mtf.newThread(mc);
				t.start();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		return Response.ok().build();
	}

}