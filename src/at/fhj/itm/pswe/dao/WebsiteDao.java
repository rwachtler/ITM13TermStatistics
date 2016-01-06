package at.fhj.itm.pswe.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.tika.parser.iwork.IWorkPackageParser;
import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.dao.interfaces.IWebsite;
import at.fhj.itm.pswe.model.Website;

@Stateless
public class WebsiteDao implements IWebsite {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;



	/**
	 * Create new Website Object in DB
	 * @param address
	 * @param description
	 * @param depth
	 */
	@Override
	public Website createWebsite(String address, String description, int depth){
		// Create Website object
		Website ws = new Website();
		ws.setDomain(address);
		ws.setDescription(description);
		ws.setCrawldepth(depth);
		ws.setActive(true);
		ws.setLast_crawldate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

		// Save to DB
		em.persist(ws);
		em.flush();
		
		return ws;
	}

	@Override
	public JSONArray findAllWebsites(){
		TypedQuery<Website> findAllQuery = em.createQuery("SELECT DISTINCT w FROM Website w ORDER BY w.id",
				Website.class);
		final List<Website> queryResult = findAllQuery.getResultList();
		JSONArray result = new JSONArray();

		for (Website ws : queryResult) {
			JSONObject temp = new JSONObject();
			temp.put("id", ws.getId());
			temp.put("address", ws.getDomain());
			temp.put("description", ws.getDescription());
			temp.put("active", ws.getActive());
			temp.put("depth", ws.getCrawldepth());
			result.put(temp);
		}
		return result;
	}
	
	@Override
	public Website updateWebsite(Website ws){
		em.merge(ws);
		em.flush();
		
		return ws;
	}
	@Override
	public void deleteWebsite(int id){
		Website ws = em.find(Website.class, id);
		em.remove(ws);
	}
	
	
	
	/**
	 * Get a specific amount of words from a Website by its id
	 * 
	 * @param id
	 *            id of the Website
	 * @param maxNum
	 *            maximal amount of words
	 * @return JSONArray of desired Words of a Website
	 */
	@Override
	public JSONArray findWordsOFSite(int id, int maxNum) {
		Query q = em.createQuery("SELECT c.word.text, sum(c.amount)  "
				+ "FROM Container c WHERE c.website.id=:id AND c.word.active = TRUE "
				+ "GROUP BY c.word ORDER BY sum(c.amount) DESC").setParameter("id", id);

		if (maxNum > 0) {
			q.setMaxResults(maxNum);
		}
		List<Object[]> queryResults = q.getResultList();

		JSONArray result = new JSONArray();

		for (Object[] wo : queryResults) {
			System.out.println(wo[0] + " | " + wo[1]);

			JSONObject temp = new JSONObject();
			temp.put("word", wo[0]);
			temp.put("amount", wo[1]);

			result.put(temp);
		}
		return result;

	}
	
	/**
	 * Helper method for "countSiteOverPeriod" to get all Words from a given
	 * Website in a specific period of time
	 * 
	 * @param siteID
	 *            id of Website where the data should come from
	 * @param word
	 *            word, where data should be collected
	 * @param startDate
	 *            Date where the first dataset should start
	 * @param endDate
	 *            Date where the last dataset should end
	 * @return JSONArray of desired Data
	 */
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


	

	
	
	
	
	
}
