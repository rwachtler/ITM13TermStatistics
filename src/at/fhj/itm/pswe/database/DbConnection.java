package at.fhj.itm.pswe.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
	
	private boolean dbConnected = false;
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	//private static String DB_HOST = "itm13db.ccvogifhormq.eu-west-1.rds.amazonaws.com:3306";
	private static String DB_NAME = "itm13";
	//private static String DB_USER = "itm13root";
	//private static String DB_PASSWORD = "iTm!3DbMySqL";
	
	private static String DB_HOST = "localhost";
	private static String DB_USER = "root";
	private static String DB_PASSWORD = "";
	
	public DbConnection() {
		dbConnected = init();
	}
	
	public boolean init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD);
			
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
	
	public boolean isConnected() {
		return this.dbConnected;
	}
	
	public int addWebsite(String website, String description, int active) {
		try {
			preparedStatement = connect.prepareStatement("INSERT INTO website (domain, description, active) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, website);
			preparedStatement.setString(2, description);
			preparedStatement.setInt(3, active);
			
			preparedStatement.executeUpdate();
			
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int addWord(String word, int active) {
		try {
			preparedStatement = connect.prepareStatement("INSERT INTO word (text, active) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, word);
			preparedStatement.setInt(2, active);
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int addContainer(String word, int count, int websiteId, String dateString) {
		try {
			preparedStatement = connect.prepareStatement("INSERT INTO container (amount, log_date, fk_website, fk_word) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, count);
			preparedStatement.setString(2, dateString);
			preparedStatement.setInt(3, websiteId);
			preparedStatement.setString(4, word);
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Checks if a website exists in the database.
	 * @param website  The website to search the database for
	 * @return  int id of the website if it exists, -1 if it does not exist
	 */
	public int websiteExists(String website) {
		try {
			preparedStatement = connect.prepareStatement("SELECT id FROM website WHERE domain = ?");
			preparedStatement.setString(1, website);
			
			ResultSet result = preparedStatement.executeQuery();
			
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Checks if a word exists in the database.
	 * @param word  The word to search the database for
	 * @return boolean true if word exists, false if it does not exist
	 */
	public boolean wordExists(String word) {
		try {
			preparedStatement = connect.prepareStatement("SELECT text FROM word WHERE text = ?");
			preparedStatement.setString(1, word);
			
			ResultSet result = preparedStatement.executeQuery();
			
			if (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
