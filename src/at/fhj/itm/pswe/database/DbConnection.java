package at.fhj.itm.pswe.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbConnection {

	public static String DB_NAME = "itm13";

	public static String DB_HOST = "itm13db.ccvogifhormq.eu-west-1.rds.amazonaws.com:3306";
	public static String DB_USER = "itm13root";
	public static String DB_PASSWORD = "iTm!3DbMySqL";

	public DbConnection() {

	}


	
	public int addWord(String word, int active, Connection conn) {
		System.out.println("Adding new word");
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
				stmt = conn.prepareStatement("INSERT INTO word (text, active) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, word);
			stmt.setInt(2, active);
			stmt.executeUpdate();

			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch(Exception e) {
			// Error Handling
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {};
			try { if (stmt != null) stmt.close(); } catch (Exception e) {};
		}

		return -1;
	}

	public int addContainer(String word, int count, int websiteId, String dateString, Connection conn) {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
				stmt = conn.prepareStatement("INSERT INTO container (amount, log_date, fk_website, fk_word) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, count);
			stmt.setString(2, dateString);
			stmt.setInt(3, websiteId);
			stmt.setString(4, word);

			return stmt.executeUpdate();
		} catch(Exception e) {
			// Error Handling
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {};
			try { if (stmt != null) stmt.close(); } catch (Exception e) {};
		}

		return -1;
	}

	/**
	 * Checks if a website exists in the database.
	 * @param website  The website to search the database for
	 * @return  int id of the website if it exists, -1 if it does not exist
	 */
	public int websiteExists(String website) {
		Connection conn =null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn =  DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD);
			stmt = conn.prepareStatement("SELECT id FROM website WHERE domain = ?");
			stmt.setString(1, website);

			rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch(Exception e) {
			// Error Handling
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {};
			try { if (stmt != null) stmt.close(); } catch (Exception e) {};
			try { if (conn != null) conn.close(); } catch (Exception e) {};
		}

		return -1;
	}

	/**
	 * Checks if a word exists in the database.
	 * @param word  The word to search the database for
	 * @return boolean true if word exists, false if it does not exist
	 */
	public boolean wordExists(String word,Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT text FROM word WHERE text = ?");
			stmt.setString(1, word);

			rs = stmt.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch(Exception e) {
			// Error Handling
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {};
			try { if (stmt != null) stmt.close(); } catch (Exception e) {};
		}

		return false;
	}

}
