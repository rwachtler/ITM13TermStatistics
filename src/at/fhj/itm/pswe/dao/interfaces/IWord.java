package at.fhj.itm.pswe.dao.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IWord {

	/**
	 * Finds all words of all websites and additionally the amount and the
	 * status
	 * 
	 * @return JSONArray of JSONObject of Word-Text, Amount and active
	 */
	public JSONArray wordAndAmount();

	/**
	 * Finds the corresponding website and the found amount on the website of
	 * one word
	 * 
	 * @param word
	 *            Desired Word
	 * @return JSONArray JSONArray of all Websites
	 */
	public JSONArray sitesOfWord(String word);

	/**
	 * Toggle active/inactive state of one specific word
	 * 
	 * @param word
	 *            String of word
	 * @param active
	 *            boolean
	 */
	public void changeWordActive(String word, boolean active);

	/**
	 * Helper method for "editWord" to get all informations of a desired word
	 * 
	 * @param word
	 *            word, where the information is desired
	 * @return JSONData from the desired word
	 */
	public JSONObject findSingleWordWithAmount(String word);

	/**
	 * Finds and counts all words for a specific period of time
	 * 
	 * @param word
	 *            desired word
	 * @param startdate
	 *            desired start date
	 * @param enddate
	 *            desired end date
	 * @return JSONArray of desired Data
	 */
	public JSONArray wordCountOverPeriod(String word, String startdate, String enddate);
}
