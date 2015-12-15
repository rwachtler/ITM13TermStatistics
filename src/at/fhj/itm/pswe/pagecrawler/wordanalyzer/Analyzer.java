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

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import at.fhj.itm.pswe.model.Container;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.model.Word;

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



	public Analyzer(){}

	public void analyzeResults(String path) {
		readResultFile(path);
	}

	public void testPersist(){
		//TESTCODE
		Website ws = em.find(Website.class, 18);

		Word w=new Word();
		w.setActive(true);
		w.setText("hello");
		//em.persist(w);
		Container c= new Container();
		c.setAmount(10);
		c.setWord(w);
		c.setLogDate("01.10.2015");
		c.setWebsite(ws);
		em.persist(c);
		em.flush();
		//END
	}


	private void persistData(String url, String data, Website newWebsite, String currentDate){

		
		this.wordMap = this.calculateWordMap(data);
		Iterator<Map.Entry<String, Integer>> it = this.wordMap.entrySet().iterator();

		System.out.println("Start iterate over Wordmap");
		while (it.hasNext()) {

			// get key/value pair from hash map
			Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();

			String word = (String) pair.getKey();
			int count = (int) pair.getValue();

			Word wo = em.find(Word.class, word);
			System.out.println("WORD: "+word);
			
			// check if word exists in the database
			if (wo==null) {	
				System.out.println("WORD ISNULL");
				wo = new Word();
				wo.setActive(true);
				wo.setText(word);
				//em.persist(wo);
			}else{
				System.out.println("WORD NOT NULL");
			}

			//TODO refactoring -> DATE aus parameter benutzenn, wenn richtig formatiert
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
			em.persist(newCont);
			em.flush();

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
			word = word.replaceFirst("^[^a-zA-Z������]+", "").replaceAll("[^a-zA-Z������]+$", "").trim();

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

	public void readResultFile(String path) {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			// Read Start Page
			String line = br.readLine();
			String website = line;
			// Read Start Date
			line = br.readLine();
			String currentdate = line;
			
			//Get Current Root Domain only once, pass it further
			
			//Expect that domain only exists once
			//Expect only one result
			Query websiteExistsq = em.createQuery("SELECT w.id FROM Website w WHERE w.domain = :domain").setParameter("domain", website);
			List<Object> result = websiteExistsq.getResultList();
			int websiteId=-1;
			if(result.size()>0)
				websiteId = (int)result.get(0);
			Website newWebsite = em.find(Website.class, websiteId);

			// Start with analyzing data
			while (true) {
				String url = br.readLine();
				String data = br.readLine();
				if(data == null)
					break;
				else{
					try{					
						persistData(url, data, newWebsite, currentdate);
					}catch (Exception e){
						e.printStackTrace();
						break; //If error occurs
					}
				}
			}
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