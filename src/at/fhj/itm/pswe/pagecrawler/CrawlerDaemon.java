package at.fhj.itm.pswe.pagecrawler;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.fhj.itm.pswe.model.Website;

public class CrawlerDaemon implements Runnable {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	public CrawlerDaemon() {

	}

	@Override
	public void run() {

		TypedQuery<Website> findAllQuery = em.createQuery("SELECT DISTINCT w FROM Website w WHERE w.active = TRUE ORDER BY w.id", Website.class);
		final List<Website> results = findAllQuery.getResultList();

		for (Website ws : results) {
			MainCrawler mc = new MainCrawler(ws.getDomain(), ws.getCrawldepth());
			mc.crawl();
		}

	}

}
