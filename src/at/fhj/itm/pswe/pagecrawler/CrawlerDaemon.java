package at.fhj.itm.pswe.pagecrawler;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CrawlerDaemon implements Runnable {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	public CrawlerDaemon(){

	}


	@Override
	public void run() {

//		TypedQuery<Website> findAllQuery = em.createQuery("SELECT DISTINCT w FROM Website w ORDER BY w.id", Website.class);
//		final List<Website> results = findAllQuery.getResultList();
//
//		for(Website ws:results){
//			MainCrawler mc= new MainCrawler(ws.getDomain(),ws.getCrawldepth());
//			mc.crawl();
//		}

		System.out.println("DEAMON \n\n\n\n\n\n\n\n\n\n\n\n");

		//Read all urls from db
		//for loop
		//create maincrawler objects
		//crawl

	}

}
