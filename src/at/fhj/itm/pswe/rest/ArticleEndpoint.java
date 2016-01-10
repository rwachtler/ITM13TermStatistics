package at.fhj.itm.pswe.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import at.fhj.itm.pswe.dao.interfaces.IArticle;

@Stateless
@Path("/article")
public class ArticleEndpoint {
	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	private IArticle arDao;

	/**
	 * Setter Injection to make testing easier
	 * 
	 * @param wDao
	 */
	@Inject
	public void setarDao(IArticle arDao) {
		this.arDao = arDao;
	}

	// For Datatable on Subsite "Article"
	/**
	 * Get all Words of one Article
	 * 
	 * @param id
	 *            id of the Website
	 * @return JSON-Data
	 */
	@GET
	@Path("/{id:[0-9][0-9]*}/words")
	@Produces("application/json")
	public Response wordsOfArticle(@PathParam("id") int id) {

		JSONObject my = new JSONObject();
		my.put("data", arDao.findWordsOfArticle(id, 0));

		return Response.ok(my.toString()).build();
	}

	/**
	 * Get just a specific amount of Words of one Article
	 * 
	 * @param id
	 *            id of the Article
	 * @param num
	 *            maximal amount of words
	 * @return JSONArray of desired Words of a Article
	 */
	@GET
	@Path("/{id:[0-9][0-9]*}/words/{num:[0-9][0-9]*}")
	@Produces("application/json")
	public Response wordsOfArticleNumbered(@PathParam("id") int id, @PathParam("num") int num) {

		JSONObject my = new JSONObject();
		my.put("data", arDao.findWordsOfArticle(id, num));

		return Response.ok(my.toString()).build();
	}

}