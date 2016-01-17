package at.fhj.itm.pswe.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import at.fhj.itm.pswe.model.Article;
import at.fhj.itm.pswe.model.ArticleStat;
import at.fhj.itm.pswe.model.Container;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.model.WebsiteStat;
import at.fhj.itm.pswe.model.Word;
import at.fhj.itm.pswe.model.WordlistEntry;
import at.fhj.itm.pswe.model.Wordtype;

/**
 * Includes all DB access methods which the analyzer needs
 * 
 *
 */
@Stateless
public class AnalyzerDao {

	private EntityManager em;

	@PersistenceContext(unitName = "TermStatistics")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public Word findWord(String word) {
		return em.find(Word.class, word);
	}

	public String findTypeForWord(String word) {
		Query q = em.createQuery("SELECT wl FROM WordlistEntry wl WHERE wl.word = :word").setParameter("word", word);

		List<WordlistEntry> queryResults = q.getResultList();

		if (queryResults.size() == 0) {
			// word not found in wordlist --> we set it to unknown
			return "unknown";
		} else {
			// we use the wordtype found in the wordlist
			return queryResults.get(0).getWordtype();
		}
	}

	public void setTypeForWord(Word wo, String type) {
		// look up the word type in our own wordtype table
		Wordtype wt;

		Query wordTypeQuery = em.createQuery("SELECT wt FROM Wordtype wt WHERE wt.texttype = :wordtype")
				.setParameter("wordtype", type);

		List<Wordtype> wordTypeQueryResults = wordTypeQuery.getResultList();

		if (wordTypeQueryResults.size() == 0) {
			// wordtype not found --> we add it
			wt = new Wordtype();
			wt.setTexttype(type);

			em.persist(wt);
		} else {
			// wordtype found --> we can use it
			wt = wordTypeQueryResults.get(0);
		}
		wo.setWordtype(wt);

		// Need to persist so that it is available for newCont later on
		em.persist(wo);
	}

	public Article findArticle(Article ar) {
		Query q = em.createQuery("SELECT a.id, a.url FROM Article a WHERE a.url = :url").setParameter("url",
				ar.getUrl());

		List<Object[]> queryResults = q.getResultList();
		if (queryResults.size() == 0) {
			// if article not in database, persist it
			System.out.println("Add Article: " + ar.getUrl());
			em.persist(ar);
		} else {
			// else get the one from Database
			ar = em.find(Article.class, queryResults.get(0)[0]);
		}
		return ar;
	}

	public void saveContainer(Container co) {
		em.persist(co);

	}

	public void saveArticleStat(ArticleStat as) {
		em.persist(as);
	}

	public void saveWebsiteStat(WebsiteStat webStat) {
		em.persist(webStat);
	}

	public Website findWebsite(String url) {
		// Expect only one result
		Query websiteQuery = em.createQuery("SELECT w FROM Website w WHERE w.domain = :domain").setParameter("domain",
				url);
		List<Website> result = websiteQuery.getResultList();
		int websiteId = -1;
		Website returnSite = null;
		;
		if (result.size() > 0)
			returnSite = result.get(0);
		else
			System.out.println("Site not found");
		return returnSite;

	}

	public void flushDAO() {
		em.flush();
		em.clear();
	}

	public void updateCrawlDateofWebsite(String domain) {
		System.out.println("Setting time");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Query q = em.createQuery("UPDATE Website w SET crawldate = :crawldate WHERE w.domain = :domain")
				.setParameter("crawldate", df.format(cal.getTime())).setParameter("domain", domain);

		int updatenumber = q.executeUpdate();
	}

}
