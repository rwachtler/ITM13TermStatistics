package at.fhj.itm.pswe.unittest.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert; 
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
		Assert.assertEquals(testResult.toString(),validator.toString());


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
		Assert.assertEquals(testResult.toString(),validator.toString());


	}
}
