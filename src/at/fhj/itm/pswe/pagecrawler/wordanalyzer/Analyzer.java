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

public class Analyzer {

	private String input;
	private HashMap<String, Integer> wordMap;
	
	private boolean dbConnected = false;
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	private static String WEBSITE_NAME = "http://pswengi.bamb.at";

	public Analyzer() {
		if (!this.initDbConnection()) {
			System.out.println("Could not create database connection!");
			return;
		}
	}
	
	public void analyzeResults() {
		if (this.dbConnected) {
			this.wordMap = this.calculateWordMap(this.readResultFile());
			
			// get website id
			int websiteId = 0;
			
			try {
				preparedStatement = connect.prepareStatement("INSERT INTO website (domain, description, active) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, WEBSITE_NAME);
				preparedStatement.setString(2, "blabla");
				preparedStatement.setInt(3, 1);
				
				preparedStatement.executeUpdate();
				
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				
				if (generatedKeys.next()) {
					websiteId = generatedKeys.getInt(1);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Iterator it = this.wordMap.entrySet().iterator();
			
			while(it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				
				String word = (String)pair.getKey();
				int count = (int)pair.getValue();
				
				// add word
				try {
					preparedStatement = connect.prepareStatement("INSERT INTO word (text, active) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, word);
					preparedStatement.setInt(2, 1);
					
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// add container entry
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date date = new Date();
				
				String dateString = dateFormat.format(date);
				
				try {
					preparedStatement = connect.prepareStatement("INSERT INTO container (amount, log_date, fk_website, fk_word) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setInt(1, count);
					preparedStatement.setString(2, dateString);
					preparedStatement.setInt(3, websiteId);
					preparedStatement.setString(4, word);
					
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				it.remove();
			}
		}
	}
	
	private boolean initDbConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager.getConnection("jdbc:mysql://itm13db.ccvogifhormq.eu-west-1.rds.amazonaws.com:3306/itm13?user=itm13root&password=iTm!3DbMySqL");
			
			this.dbConnected = true;
			
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void writeMapToDatabase() {
		
	}

	public HashMap<String,Integer> calculateWordMap(String input) {
		HashMap<String,Integer> wordmap = new HashMap<String,Integer>();
		String[] inputWords = input.split(" ");
		for(int i = 0; i<inputWords.length;i++){
			String word = inputWords[i].toLowerCase();
			
			if(wordmap.containsKey(word)){
				wordmap.put(word,(Integer) wordmap.get(word)+1);
			}else{
				wordmap.put(word, 1);
			}
		}

		return wordmap;
	}
	
	public String readResultFile() {		
		try(BufferedReader br = new BufferedReader(new FileReader("result/crawl/result.txt"))) {
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
