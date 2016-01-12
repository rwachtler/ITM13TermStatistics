package at.fhj.itm.pswe.dao.interfaces;

import org.json.JSONArray;

import at.fhj.itm.pswe.model.Article;

public interface IArticle {

	/**
	 * Create new Article Object in DB
	 * 
	 * @param url
	 *            URL of the Article
	 * @return persisted Article
	 */
	Article createArticle(String url);

	/**
	 * Get the persisted Article Object from the DB
	 * 
	 * @param url
	 *            URL of the Article
	 * @return Complete Article Object
	 */
	Article readArticle(String url);

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
	 * Find all Words of one Article by its id
	 * 
	 * @param id
	 *            id of the article
	 * @param maxNum
	 *            Maximum amount of data
	 */
	JSONArray findWordsOfArticle(int id, int maxNum);

}
