package at.fhj.itm.pswe.rest;

import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.dao.WordDao;

@Stateless
@Path("/word")
public class WordEndpoint {
	
	private WordDao wDao;
	

	/**
	 * Setter Injection to make testing easier
	 * @param wDao
	 */
	@Inject
	public void setwDao(WordDao wDao) {
		this.wDao = wDao;
	}

	
	/**
	 * Get all Words from all Websites with the actual word, active status and
	 * amount
	 * 
	 * @return JSONObject with jsonarray of data
	 */
	@GET
	@Produces("application/json")
	public Response listAllwithAmount() {
		JSONObject my = new JSONObject();
		my.put("data", wDao.wordAndAmount());

		return Response.ok(my.toString()).build();
	}

	// For Datatable on Subsite "Website"
	/**
	 * Get all Websites that contain the given word
	 * 
	 * @param word
	 *            word, which should be checked on which websites it appears
	 * @return JSONData of the the desired information
	 */
	@GET
	@Path("/{word}/websites")
	@Produces("application/json")
	public Response sitesOfWord(@PathParam("word") String word) {
		

		JSONObject my = new JSONObject();
		my.put("data", wDao.sitesOfWord(word));

		return Response.ok(my.toString()).build();
	}

	/**
	 * Update a Word
	 * 
	 * @param incoming
	 *            JSONData of the Word that should be updated
	 * @return same JSONData from input if process was successful
	 */
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	public Response editWord(String incoming) {
		
		JSONObject json = new JSONObject(incoming);

		// Get first key which is ID
		Iterator<String> keys = json.keys();
		String id = "";
		if (keys.hasNext()) {
			id = keys.next(); // First key in your json object
		}

		//Update Object in DAO		
		if (json.getJSONObject(id).getJSONArray("active").length() == 0)
			wDao.changeWordActive(id, false);
		else
			wDao.changeWordActive(id, true);

		JSONObject output = new JSONObject();
		output.put("data", new JSONArray().put(wDao.findSingleWordWithAmount(id)));

		// Add info for Return object
		return Response.ok(output.toString()).build();
	}


	/**
	 * Ein Wort auf vorkommenden Seiten mit Anzahl und Datum mit Zeituebergabe
	 * Einschraenkung Analyse the amount of a word over a certain period
	 * 
	 * @param word
	 *            the word which should be counted
	 * @param startdate
	 *            the date as String when the analysis should start
	 * @param enddate
	 *            the date as String when the analysis should end
	 * @return List<Integer> with the amount of the word Example:
	 *         http://localhost:8080/TermStatistics/rest/word/{someWord}/period/
	 *         10.11.2015/30.11.2015
	 */
	@GET
	@Path("/{word:[a-zA-Z][a-zA-Z]*}/period/{startdate:[a-zA-Z0-9][a-zA-Z0-9.]*}/{enddate:[a-zA-Z0-9][a-zA-Z0-9.]*}")
	@Produces("application/json")
	public Response countWordOverPeriod(@PathParam("word") String word, @PathParam("startdate") String startdate,
			@PathParam("enddate") String enddate) {
		
		

		JSONObject my = new JSONObject();
		my.put(word, wDao.wordCountOverPeriod(word, startdate, enddate));
		System.out.println(my.toString());

		return Response.ok(my.toString()).build();
	}
}