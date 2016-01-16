package at.fhj.itm.pswe.unittest.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert; 
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import at.fhj.itm.pswe.dao.WordDao;
import at.fhj.itm.pswe.model.Word;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class WordMockTest {





	/**
	 * Tests wordAndAmount Method
	 */
	@Test
	public void testWordAndAmount(){
		//Setup
		String text="test";
		boolean active=true;
		long sumAmount=10;
		long wTypeID=0;
		String wTypeText="unknown";

		JSONObject word = new JSONObject();
		word.put("word", text);
		word.put("amount",sumAmount);
		word.put("active", active);
		word.put("wType", wTypeID);
		
		JSONObject wordType = new JSONObject();
		wordType.put("name", wTypeText);
		
		JSONObject validatorObject = new JSONObject();
		validatorObject.put("word", word);
		validatorObject.put("wTypes", wordType);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);


		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[5];
		retArr[0]=text;
		retArr[1]=active;
		retArr[2]=sumAmount;
		retArr[3]=wTypeID;
		retArr[4]=wTypeText;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);

		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT w.text, w.active, sum(c.amount), w.wordType.id, w.wordType.texttype  FROM Container c JOIN c.word w  GROUP BY w.text, w.active")
				).andReturn(mockedQuery);
		replay(mockEm);
		WordDao wDAO= new WordDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.wordAndAmount();

		//Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(),testResult.toString());

	}


	/**
	 * Test call to entitymanager in changeWord
	 * em returns valid word
	 */
	@Test
	public void testChangeWord(){

		//Setup
		Word w= new Word();
		w.setText("test");
		w.setActive(false);

		WordDao wDAO= new WordDao();

		EntityManager mockEm = createMock(EntityManager.class);
		//Expect one single call to find
		expect(mockEm.find(Word.class, "test")).andReturn(w);
		mockEm.flush();
		EasyMock.expectLastCall();
		//set to replay state
		replay(mockEm);
		//Set Mock Object
		wDAO.setEntityManager(mockEm);

		//TEST
		wDAO.changeWordActive("test",true);

		//Verify
		verify(mockEm);
	}

	//This test should fail at this point (no nullpointer handling is implemented)
	/**
	 * Test call to entitymanager in changeWord
	 * em returns null (no such word found
	 */
	@Test
	public void testChangeWordNoWordFound(){

		//Setup
		Word w= new Word();
		w.setText("test");
		w.setActive(false);

		WordDao wDAO= new WordDao();

		EntityManager mockEm = createMock(EntityManager.class);
		//Expect one single call to find
		expect(mockEm.find(Word.class, "test")).andReturn(null);
		//set to replay state
		replay(mockEm);
		//Set Mock Object
		wDAO.setEntityManager(mockEm);

		//TEST
		wDAO.changeWordActive("test",true);

		//Verify
		verify(mockEm);
	}

	//Helper method for mocking queries
	private Query mockQuery(String param, String replace,  List<Object[]> results) {
		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter(param, replace)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(results);
		return mockedQuery;



	}

	/**
	 * Test the findSingleWordMethod
	 * With valid input
	 * 
	 */
	@Test
	public void testFindSingleWord(){

		//Setup
		WordDao wDAO= new WordDao();
		String testWord= "test";
		boolean testActive=true;
		int testNum=10;
		List<Object[]> returnList = new ArrayList<Object[]>();

		Object[] retArr= new Object[3];
		retArr[0]=testWord;
		retArr[1]=testActive;
		retArr[2]=testNum;

		returnList.add(retArr);
		//Compare to
		JSONObject validator= new JSONObject();
		validator.put("amount", testNum);
		validator.put("word", testWord);
		validator.put("active", testActive);


		//Init Mock For Query
		Query qMock=mockQuery("word", testWord, returnList);
		replay(qMock);

		//Init Mock for em
		EntityManager mockEm = createMock(EntityManager.class);
		//Expect one single call to find
		expect(mockEm.createQuery(
				"SELECT w.text, w.active, sum(c.amount)  FROM Container c JOIN c.word w WHERE w.text = :word  GROUP BY w.text, w.active")
				).andReturn(qMock);
		//set to replay state
		replay(mockEm);
		//Set Mock Object
		wDAO.setEntityManager(mockEm);

		//run test
		JSONObject testResult= wDAO.findSingleWordWithAmount(testWord);

		//Validate
		verify(mockEm);
		Assert.assertEquals(validator.toString(),testResult.toString());


	}

	/**
	 * Test the findSingleWordMethodNoValid
	 * With no valid input
	 * 
	 */

	@Test
	public void testFindSingleWordNoValid(){

		//Setup
		WordDao wDAO= new WordDao();
		String testWord= "test";
		List<Object[]> returnList = new ArrayList<Object[]>();


		//Compare to empty object
		JSONObject validator= new JSONObject();


		//Init Mock For Query
		Query qMock=mockQuery("word", testWord, returnList);
		replay(qMock);

		//Init Mock for em
		EntityManager mockEm = createMock(EntityManager.class);
		//Expect one single call to find
		expect(mockEm.createQuery(
				"SELECT w.text, w.active, sum(c.amount)  FROM Container c JOIN c.word w WHERE w.text = :word  GROUP BY w.text, w.active")
				).andReturn(qMock);
		//set to replay state
		replay(mockEm);
		//Set Mock Object
		wDAO.setEntityManager(mockEm);

		//run test
		JSONObject testResult= wDAO.findSingleWordWithAmount(testWord);

		//Validate
		verify(mockEm);
		Assert.assertEquals(validator.toString(),testResult.toString());
	}

	/**
	 * Test Sitesofword method with valid input
	 */
	@Test
	public void testsitesOfWord(){
		//Setup
		String text="test";
		long id=15;
		String adresse="test.de";
		long sumAmount=10;

		JSONObject validatorObject= new JSONObject();
		validatorObject.put("amount", sumAmount);
		validatorObject.put("id", id);
		validatorObject.put("adresse", adresse);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);


		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[3];
		retArr[0]=id;
		retArr[1]=adresse;
		retArr[2]=sumAmount;
		returnList.add(retArr);

		Query mockedQuery = mockQuery("word", text, returnList);
		replay(mockedQuery);
		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT c.website.id, c.website.domain, sum(c.amount) "
						+ "FROM Container c WHERE c.word.text LIKE :word AND c.word.active = TRUE "
						+ "GROUP BY c.website ORDER BY sum(c.amount) DESC")
				).andReturn(mockedQuery);
		replay(mockEm);
		WordDao wDAO= new WordDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.sitesOfWord(text);

		//Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(),testResult.toString());
	}

	/**
	 * Test Sitesofword method with invalid input
	 */
	@Test
	public void testsitesOfWordInvalid(){
		//Setup
		String text="test";



		JSONArray validator= new JSONArray();



		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();


		Query mockedQuery = mockQuery("word", text, returnList);
		replay(mockedQuery);
		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT c.website.id, c.website.domain, sum(c.amount) "
						+ "FROM Container c WHERE c.word.text LIKE :word AND c.word.active = TRUE "
						+ "GROUP BY c.website ORDER BY sum(c.amount) DESC")
				).andReturn(mockedQuery);
		replay(mockEm);
		WordDao wDAO= new WordDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.sitesOfWord(text);

		//Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(),testResult.toString());
	}
	
	

	


}
