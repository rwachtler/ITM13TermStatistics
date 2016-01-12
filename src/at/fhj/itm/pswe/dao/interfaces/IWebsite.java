package at.fhj.itm.pswe.dao.interfaces;

import java.util.List;

import org.json.JSONArray;

import at.fhj.itm.pswe.model.Article;
import at.fhj.itm.pswe.model.Website;

public interface IWebsite {

	/**
	 * Create new Website Object in DB
	 * 
	 * @param address
	 * @param description
	 * @param depth
	 */
	Website createWebsite(String address, String description, int depth);

	/**
	 * Read persisted Website by its URL
	 * 
	 * @param url
	 *            URL of the Website
	 * @return complete Website Object
	 */
	Website readWebsite(String url);

	/**
	 * Read all persisted Websites
	 * 
	 * @return List of all persisted Websites
	 */
	List<Website> findAllWebsites();

	/**
	 * Read all persisted Websites
	 * 
	 * @return JSONArray of all persisted Websites
	 */
	JSONArray findAllWebsitesJSON();

	/**
	 * Update a persisted Website
	 * 
	 * @param ws
	 *            New Website Object
	 * @return Updated persisted Website Object
	 */
	Website updateWebsite(Website ws);

	/**
	 * Delete a persisted Website
	 * 
	 * @param id
	 *            ID of the Website
	 */
	void deleteWebsite(int id);

	/**
	 * Get a specific amount of words from a Website by its id
	 * 
	 * @param id
	 *            id of the Website
	 * @param maxNum
	 *            maximal amount of words
	 * @return JSONArray of desired Words of a Website
	 */
	JSONArray findWordsOfSite(int id, int maxNum);

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
	JSONArray timeLine4WordAndSite(int siteID, String word, String startDate, String endDate);

	/**
	 * Get a List of all Articles from one Website
	 * 
	 * @param id
	 *            ID of the Website
	 * @return List of all Articles on the Website
	 */
	List<Article> findAllArticlesOfOneWebsite(int id);

	/**
	 * Get a List of all Articles from one Website
	 * 
	 * @param id
	 *            ID of the Website
	 * @return JSONArray of all Articles on the Website
	 */
	JSONArray findAllArticlesOfOneWebsiteJSON(int id);

}
