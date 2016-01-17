package at.fhj.itm.pswe.dao.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.model.Word;

public interface IWord {

	/**
	 * Finds all words of all websites and additionally the amount and the
	 * status
	 * 
	 * @return JSONArray of JSONObject of Word-Text, Amount and active
	 */
	JSONArray wordAndAmount();

	/**
	 * Finds the corresponding website and the found amount on the website of
	 * one word
	 * 
	 * @param word
	 *            Desired Word
	 * @return JSONArray JSONArray of all Websites
	 */
	JSONArray sitesOfWord(String word);

	/**change active/inactive and type of one specific word
	 * 
	 * @param Word w
	
	 */
	void updateWord(Word w);

	/**
	 * Helper method for "editWord" to get all informations of a desired word
	 * 
	 * @param word
	 *            word, where the information is desired
	 * @return JSONData from the desired word
	 */
	JSONObject findSingleWordWithAmount(String word);

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
	JSONArray wordCountOverPeriod(String word, String startdate, String enddate);

	/**
	 * Returns all wordtype formattet as dt options
	 * 
	 * @return
	 */
	JSONArray wordTypeAsOption();
}
