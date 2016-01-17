package at.fhj.itm.pswe.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.pagecrawler.MainCrawler;
import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;

/**
 * 
 * Enterprise bean which triggers Webcrawler everyday
 *
 */
@Named("CrawlerBean")
@Singleton
public class CrawlerDaemonBean {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;
	
	@Resource
	private ManagedThreadFactory mtf;
	
	@Inject
	Analyzer analyzer;

	public CrawlerDaemonBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Schedule function Invokes a thread of CrawlerDeamon
	 */
	@Schedule(hour = "2")
	public void runCrawler() {

		TypedQuery<Website> findAllQuery = em
				.createQuery("SELECT DISTINCT w FROM Website w WHERE w.active = TRUE ORDER BY w.id", Website.class);
		final List<Website> results = findAllQuery.getResultList();
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		

		for (Website ws : results) {

			System.out.println("Crawler-Daemon: " + ws.getDomain());
			try {
				Date current=df.parse(df.format(cal.getTime()));
				System.out.println("Current "+current.toString());
				System.out.println("ws: "+df.parse(ws.getLast_crawldate()).toString());
			
				
				if(df.parse(ws.getLast_crawldate()).before(current)){
					MainCrawler mc = new MainCrawler();
					mc.setDepth(ws.getCrawldepth());
					mc.setUrl(ws.getDomain());
					mc.setAnalyzer(analyzer);
					
					Thread t = mtf.newThread(mc);
					t.start();
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		

		}
	}

}
