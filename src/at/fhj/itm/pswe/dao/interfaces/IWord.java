package at.fhj.itm.pswe.dao.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IWord {

	public JSONArray wordAndAmount();
	public JSONArray sitesOfWord(String word);
	public void changeWordActive(String word, boolean active);
	public JSONObject findSingleWordWithAmount(String word);
	public JSONArray wordCountOverPeriod(String word, String startdate, String enddate);
}
