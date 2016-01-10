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
import java.util.concurrent.TimeUnit;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.ejb3.annotation.TransactionTimeout;

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

	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

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
	}

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

			Word wo = em.find(Word.class, word);
			// System.out.println("WORD: " + word);

			// check if word exists in the database
			if (wo == null) {

				wo = new Word();
				wo.setActive(true);
				wo.setText(word);

				// look up word type in the wordlist
				String wordtype;

				Query q = em.createQuery("SELECT wl FROM WordlistEntry wl WHERE wl.word = :word").setParameter("word",
						word);

				List<WordlistEntry> queryResults = q.getResultList();

				if (queryResults.size() == 0) {
					// word not found in wordlist --> we set it to unknown
					wordtype = "unknown";
				} else {
					// we use the wordtype found in the wordlist
					wordtype = queryResults.get(0).getWordtype();
				}

				// look up the word type in our own wordtype table
				Wordtype wt;

				Query wordTypeQuery = em.createQuery("SELECT wt FROM Wordtype wt WHERE wt.texttype = :wordtype")
						.setParameter("wordtype", wordtype);

				List<Wordtype> wordTypeQueryResults = wordTypeQuery.getResultList();

				if (wordTypeQueryResults.size() == 0) {
					// wordtype not found --> we add it
					wt = new Wordtype();
					wt.setTexttype(wordtype);

					em.persist(wt);
				} else {
					// wordtype found --> we can use it
					wt = wordTypeQueryResults.get(0);
				}

				wo.setWordtype(wt);

				// Need to persist so that it is available for newCont later on
				em.persist(wo);
			}

			// Get Article, if already in Database
			Query q = em.createQuery("SELECT a.id, a.url FROM Article a WHERE a.url = :url").setParameter("url", url);

			List<Object[]> queryResults = q.getResultList();

			if (queryResults.size() == 0) {
				// if article not in database, persist it
				System.out.println("Add Article: " + url);
				ar = new Article();
				ar.setUrl(url);
				em.persist(ar);
			} else {
				// else get the one from Database
				ar = em.find(Article.class, queryResults.get(0)[0]);
			}

			// TODO refactoring -> DATE aus parameter benutzenn, wenn richtig
			// formatiert
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
			newCont.setArticle(ar);
			em.persist(newCont);
		}

		// Output of time from analyzing
		System.out.println("Time of Analyzing Article:");
		System.out.println(System.currentTimeMillis() - articleStartTime);
		// Persist articleStat
		ArticleStat articleStat = new ArticleStat();
		articleStat.setAnalyzeDuration(System.currentTimeMillis() - articleStartTime);
		articleStat.setArticle(ar);
		articleStat.setLogDate(DATE);
		em.persist(articleStat);

		System.out.println("End iterating over Wordmap");
	}

	public HashMap<String, Integer> calculateWordMap(String input) {
		filterwords = rf.readWords();

		HashMap<String, Integer> wordmap = new HashMap<String, Integer>();
		String[] inputWords = input.split(" ");
		for (int i = 0; i < inputWords.length; i++) {
			String word = inputWords[i].toLowerCase();
			// System.out.println("Current word: " + word);

			// remove punctuation from start and end of word
			// according to:
			// http://stackoverflow.com/questions/12506655/how-can-i-remove-all-leading-and-trailing-punctuation
			word = word.replaceFirst("^[^a-zA-Z������]+", "").replaceAll("[^a-zA-Z������]+$", "").trim();

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
			// Expect only one result
			Query websiteExistsq = em.createQuery("SELECT w.id FROM Website w WHERE w.domain = :domain")
					.setParameter("domain", website);
			List<Object> result = websiteExistsq.getResultList();
			int websiteId = -1;
			if (result.size() > 0)
				websiteId = (int) result.get(0);
			Website newWebsite = em.find(Website.class, websiteId);

			int count = 0;

			// Get Timestamp before starting analyzing
			long analyzerStartTime = System.currentTimeMillis();

			// Start with analyzing data
			while (true) {
				String url = br.readLine();
				System.out.println("URL: " + url + "|");
				String data = br.readLine();
				count++;
				if (data == null) {
					crawlerDuration = url;
					break;
				} else {
					try {
						persistData(url, data, newWebsite, currentdate);
						// flush to db every 20 words
						if (count > 20) {
							count = 0;
							em.flush();
							em.clear();
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
			websiteStat.setWebsite(newWebsite);
			em.persist(websiteStat);

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