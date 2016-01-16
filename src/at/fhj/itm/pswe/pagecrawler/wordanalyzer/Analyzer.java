package at.fhj.itm.pswe.pagecrawler.wordanalyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.ejb3.annotation.TransactionTimeout;

import at.fhj.itm.pswe.dao.AnalyzerDao;
import at.fhj.itm.pswe.dao.WebsiteDao;
import at.fhj.itm.pswe.model.Article;
import at.fhj.itm.pswe.model.ArticleStat;
import at.fhj.itm.pswe.model.Container;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.model.WebsiteStat;
import at.fhj.itm.pswe.model.Word;
import at.fhj.itm.pswe.model.WordlistEntry;
import at.fhj.itm.pswe.model.Wordtype;

@Stateless
@LocalBean
public class Analyzer {

	/*@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;*/
	
	private AnalyzerDao analyzerDAO;
	
	@Inject
	public void setAnalyzerDAO(AnalyzerDao analyzerDAO) {
		this.analyzerDAO = analyzerDAO;
	}

	private String input;
	private HashMap<String, Integer> wordMap;
	ReaderFilterWords rf = new ReaderFilterWords();
	private List<String> filterwords;

	private static String DATE;

	public Analyzer() {
	}

	public void analyzeResults(String path) {
		readResultFile(path);
	}
/* Temporary Testcode?
	public void testPersist() {
		// TESTCODE
		Website ws = em.find(Website.class, 18);

		Word w = new Word();
		w.setActive(true);
		w.setText("hello");
		// em.persist(w);
		Container c = new Container();
		c.setAmount(10);
		c.setWord(w);
		c.setLogDate("01.10.2015");
		c.setWebsite(ws);
		em.persist(c);
		em.flush();
		// END
	}*/

	private void persistData(String url, String data, Website newWebsite, String currentDate) {

		// Timestamp before starting analyzing article
		long articleStartTime = System.currentTimeMillis();

		this.wordMap = this.calculateWordMap(data);
		Iterator<Map.Entry<String, Integer>> it = this.wordMap.entrySet().iterator();
		
		Article ar = null;

		System.out.println("Start iterate over Wordmap");
		while (it.hasNext()) {
			// get key/value pair from hash map
 			Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();

			String word = (String) pair.getKey();
			int count = (int) pair.getValue();

			Word wo = analyzerDAO.findWord(word);
			
			long findTypeofWordTimebefore = 0;
			long findTypeofWordTimeafter = 0;
			
			long setTypeForWordTimebefore = 0;
			long setTypeForWordTimeafter = 0;
			
			System.out.println("WORD: " + word + " | COUNT: " + count);

			// check if word exists in the database
			if (wo == null) {

				wo = new Word();
				wo.setActive(true);
				wo.setText(word);

				findTypeofWordTimebefore = System.currentTimeMillis();
				// look up word type in the wordlist
				String wordtype = analyzerDAO.findTypeForWord(word);
				
				findTypeofWordTimeafter = System.currentTimeMillis();
				
				setTypeForWordTimebefore = System.currentTimeMillis();
				//Set Type for word and persist both
				analyzerDAO.setTypeForWord(wo, wordtype);
				setTypeForWordTimeafter = System.currentTimeMillis();
				
				
			}

			// Get Article, if already in Database
			long findArticleTimebefore = System.currentTimeMillis();
			Article tmp = new Article();
			tmp.setUrl(url);
			ar = analyzerDAO.findArticle(tmp);
			long findArticleTimeafter = System.currentTimeMillis();
			// add container entry to database
			Container newCont = new Container();
			newCont.setAmount(count);
			newCont.setWord(wo);
			newCont.setLogDate(DATE);
			newCont.setWebsite(newWebsite);
			newCont.setArticle(ar);
			long saveContainerTimebefore = System.currentTimeMillis();
			analyzerDAO.saveContainer(newCont);
			long saveContainerTimeafter = System.currentTimeMillis();
			
			long saveContainerTime = System.currentTimeMillis();
			
			System.out.println("findWordTime: " + (findTypeofWordTimeafter - findTypeofWordTimebefore));
			System.out.println("setTypeForWordTime: " + (setTypeForWordTimeafter - setTypeForWordTimebefore));
			System.out.println("findArticleTime: " + (findArticleTimeafter - findArticleTimebefore));
			System.out.println("saveContainerTime: " + (saveContainerTimeafter - saveContainerTimebefore));
			System.out.println();
		}

		// Output of time from analyzing
		System.out.println("Time of Analyzing Article:");
		System.out.println(System.currentTimeMillis() - articleStartTime);
		// Persist articleStat
		ArticleStat articleStat = new ArticleStat();
		articleStat.setAnalyzeDuration(System.currentTimeMillis() - articleStartTime);
		articleStat.setArticle(ar);
		articleStat.setLogDate(DATE);
		analyzerDAO.saveArticleStat(articleStat);

		System.out.println("End iterating over Wordmap");
	}

	public HashMap<String, Integer> calculateWordMap(String input) {
		filterwords = rf.readWords();

		HashMap<String, Integer> wordmap = new HashMap<String, Integer>();
		String[] inputWords = input.split(" ");
		for (int i = 0; i < inputWords.length; i++) {
			String word = inputWords[i].trim();
			// System.out.println("Current word: " + word);

			// remove punctuation from start and end of word
			// according to:
			// http://stackoverflow.com/questions/12506655/how-can-i-remove-all-leading-and-trailing-punctuation
			word = word.replaceFirst("^[^a-zA-ZÖöÄäÜü]+", "").replaceAll("[^a-zA-ZÖöÄäÜü]+$", "").trim();

			if (!word.isEmpty()) {
				boolean isForbidden = false;

				if (filterwords.contains(word)) {
					isForbidden = true;
				}

				/*
				 * old solution for (String s : filterwords) {
				 * 
				 * // System.out.println("Filter: "+s); if
				 * (s.contentEquals(word)) { // System.out.println("TRUE: "+s+
				 * " vs "+word); isForbidden = true; }
				 * 
				 * }
				 */

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

	@TransactionTimeout(3600)
	public void readResultFile(String path) {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String crawlerDuration = "";
			// Read Start Page
			String line = br.readLine();
			String website = line;
			// Read Start Date
			line = br.readLine();
			String currentdate = line;
			DATE = currentdate;

			// Get Current Root Domain only once, pass it further

			// Expect that domain only exists once
			Website webSite = analyzerDAO.findWebsite(website);
			
			// Update Last Website Crawldate
			//analyzerDAO.updateCrawlDateofWebsite(website);
			
			int count = 0;

			// Get Timestamp before starting analyzing
			long analyzerStartTime = System.currentTimeMillis();

			// Start with analyzing data
			while (true) {
				String url = br.readLine();
				System.out.println("URL: " + url + "|");
				String data = br.readLine();
				System.out.println("Data: " + data);
				count++;
				if (data == null) {
					crawlerDuration = url;
					break;
				} else {
					try {
						persistData(url, data, webSite, currentdate);
						// flush to db every 20 words
						if (count > 20) {
							count = 0;
							analyzerDAO.flushDAO();
						}
					} catch (Exception e) {
						e.printStackTrace();
						break; // If error occurs
					}
				}
			}
			// Output of time from analyzing
			System.out.println("Time of Analyzing:");
			System.out.println(System.currentTimeMillis() - analyzerStartTime);
			// Persist websiteStat
			WebsiteStat websiteStat = new WebsiteStat();
			websiteStat.setAnalyzeDuration(System.currentTimeMillis() - analyzerStartTime);
			// Convert Time into millis
			Long durationMillis = 0L;
			for (String s : crawlerDuration.split("[:]")) {
				durationMillis += TimeUnit.SECONDS.toMillis(Long.valueOf(s));
			}
			websiteStat.setCrawlDuration(durationMillis);
			websiteStat.setLogDate(DATE);
			websiteStat.setWebsite(webSite);
			analyzerDAO.saveWebsiteStat(websiteStat);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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