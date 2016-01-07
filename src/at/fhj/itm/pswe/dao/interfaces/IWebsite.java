package at.fhj.itm.pswe.dao.interfaces;

import java.util.List;

import org.json.JSONArray;

import at.fhj.itm.pswe.model.Website;

public interface IWebsite {

	public Website createWebsite(String address, String description, int depth);

	public List<Website> findAllWebsites();

	public JSONArray findAllWebsitesJSON();

	public Website updateWebsite(Website ws);

	public void deleteWebsite(int id);

	public JSONArray findWordsOFSite(int id, int maxNum);

	public JSONArray timeLine4WordAndSite(int siteID, String word, String startDate, String endDate);

}
