package at.fhj.itm.pswe.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.dao.interfaces.IArticle;
import at.fhj.itm.pswe.model.Article;

@Stateless
public class ArticleDao implements IArticle {

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	@Override
	public Article createArticle(String url) {
		// Create Website object
		Article ar = new Article();
		ar.setUrl(url);

		// Save to DB
		em.persist(ar);
		em.flush();

		return readArticle(url);
	}

	@Override
	public Article readArticle(String url) {
		Query q = em.createQuery("SELECT ar.id, ar.url FROM Article ar WHERE ar.url = :url").setParameter("url", url);

		List<Object[]> queryResults = q.getResultList();

		Article ar = new Article();
		ar.setId((int) queryResults.get(0)[0]);
		ar.setUrl((String) queryResults.get(0)[1]);

		return ar;
	}

	@Override
	public Article updateArticle(Article ar) {

		// Get Website, if already in Database
		Query q = em.createQuery("SELECT ar.id, ar.url FROM Article ar WHERE ar.url = :url").setParameter("url",
				ar.getUrl());

		List<Object[]> queryResults = q.getResultList();

		ar.setId((int) queryResults.get(0)[0]);
		ar.setUrl((String) queryResults.get(0)[1]);

		em.merge(ar);
		em.flush();

		return ar;
	}

	@Override
	public void deleteArticle(int id) {
		Article ar = em.find(Article.class, id);
		em.remove(ar);
	}

	@Override
	public JSONArray findWordsOfArticle(int id, int maxNum) {
		Query q = em.createQuery("SELECT SUM(c.amount), c.logDate "
				+ "FROM Container c WHERE c.article.id=:id AND c.word.active = TRUE "
				+ "GROUP BY c.article.id, c.logDate ORDER BY SUM(c.amount) DESC").setParameter("id", id);

		if (maxNum > 0) {
			q.setMaxResults(maxNum);
		}
		List<Object[]> queryResults = q.getResultList();

		JSONArray result = new JSONArray();

		for (Object[] wo : queryResults) {
			System.out.println("findWordsOfArticle: " + wo[0] + " | " + wo[1]);

			JSONObject temp = new JSONObject();
			temp.put("amount", wo[0]);
			temp.put("logDate", wo[1]);

			result.put(temp);
		}
		
		return result;
	}
	
	public JSONArray getAVGAnalyzeDurationofArticle(int id){
		Query q = em.createQuery("SELECT SUM(ast.analyzeDuration)/COUNT(ast.analyzeDuration) "
				+ "FROM ArticleStat ast WHERE ast.article.id=:id ORDER BY ast.article.url").setParameter("id", id);
		
		List<Long> queryResults = q.getResultList();

		JSONArray result = new JSONArray();

		for (Long wo : queryResults) {
			System.out.println("getAVGAnalyzeDurationofArticle: " + wo);

			JSONObject temp = new JSONObject();
			temp.put("avgAnalyze", wo);

			result.put(temp);
		}
		
		return result;
	}

}
