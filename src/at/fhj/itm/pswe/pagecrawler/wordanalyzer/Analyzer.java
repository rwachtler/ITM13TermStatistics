package at.fhj.itm.pswe.pagecrawler.wordanalyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import at.fhj.itm.pswe.database.DbConnection;

public class Analyzer {

	private String input;
	private HashMap<String, Integer> wordMap;
	ReaderFilterWords rf=new ReaderFilterWords();
	private List<String> filterwords;

	private DbConnection db;

	private static String WEBSITE_NAME;

	private static String RESULT_FILE;

	public Analyzer(String path) {
		this.db = new DbConnection();
		setRESULT_FILE(path);

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(RESULT_FILE));
			WEBSITE_NAME = br.readLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void analyzeResults() {
		// Blockweise lesen (File)
		this.wordMap = this.calculateWordMap(this.readResultFile());

		String website = WEBSITE_NAME;

		// get website id
		int websiteId = db.websiteExists(website);

		Iterator it = this.wordMap.entrySet().iterator();
		
		System.out.println("Start iterate over Wordmap");
		while (it.hasNext()) {
			// get key/value pair from hash map
			Map.Entry pair = (Map.Entry) it.next();

			String word = (String) pair.getKey();
			int count = (int) pair.getValue();

			//Open connection
			Connection conn =null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				// nur einmal connection erstellen
				conn =  DriverManager.getConnection("jdbc:mysql://" + DbConnection.DB_HOST + "/" + DbConnection.DB_NAME + "?user=" + DbConnection.DB_USER + "&password=" + DbConnection.DB_PASSWORD);

				// check if word exists in the database
				if (!db.wordExists(word,conn)) {
					int active = 1;

					db.addWord(word, active,conn);
				}
				
				// format date string
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date date = new Date();

				String dateString = dateFormat.format(date);

				// add container entry to database
				db.addContainer(word, count, websiteId, dateString,conn);
				
				// nicht notwendig
				it.remove();

			}catch(Exception e){
				//Do nothing
				e.printStackTrace();
			}finally{
				try { if (conn != null) conn.close(); } catch (Exception e) {};
			}

		}
		System.out.println("End iterating over Wordmap");
	}

	public HashMap<String, Integer> calculateWordMap(String input) {
		filterwords=rf.readWords();

		HashMap<String, Integer> wordmap = new HashMap<String, Integer>();
		String[] inputWords = input.split(" ");
		for (int i = 0; i < inputWords.length; i++) {
			String word = inputWords[i].toLowerCase();
			System.out.println("Current word: "+word);

			// remove punctuation from start and end of word
			// according to:
			// http://stackoverflow.com/questions/12506655/how-can-i-remove-all-leading-and-trailing-punctuation
			// Umlaute zu beginn!
			word = word.replaceFirst("^[^a-zA-Z]+", "").replaceAll("[^a-zA-Z]+$", "").trim();

			if (!word.isEmpty()) {
				boolean isForbidden=false;
				for(String s : filterwords){

					//System.out.println("Filter: "+s);
					if(s.contentEquals(word)){
						//System.out.println("TRUE: "+s+" vs "+word);
						isForbidden=true;
					}

				}
				if(!isForbidden){
					if (wordmap.containsKey(word)) {
						wordmap.put(word, (Integer) wordmap.get(word) + 1);
					} else {
						wordmap.put(word, 1);
					}
				}
			}
		}

		return wordmap;
	}

	public String readResultFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(RESULT_FILE))) {
			StringBuilder sb = new StringBuilder();
			// Skip first 2 Lines (URL and date of creation)
			String line = br.readLine();
			System.out.println("First line: " + line);
			line = br.readLine();
			System.out.println("Second line: " + line);
			line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			
			System.out.println("Everything: " + line);

			return everything;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
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