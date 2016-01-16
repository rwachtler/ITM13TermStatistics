package at.fhj.itm.pswe.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.dao.interfaces.IWebsite;
import at.fhj.itm.pswe.model.Article;
import at.fhj.itm.pswe.model.Website;

@Stateless
public class WebsiteDao implements IWebsite {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	@Override
	public Website createWebsite(String address, String description, int depth) {
		// Create Website object
		Website ws = new Website();
		ws.setDomain(address);
		ws.setDescription(description);
		ws.setCrawldepth(depth);
		ws.setActive(true);

		ws.setLast_crawldate("1970-01-01");

		// Actual Date for later use
		// ws.setLast_crawldate(new
		// SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

		// Save to DB
		em.persist(ws);
		em.flush();

		return ws;
	}

	@Override
	public Website readWebsite(String url) {
		Query q = em
				.createQuery(
						"SELECT ws.id, ws.domain, ws.last_crawl_date, ws.description, ws.active FROM Website ws WHERE ws.domain = :url")
				.setParameter("url", url);

		List<Object[]> queryResults = q.getResultList();

		Website ws = new Website();
		ws.setId((int) queryResults.get(0)[0]);
		ws.setDomain((String) queryResults.get(0)[1]);
		ws.setLast_crawldate((String) queryResults.get(0)[2]);
		ws.setDescription((String) queryResults.get(0)[3]);
		if ((int) queryResults.get(0)[4] == 0) {
			ws.setActive(false);
		} else {
			ws.setActive(true);
		}

		return ws;
	}

	@Override
	public List<Website> findAllWebsites() {
		TypedQuery<Website> findAllQuery = em.createQuery("SELECT DISTINCT w FROM Website w ORDER BY w.id",
				Website.class);
		return findAllQuery.getResultList();
	}

	@Override
	public JSONArray findAllWebsitesJSON() {
		final List<Website> queryResult = findAllWebsites();
		JSONArray result = new JSONArray();

		for (Website ws : queryResult) {
			JSONObject temp = new JSONObject();
			temp.put("id", ws.getId());
			temp.put("address", ws.getDomain());
			temp.put("description", ws.getDescription());
			temp.put("active", ws.getActive());
			temp.put("lCrawled", ws.getLast_crawldate());
			temp.put("depth", ws.getCrawldepth());
			result.put(temp);
		}
		return result;
	}

	
	@Override
	public Website updateWebsite(Website ws) {

			
		Website toUpdate = em.find(Website.class, ws.getId());
		if(toUpdate !=null){
			toUpdate.setDescription(ws.getDescription());
			toUpdate.setCrawldepth(ws.getCrawldepth());
			toUpdate.setActive(ws.getActive());
			em.flush();
			return toUpdate;
		}else{
			return null;
		}
		
		

		
	}

	@Override
	public void deleteWebsite(int id) {
		Website ws = em.find(Website.class, id);
		em.remove(ws);
	}

	@Override
	public JSONArray findWordsOfSite(int id, int maxNum) {
		Query q = em.createQuery("SELECT c.word.text, sum(c.amount) "
				+ "FROM Container c WHERE c.website.id=:id AND c.word.active = TRUE "
				+ "GROUP BY c.word ORDER BY sum(c.amount) DESC").setParameter("id", id);

		if (maxNum > 0) {
			q.setMaxResults(maxNum);
		}
		List<Object[]> queryResults = q.getResultList();

		JSONArray result = new JSONArray();

		for (Object[] wo : queryResults) {
			System.out.println("findWordsOfSite: " + wo[0] + " | " + wo[1]);

			JSONObject temp = new JSONObject();
			temp.put("word", wo[0]);
			temp.put("amount", wo[1]);

			result.put(temp);
		}
		return result;

	}

	@Override
	public JSONArray timeLine4WordAndSite(int siteID, String word, String startDate, String endDate) {

		Query countWordPeriod = em.createQuery("SELECT co.logDate, SUM(co.amount) " + "FROM Container co "
				+ "WHERE co.website.id = :id " + "AND (co.logDate BETWEEN :startdate AND :enddate) "
				+ "AND co.word.text = :word " + "GROUP BY co.word.text, co.logDate");
		countWordPeriod.setParameter("id", siteID);
		countWordPeriod.setParameter("word", word);
		countWordPeriod.setParameter("startdate", startDate);
		countWordPeriod.setParameter("enddate", endDate);

		final List<Object[]> queryResult = countWordPeriod.getResultList();

		JSONArray result = new JSONArray();

		for (Object[] wo : queryResult) {
			JSONObject temp = new JSONObject();
			temp.put("date", wo[0]);
			temp.put("amount", wo[1]);

			result.put(temp);
		}

		return result;
	}

	@Override
	public List<Article> findAllArticlesOfOneWebsite(int id) {
		Query q = em.createQuery("SELECT DISTINCT c.article.id, c.article.url "
				+ "FROM Container c WHERE c.website.id=:id ORDER BY c.article.id").setParameter("id", id);

		List<Object[]> queryResults = q.getResultList();

		ArrayList<Article> list = new ArrayList<>();

		for (Object[] obj : queryResults) {
			Article ar = new Article();
			// System.out.println("findAllArticlesOfOneWebsite: Obj[0]: " + obj[0] + " | obj[1]: " + obj[1]);
			ar.setId(Integer.parseInt(obj[0].toString()));
			ar.setUrl(obj[1].toString());
			list.add(ar);
		}

		return list;
	}

	@Override
	public JSONArray findAllArticlesOfOneWebsiteJSON(int id) {
		final List<Article> queryResult = findAllArticlesOfOneWebsite(id);
		JSONArray result = new JSONArray();

		for (Article ar : queryResult) {
			JSONObject temp = new JSONObject();
			temp.put("id", ar.getId());
			temp.put("url", ar.getUrl());
			result.put(temp);
		}
		return result;
	}
	
	@Override
	public JSONArray articleTimelineofOneWebsite(int id) {
		Query q = em.createQuery("SELECT COUNT(DISTINCT c.article.url), c.logDate, c.article.id "
				+ "FROM Container c WHERE c.website.id=:id GROUP BY c.logDate, c.website.id ORDER BY c.article.id").setParameter("id", id);

		List<Object[]> queryResults = q.getResultList();

		JSONArray list = new JSONArray();

		for (Object[] obj : queryResults) {
			JSONObject json_obj = new JSONObject();
			// System.out.println("findAllArticlesOfOneWebsite: Obj[0]: " + obj[0] + " | obj[1]: " + obj[1]);
			json_obj.put("amount", obj[0]);
			json_obj.put("crawldate", obj[1]);
			list.put(json_obj);
		}

		return list;
	}
	
	@Override
	public JSONArray getAverageWebsiteStats(int id){
		Query q = em.createQuery("SELECT SUM(ws.crawlDuration)/COUNT(ws.id), SUM(ws.analyzeDuration)/COUNT(ws.analyzeDuration), ws.website.domain "
				+ "FROM WebsiteStat ws WHERE ws.website.id=:id GROUP BY ws.website.id, ws.website.domain ORDER BY ws.website.domain").setParameter("id", id);

		List<Object[]> queryResults = q.getResultList();

		JSONArray list = new JSONArray();
		
		JSONArray list2 = articleTimelineofOneWebsite(id);
		
		int article_amount = 0;
		
		for (int i = 0; i < list2.length(); i++) {
		    JSONObject c = list2.getJSONObject(i);
		    article_amount += c.getInt("amount");
		}
		
		double average_amount = article_amount / list2.length();
		
		for (Object[] obj : queryResults) {
			JSONObject json_obj = new JSONObject();
			// System.out.println("findAllArticlesOfOneWebsite: Obj[0]: " + obj[0] + " | obj[1]: " + obj[1]);
			json_obj.put("crawlDuration", obj[0]);
			json_obj.put("analyzeDuration", obj[1]);
			json_obj.put("domain", obj[2]);
			json_obj.put("avg_article", average_amount);
			list.put(json_obj);
		}
		
		return list;
	}
	

	public JSONArray getAverageWordAmountofWebsite(int id){
		Query q = em.createQuery("SELECT SUM(c.amount)*1.0/COUNT(DISTINCT c.article.id), SUM(LENGTH(c.word.text))*1.0/COUNT(c.word.text) "
				+ "FROM Container c WHERE c.website.id=:id GROUP BY c.website.id ORDER BY c.website.domain").setParameter("id", id);
		
		List<Object []> queryResults = q.getResultList();

		JSONArray list = new JSONArray();
		
		for (Object[] obj : queryResults) {
			JSONObject json_obj = new JSONObject();
			// System.out.println("getAverageWordAmountofWebsite: Obj[0]: " + obj[0] + " | obj[1]: " + obj[1]);
			json_obj.put("avgwords", obj[0]);
			json_obj.put("avgwordlength", obj[1]);
			list.put(json_obj);
		}
		
		return list;
	}

	public void setEntityManager(EntityManager mockEm) {
		this.em=em;
		
	}

}
