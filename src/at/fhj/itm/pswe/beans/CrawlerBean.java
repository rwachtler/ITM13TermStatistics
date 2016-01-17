package at.fhj.itm.pswe.beans;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import at.fhj.itm.pswe.pagecrawler.CrawlerDaemon;

/**
 * 
 * Enterprise bean which triggers Webcrawler everyday
 *
 */
@Named("CrawlerBean")
@Singleton
public class CrawlerBean {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	public CrawlerBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Schedule function Invokes a thread of CrawlerDeamon
	 */
	@Schedule(hour = "2")
	public void runCrawler() {

		Thread t = new Thread(new CrawlerDaemon(em));
		t.start();
	}

}
