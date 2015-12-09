package at.fhj.itm.pswe.pagecrawler.wordanalyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import at.fhj.itm.pswe.model.Container;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.model.Word;

@Stateless
public class Analyzer {
	
	private EntityManager em;

	private String input;
	private HashMap<String, Integer> wordMap;
	ReaderFilterWords rf = new ReaderFilterWords();
	private List<String> filterwords;

	private static String WEBSITE_NAME;
	private static String DATE;

	private static String RESULT_FILE;

	public Analyzer(){}
	
	public Analyzer(String path) {
		setRESULT_FILE(path);
	}
	
	public void analyzeResults() {
		readResultFile();
	}

	private void persistData(String url, String data) {
		
		int websiteId = -1;

		this.wordMap = this.calculateWordMap(data);

		String website = WEBSITE_NAME;

		Website newWebsite;
		
		// get website id
		if(em == null) {
			System.out.println("EntityManager is null");
		}
		Query websiteExistsq = em.createQuery("SELECT w.id FROM Website w WHERE w.domain = :domain").setParameter("domain", website);
		List<Object> result = websiteExistsq.getResultList();

		if(result.size()>0)
			websiteId = (int)result.get(0);
		newWebsite = em.find(Website.class, websiteId);

		Iterator it = this.wordMap.entrySet().iterator();

		System.out.println("Start iterate over Wordmap");
		while (it.hasNext()) {
			// get key/value pair from hash map
			Map.Entry pair = (Map.Entry) it.next();

			String word = (String) pair.getKey();
			int count = (int) pair.getValue();

			Word wo = em.find(Word.class, word);

			// check if word exists in the database
			if (wo==null) {					
				Word newWord = new Word();
				newWord.setActive(true);
				newWord.setText(word);
				em.persist(newWord);
				em.flush();
				wo = newWord;
			}

			// refactoring -> DATE, wenn richtig formatiert
			// format date string
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Date date = new Date();

			String dateString = dateFormat.format(date);

			// add container entry to database
			Container newCont = new Container();
			newCont.setAmount(count);
			newCont.setWord(wo);
			newCont.setLogDate(dateString);
			newCont.setWebsite(newWebsite);
			newCont.setUrl(url);
		}
		System.out.println("End iterating over Wordmap");
	}

	private HashMap<String, Integer> calculateWordMap(String input) {
		filterwords = rf.readWords();

		HashMap<String, Integer> wordmap = new HashMap<String, Integer>();
		String[] inputWords = input.split(" ");
		for (int i = 0; i < inputWords.length; i++) {
			String word = inputWords[i].toLowerCase();
			//System.out.println("Current word: " + word);

			// remove punctuation from start and end of word
			// according to:
			// http://stackoverflow.com/questions/12506655/how-can-i-remove-all-leading-and-trailing-punctuation
			word = word.replaceFirst("^[^a-zA-ZüÜöÖäÄ]+", "").replaceAll("[^a-zA-ZüÜöÖäÄ]+$", "").trim();

			if (!word.isEmpty()) {
				boolean isForbidden = false;
				for (String s : filterwords) {

					// System.out.println("Filter: "+s);
					if (s.contentEquals(word)) {
						// System.out.println("TRUE: "+s+" vs "+word);
						isForbidden = true;
					}

				}
				if (!isForbidden) {
					if (wordmap.containsKey(word)) {
						wordmap.put(word, wordmap.get(word) + 1);
					} else {
						wordmap.put(word, 1);
					}
				}
			}
		}

		return wordmap;
	}

	private void readResultFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(RESULT_FILE))) {
			// Read Start Page
			String line = br.readLine();
			WEBSITE_NAME = line;
			// Read Start Date
			line = br.readLine();
			DATE = line;

			// Start with analyzing data
			while (true) {
				String url = br.readLine();
				String data = br.readLine();
				if(data == null)
					break;
				else
					persistData(url, data);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getRESULT_FILE() {
		return RESULT_FILE;
	}

	public static void setRESULT_FILE(String rESULT_FILE) {
		RESULT_FILE = rESULT_FILE;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public HashMap<String, Integer> getWordMap() {
		return wordMap;
	}

	public void setWordMap(HashMap<String, Integer> wordMap) {
		this.wordMap = wordMap;
	}
}