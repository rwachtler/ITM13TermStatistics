package at.fhj.itm.pswe.dao.interfaces;

import org.json.JSONArray;

import at.fhj.itm.pswe.model.Article;

public interface IArticle {

	/**
	 * Create new Article Object in DB
	 * 
	 * @param ar
	 *            Article Object
	 * @return persisted Article
	 */
	Article createArticle(Article ar);

	/* NOT USED
	/**
	 * Get the persisted Article Object from the DB
	 * 
	 * @param url
	 *            URL of the Article
	 * @return Complete Article Object
	 *
	Article readArticle(String url);
	 */

	/**
	 * Update an Article Object in DB
	 * 
	 * @param ar
	 *            new Article Object
	 */
	Article updateArticle(Article ar);

	/**
	 * Delete an Article Object in DB
	 * 
	 * @param id
	 *            id of the article
	 */
	void deleteArticle(int id);

	/**
	 * Find all Words of one Article by its id and the date of insertion
	 * 
	 * @param id
	 *            id of the article
	 * @param maxNum
	 *            Maximum amount of data
	 */
	JSONArray findWordsOfArticle(int id, int maxNum);

	/**
	 * Get the average Analyzeduration of an Article over all analyze processes
	 * 
	 * @param id
	 *            Article ID
	 * @return
	 */
	JSONArray getAVGAnalyzeDurationofArticle(int id);

}
