package at.fhj.itm.pswe.rest;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.dao.WebsiteDao;
import at.fhj.itm.pswe.dao.WordDao;
import at.fhj.itm.pswe.model.Website;

@Stateless
@Path("/website")
public class WebsiteEndpoint {
	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	private WebsiteDao wDao;
	
	/**
	 * Setter Injection to make testing easier
	 * @param wDao
	 */
	@Inject
	public void setwDao(WebsiteDao wDao) {
		this.wDao = wDao;
	}

	
	
	
	/**
	 * Add a website to the database
	 * 
	 * @param incoming
	 *            JSON-Data as String
	 * @return Sent data + id from Database + active status as JSON
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response addSite(String incoming) {
		JSONObject json = new JSONObject(incoming);

		Website ws=wDao.createWebsite(json.getString("address"), json.getString("description"), json.getInt("depth"));
		// Add info for Return object
		json.put("id", ws.getId());
		json.put("active", ws.getActive());
	
		//TODO: Inject new maincrawler object and start a crawl
		/*
		 * Depcrecated---Thread t = new Thread(new MainCrawler(ws.getDomain(), 1)); t.start();
		 */

		return Response.ok(new JSONObject().put("data", new JSONArray().put(json)).toString()).build();
	}

	/**
	 * List all Websites that are currently in the Database
	 * 
	 * @return JSON with all Websites in the Database
	 */
	@GET
	@Produces("application/json")
	public Response listAll() {
		JSONObject my = new JSONObject();
		my.put("data", wDao.findAllWebsites());

		return Response.ok(my.toString()).build();
	}

	// For Datatable on Subsite "Website"
	/**
	 * Get all Words of one Website
	 * 
	 * @param id
	 *            id of the Website
	 * @return JSON-Data
	 */
	@GET
	@Path("/{id:[0-9][0-9]*}/words")
	@Produces("application/json")
	public Response wordsOfSite(@PathParam("id") int id) {

		JSONObject my = new JSONObject();
		my.put("data", wDao.findWordsOFSite(id, 0));

		return Response.ok(my.toString()).build();
	}

	/**
	 * Get just a specific amount of Words of one Website
	 * 
	 * @param id
	 *            id of the Website
	 * @param num
	 *            maximal amount of words
	 * @return JSONArray of desired Words of a Website
	 */
	@GET
	@Path("/{id:[0-9][0-9]*}/words/{num:[0-9][0-9]*}")
	@Produces("application/json")
	public Response wordsOfSiteNumbered(@PathParam("id") int id, @PathParam("num") int num) {

		JSONObject my = new JSONObject();
		my.put("data", wDao.findWordsOFSite(id, num));

		return Response.ok(my.toString()).build();
	}

	

	/**
	 * Returns all words from a given domain in the chosen period
	 * 
	 * @param domainID
	 *            site where words should be counted
	 * @param startdate
	 *            the date as String when the analysis should start
	 * @param enddate
	 *            the date as String when the analysis should end
	 * @return List<Integer> with the amount of the word Example:
	 *         http://localhost:8080/TermStatistics/rest/website/1/period/10.11.
	 *         2015/30.11.2015
	 */
	@GET
	@Path("/{domainID:[0-9][0-9]*}/period/{startdate:[a-zA-Z0-9][a-zA-Z0-9.]*}/{enddate:[a-zA-Z0-9][a-zA-Z0-9.]*}")
	@Produces("application/json")
	public Response countSiteOverPeriod(@PathParam("domainID") int domainID, @PathParam("startdate") String startDate,
			@PathParam("enddate") String endDate) {

		// Get top10 words of page
		JSONArray topwords = wDao.findWordsOFSite(domainID, 10);
		JSONObject output = new JSONObject();

		// for each word, geht timeline
		for (int i = 0; i < topwords.length(); i++) {
			String word = topwords.getJSONObject(i).getString("word");
			output.put(word, wDao.timeLine4WordAndSite(domainID, word, startDate, endDate));

		}
		return Response.ok(output.toString()).build();
	}

	
	/**
	 * Update an already saved Website.
	 * 
	 * @param incoming
	 *            JSON Data with all informations of the Website
	 * @return same JSON Data, if the update was successful
	 */
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	public Response editSite(String incoming) {
		
		JSONObject json = new JSONObject(incoming);
		
		// Get first key which is id
		Iterator<String> keys = json.keys();
		String id = "";
		if (keys.hasNext()) {
			id = keys.next(); // First key in your json object
		}

		// Update and Save object
		Website ws = new Website();
		ws.setDomain(json.getJSONObject(id).getString("address"));
		ws.setDescription(json.getJSONObject(id).getString("description"));
		ws.setCrawldepth(json.getJSONObject(id).getInt("depth"));

		if (json.getJSONObject(id).getJSONArray("active").length() == 0) {
			ws.setActive(false);
		} else {
			ws.setActive(true);
		}

		Website wsOut=wDao.updateWebsite(ws);
		
		JSONObject output = new JSONObject();
		output.put("data",
				new JSONArray().put(new JSONObject().put("id", wsOut.getId()).put("address", wsOut.getDomain())
						.put("description", wsOut.getDescription()).put("depth", wsOut.getCrawldepth())
						.put("active", wsOut.getActive())));

		// Add info for Return object
		System.out.println("JSON: " + output.toString());
		return Response.ok(output.toString()).build();
	}

	/**
	 * Delete a Website from the Database
	 * 
	 * @param incoming
	 *            all informations of the Website that should be deleted
	 * @return empty JSON if successful
	 */
	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	public Response deleteSite(String incoming) {
		
		JSONObject json = new JSONObject(incoming);
	
		// Get first key which is id
		Iterator<String> keys = json.keys();
		String id = "";
		if (keys.hasNext()) {
			id = keys.next(); // First key in your json object
		}
		
		wDao.deleteWebsite(Integer.parseInt(id));

		// Send empty object on success
		return Response.ok("{}").build();
	}

}