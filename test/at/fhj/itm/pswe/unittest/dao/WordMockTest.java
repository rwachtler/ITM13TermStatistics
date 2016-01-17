package at.fhj.itm.pswe.unittest.dao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import at.fhj.itm.pswe.dao.WordDao;
import at.fhj.itm.pswe.model.Word;
import at.fhj.itm.pswe.model.Wordtype;

public class WordMockTest {

	/**
	 * Tests wordAndAmount Method
	 */
	@Test
	public void testWordAndAmount() {
		// Setup
		String text = "test";
		boolean active = true;
		long sumAmount = 10;
		long wTypeID = 0;
		String wTypeText = "unknown";

		JSONObject word = new JSONObject();
		word.put("word", text);
		word.put("amount", sumAmount);
		word.put("active", active);
		word.put("wType", wTypeID);

		JSONObject wordType = new JSONObject();
		wordType.put("name", wTypeText);

		JSONObject validatorObject = new JSONObject();
		validatorObject.put("word", word);
		validatorObject.put("wTypes", wordType);
		JSONArray validator = new JSONArray();
		validator.put(validatorObject);

		// QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr = new Object[5];
		retArr[0] = text;
		retArr[1] = active;
		retArr[2] = sumAmount;
		retArr[3] = wTypeID;
		retArr[4] = wTypeText;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);

		// EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT w.text, w.active, sum(c.amount), w.wordtype.id, w.wordtype.texttype  FROM Container c JOIN c.word w  GROUP BY w.text, w.active"))
						.andReturn(mockedQuery);
		replay(mockEm);
		WordDao wDAO = new WordDao();
		wDAO.setEntityManager(mockEm);

		// Test
		JSONArray testResult = wDAO.wordAndAmount();

		// Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(), testResult.toString());

	}

	/**
	 * Test call to entitymanager in changeWord em returns valid word
	 */
	@Test
	public void testChangeWord() {

		// Setup
		Wordtype wt = new Wordtype();
		wt.setId(1);
		wt.setTexttype("unkown");
		
		Word w = new Word();
		w.setText("test");
		w.setActive(false);
		w.setWordtype(wt);

		WordDao wDAO = new WordDao();

		EntityManager mockEm = createMock(EntityManager.class);
		// Expect one single call to find
		expect(mockEm.find(Word.class, "test")).andReturn(w);
		expect(mockEm.find(Wordtype.class, 1)).andReturn(wt);
		mockEm.flush();
		// set to replay state
		replay(mockEm);
		// Set Mock Object
		wDAO.setEntityManager(mockEm);

		// TEST
		wDAO.updateWord(w);

		// Verify
		verify(mockEm);
	}

	// This test should fail at this point (no nullpointer handling is
	// implemented)
	/**
	 * Test call to entitymanager in changeWord em returns null (no such word
	 * found
	 */
	@Test
	public void testChangeWordNoWordFound() {

		// Setup
		// Setup
		Wordtype wt = new Wordtype();
		wt.setId(1);
		wt.setTexttype("unkown");
		
		Word w = new Word();
		w.setText("test");
		w.setActive(false);
		w.setWordtype(wt);

		WordDao wDAO = new WordDao();

		EntityManager mockEm = createMock(EntityManager.class);
		// Expect one single call to find
		expect(mockEm.find(Word.class, "test")).andReturn(null);
		expect(mockEm.find(Wordtype.class, 1)).andReturn(wt);
		// set to replay state
		replay(mockEm);
		// Set Mock Object
		wDAO.setEntityManager(mockEm);

		// TEST
		wDAO.updateWord(w);

		// Verify
		verify(mockEm);
	}

	// Helper method for mocking queries
	private Query mockQuery(String param, String replace, List<Object[]> results) {
		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter(param, replace)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(results);
		return mockedQuery;

	}

	/**
	 * Test the findSingleWordMethod With valid input
	 * 
	 */
	@Test
	public void testFindSingleWord() {

		// Setup
		WordDao wDAO = new WordDao();
		String testWord = "test";
		boolean testActive = true;
		int testNum = 10;
		int wtID=1;
		String wtType="unknown";
		List<Object[]> returnList = new ArrayList<Object[]>();

		Object[] retArr = new Object[5];
		retArr[0] = testWord;
		retArr[1] = testActive;
		retArr[2] = testNum;
		retArr[3] = wtID;
		retArr[4] = wtType;

		returnList.add(retArr);
		// Compare to
		JSONObject word = new JSONObject();
		word.put("amount", testNum);
		word.put("word", testWord);
		word.put("active", testActive);
		word.put("wType", wtID);
		
		JSONObject wt= new JSONObject();
		wt.put("name", wtType);
	
		JSONObject validator= new JSONObject();
		validator.put("wTypes", wt);
		validator.put("word", word);

		// Init Mock For Query
		Query qMock = mockQuery("word", testWord, returnList);
		replay(qMock);

		// Init Mock for em
		EntityManager mockEm = createMock(EntityManager.class);
		// Expect one single call to find
		expect(mockEm.createQuery(
				"SELECT w.text, w.active, sum(c.amount), w.wordtype.id, w.wordtype.texttype  FROM Container c JOIN c.word w WHERE w.text = :word  GROUP BY w.text, w.active"))
						.andReturn(qMock);
		// set to replay state
		replay(mockEm);
		// Set Mock Object
		wDAO.setEntityManager(mockEm);

		// run test
		JSONObject testResult = wDAO.findSingleWordWithAmount(testWord);

		// Validate
		verify(mockEm);
		Assert.assertEquals(validator.toString(), testResult.toString());

	}

	/**
	 * Test the findSingleWordMethodNoValid With no valid input
	 * 
	 */

	@Test
	public void testFindSingleWordNoValid() {

		// Setup
		WordDao wDAO = new WordDao();
		String testWord = "test";
		List<Object[]> returnList = new ArrayList<Object[]>();

		// Compare to empty object
		JSONObject validator = new JSONObject();

		// Init Mock For Query
		Query qMock = mockQuery("word", testWord, returnList);
		replay(qMock);

		// Init Mock for em
		EntityManager mockEm = createMock(EntityManager.class);
		// Expect one single call to find
		expect(mockEm.createQuery(
				"SELECT w.text, w.active, sum(c.amount), w.wordtype.id, w.wordtype.texttype  FROM Container c JOIN c.word w WHERE w.text = :word  GROUP BY w.text, w.active"))
						.andReturn(qMock);
		// set to replay state
		replay(mockEm);
		// Set Mock Object
		wDAO.setEntityManager(mockEm);

		// run test
		JSONObject testResult = wDAO.findSingleWordWithAmount(testWord);

		// Validate
		verify(mockEm);
		Assert.assertEquals(validator.toString(), testResult.toString());
	}

	/**
	 * Test Sitesofword method with valid input
	 */
	@Test
	public void testsitesOfWord() {
		// Setup
		String text = "test";
		long id = 15;
		String adresse = "test.de";
		long sumAmount = 10;

		JSONObject validatorObject = new JSONObject();
		validatorObject.put("amount", sumAmount);
		validatorObject.put("id", id);
		validatorObject.put("adresse", adresse);
		JSONArray validator = new JSONArray();
		validator.put(validatorObject);

		// QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr = new Object[3];
		retArr[0] = id;
		retArr[1] = adresse;
		retArr[2] = sumAmount;
		returnList.add(retArr);

		Query mockedQuery = mockQuery("word", text, returnList);
		replay(mockedQuery);
		// EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery("SELECT c.website.id, c.website.domain, sum(c.amount) "
				+ "FROM Container c WHERE c.word.text LIKE :word AND c.word.active = TRUE "
				+ "GROUP BY c.website ORDER BY sum(c.amount) DESC")).andReturn(mockedQuery);
		replay(mockEm);
		WordDao wDAO = new WordDao();
		wDAO.setEntityManager(mockEm);

		// Test
		JSONArray testResult = wDAO.sitesOfWord(text);

		// Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(), testResult.toString());
	}

	@Test
	public void testwordCountOverPeriod() {
		// Setup
		String word = "test";
		String startdate = "2015-05-05";
		String enddate = "2015-05-06";

		String date = "2015-05-05";
		int amount = 5;

		JSONObject validatorObject = new JSONObject();
		validatorObject.put("date", date);
		validatorObject.put("amount", amount);
		JSONArray validator = new JSONArray();
		validator.put(validatorObject);

		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr = new Object[2];
		retArr[0] = date;
		retArr[1] = amount;
		returnList.add(retArr);

		// QueryMock
		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("word", word)).andReturn(mockedQuery);
		expect(mockedQuery.setParameter("startdate", startdate)).andReturn(mockedQuery);
		expect(mockedQuery.setParameter("enddate", enddate)).andReturn(mockedQuery);

		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);

		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery("SELECT co.logDate, SUM(co.amount) FROM Container co "
				+ "WHERE co.word.text = :word AND (co.logDate BETWEEN :startdate AND :enddate) "
				+ "GROUP BY co.logDate")).andReturn(mockedQuery);
		replay(mockEm);

		WordDao wDAO = new WordDao();
		wDAO.setEntityManager(mockEm);

		// Test
		JSONArray testResult = wDAO.wordCountOverPeriod(word, startdate, enddate);

		// Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(), testResult.toString());
	}

	@Test
	public void testwordTypeAsOption() {
		// Setup
		String label = "unknown";
		int value = 1;

		JSONObject validatorObject = new JSONObject();
		validatorObject.put("value", value);
		validatorObject.put("label", label);
		JSONArray validator = new JSONArray();
		validator.put(validatorObject);

		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr = new Object[2];
		retArr[0] = value;
		retArr[1] = label;
		returnList.add(retArr);

		// QueryMock
		Query mockedQuery = createMock(Query.class);

		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);

		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery("SELECT w.id, w.texttype FROM Wordtype w"))
				.andReturn(mockedQuery);
		replay(mockEm);

		WordDao wDAO = new WordDao();
		wDAO.setEntityManager(mockEm);

		// Test
		JSONArray testResult = wDAO.wordTypeAsOption();

		// Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(), testResult.toString());
	}

}
