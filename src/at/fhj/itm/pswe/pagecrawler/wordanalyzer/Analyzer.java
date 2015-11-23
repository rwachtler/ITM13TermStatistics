package at.fhj.itm.pswe.pagecrawler.wordanalyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import at.fhj.itm.pswe.database.DbConnection;

public class Analyzer {

	private String input;
	private HashMap<String, Integer> wordMap;
	
	private DbConnection db;
	
	private static String WEBSITE_NAME = "http://pswengi.bamb.at";
	
	private static String RESULT_FILE = "./result/crawl/result.txt";

	public Analyzer() {
		// init database connection
		this.db = new DbConnection();
	}
	
	public void analyzeResults() {
		if (db.isConnected()) {
			this.wordMap = this.calculateWordMap(this.readResultFile());
			
			// TODO remove hard-coded references
			String website = WEBSITE_NAME;
			String description = "";
			
			// get website id
			int websiteId = db.websiteExists(website);
			
			// if website does not exist, add it
			if (websiteId < 0) {
				int active = 1;
				
				websiteId = db.addWebsite(website, description, active);
			}
			
			Iterator it = this.wordMap.entrySet().iterator();
			
			while(it.hasNext()) {
				// get key/value pair from hash map
				Map.Entry pair = (Map.Entry)it.next();
				
				String word = (String)pair.getKey();
				int count = (int)pair.getValue();
				
				// check if word exists in the database
				if (!db.wordExists(word)) {
					int active = 1;
					
					db.addWord(word, active);
				}
				
				// format date string
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date date = new Date();
				
				String dateString = dateFormat.format(date);
				
				// add container entry to database
				db.addContainer(word, count, websiteId, dateString);
				
				it.remove();
			}
		}
	}
	
	public HashMap<String,Integer> calculateWordMap(String input) {
		HashMap<String,Integer> wordmap = new HashMap<String,Integer>();
		String[] inputWords = input.split(" ");
		for(int i = 0; i<inputWords.length;i++){
			String word = inputWords[i].toLowerCase();
			
			// remove punctuation from start and end of word
			// according to: http://stackoverflow.com/questions/12506655/how-can-i-remove-all-leading-and-trailing-punctuation
			word = word.replaceFirst("^[^a-zA-Z]+", "").replaceAll("[^a-zA-Z]+$", "").trim();
			
			if (!word.isEmpty()) {
				if (wordmap.containsKey(word)) {
					wordmap.put(word,(Integer) wordmap.get(word)+1);
				} else {
					wordmap.put(word, 1);
				}
			}
		}

		return wordmap;
	}
	
	public String readResultFile() {		
		try(BufferedReader br = new BufferedReader(new FileReader(RESULT_FILE))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    
		    return everything;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
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