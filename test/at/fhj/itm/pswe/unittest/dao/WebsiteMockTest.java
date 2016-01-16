package at.fhj.itm.pswe.unittest.dao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import at.fhj.itm.pswe.dao.WebsiteDao;
import at.fhj.itm.pswe.dao.WordDao;
import at.fhj.itm.pswe.model.Article;
import at.fhj.itm.pswe.model.Website;

public class WebsiteMockTest {
	
	@Test
	public void testdeleteWebsite(){
		//SETUP 
		Website w=new Website();
		w.setId(1);
		w.setDomain("http://test.at");
		w.setActive(true);
		w.setDescription("Test");
		
		WebsiteDao wDao=new WebsiteDao();
		
		EntityManager mockEm = createMock(EntityManager.class);
		
		expect(mockEm.find(Website.class, w.getId())).andReturn(w);

		mockEm.remove(w);
		EasyMock.expectLastCall().once();
		mockEm.flush();
		EasyMock.expectLastCall().once();
		replay(mockEm);
		
		wDao.setEntityManager(mockEm);
		
		//TEST SETUP
		wDao.deleteWebsite(1);
		
		//VERIFY
		verify(mockEm);
		
	}
	
	@Test
	public void testcreateWebsite(){
		final Website w = new Website();
		w.setDomain("http://test.at");
		w.setDescription("Test");
		w.setCrawldepth(1);
		
		
		EntityManager mockEm = EasyMock.createMock(EntityManager.class);
		
		//expect void methods
		mockEm.persist(w);
		mockEm.flush();
		//Fakes the db setting the id in the website object
		EasyMock.expectLastCall().andAnswer(new SideEffect() {
			
			@Override
			public void effect() throws Throwable {
				w.setId(1);
				
			}
		});
		
		//set to replay state
		replay(mockEm);
		
		//Set Mock Object
		WebsiteDao wDao=new WebsiteDao();
		wDao.setEntityManager(mockEm);

		//TEST
		Website result = wDao.createWebsite(w);

		//Verify
		Assert.assertEquals(w, result);
		verify(mockEm);
	}
	
	abstract public class SideEffect implements IAnswer<Void> {
		public Void answer() throws Throwable
		{
		effect();
		//Void is an uninstantiable placeholder class
		return null;
		}

		abstract public void effect() throws Throwable;
		}
	
	
	@Test
	public void testreadWebsite(){
		//SETUP 
		Website w=new Website();
		w.setId(1);
		w.setDomain("http://test.at");
		w.setActive(true);
		w.setDescription("Test");
		w.setLast_crawldate("2015-05-05");
		
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[5];
		retArr[0]=w.getId();
		retArr[1]=w.getDomain();
		retArr[2]=w.getLast_crawldate();
		retArr[3]=w.getDescription();
		retArr[4]=1;
		returnList.add(retArr);
		
		//QueryMock
		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("url", w.getDomain())).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);
		
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT ws.id, ws.domain, ws.last_crawl_date, ws.description, ws.active FROM Website ws WHERE ws.domain = :url")
				).andReturn(mockedQuery);
		replay(mockEm);
		

		WebsiteDao wDao=new WebsiteDao();
		wDao.setEntityManager(mockEm);
		
		//TEST SETUP
		wDao.readWebsite(w.getDomain());
		
		//VERIFY
		verify(mockEm);		
	}
	
	@Test
	public void testfindAllWebsites(){
		//SETUP 
		List<Website> expected = new ArrayList<>();
		Website w = new Website();
		w.setId(1);
		w.setDomain("http://test.at");
		w.setActive(true);
		w.setDescription("Test");
		w.setLast_crawldate("2015-05-05");
		expected.add(w);
		
		//QueryMock
		TypedQuery<Website> mockedQuery = createMock(TypedQuery.class);
		expect(mockedQuery.getResultList()).andReturn(expected);
		replay(mockedQuery);
		
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT DISTINCT w FROM Website w ORDER BY w.id", Website.class)
				).andReturn(mockedQuery);
		replay(mockEm);
		

		WebsiteDao wDao=new WebsiteDao();
		wDao.setEntityManager(mockEm);
		
		//TEST SETUP
		wDao.findAllWebsites();
		
		//VERIFY
		verify(mockEm);
	}
	
	@Test
	public void testfindAllWebsitesJSON(){
		//Setup JSON
		long id = 1;
		String address = "test.at"; 
		String description = "Testseite";
		boolean active = true;
		String lCrawled = "2015-05-05";
		int depth = 2;
		
		JSONObject validatorObject= new JSONObject();
		validatorObject.put("id", id);
		validatorObject.put("address", address);
		validatorObject.put("description", description);
		validatorObject.put("active", active);
		validatorObject.put("lCrawled", lCrawled);
		validatorObject.put("depth", depth);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);
		
		//SETUP Website
		List<Website> expected = new ArrayList<>();
		Website w = new Website();
		w.setId(1);
		w.setDomain("test.at");
		w.setActive(true);
		w.setDescription("Testseite");
		w.setLast_crawldate("2015-05-05");
		w.setCrawldepth(2);
		expected.add(w);
		
		//QueryMock
		TypedQuery<Website> mockedQuery = createMock(TypedQuery.class);
		expect(mockedQuery.getResultList()).andReturn(expected);
		replay(mockedQuery);
		
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT DISTINCT w FROM Website w ORDER BY w.id", Website.class)
				).andReturn(mockedQuery);
		replay(mockEm);
		

		WebsiteDao wDao=new WebsiteDao();
		wDao.setEntityManager(mockEm);
		
		//TEST SETUP
		JSONArray testResult = wDao.findAllWebsitesJSON();
		
		verify(mockEm);
		
		Assert.assertEquals(validator.toString(),testResult.toString());
		
	}
	
	@Test
	public void testupdateWebsite(){
		//SETUP
		Website w=new Website();
		w.setId(2);
		w.setDomain("http://newtest.at");
		w.setActive(false);
		w.setDescription("NewTest");
		w.setLast_crawldate("2015-05-08");

		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[5];
		retArr[0]=w.getId();
		retArr[1]=w.getDomain();
		retArr[2]=w.getLast_crawldate();
		retArr[3]=w.getDescription();
		retArr[4]=1;
		returnList.add(retArr);

		EntityManager mockEm = createMock(EntityManager.class);
		//Expect one single call to find
		expect(mockEm.find(Website.class, w.getId())).andReturn(w);
		mockEm.flush();
		EasyMock.expectLastCall();
		//set to replay state
		replay(mockEm);

		WebsiteDao wDao=new WebsiteDao();
		wDao.setEntityManager(mockEm);

		//TEST SETUP
		wDao.updateWebsite(w);

		//VERIFY
		verify(mockEm);
	}

	@Test
	public void testarticleTimelineofOneWebsite(){
		//Setup
		int id = 5;
		int amount = 18;
		String crawldate = "2015-06-15";

		JSONObject validatorObject= new JSONObject();
		validatorObject.put("amount", amount);
		validatorObject.put("crawldate", crawldate);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);

		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[3];
		retArr[0]=amount;
		retArr[1]=crawldate;
		retArr[2]=id;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("id", id)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);
		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT COUNT(DISTINCT c.article.url), c.logDate, c.article.id "
						+ "FROM Container c WHERE c.website.id=:id GROUP BY c.logDate, c.website.id ORDER BY c.article.id")
				).andReturn(mockedQuery);
		replay(mockEm);

		WebsiteDao wDAO= new WebsiteDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.articleTimelineofOneWebsite(id);

		//Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(),testResult.toString());
	}

	@Test
	public void testgetAverageWebsiteStats(){
		//Setup Average
		int id = 5;
		int crawlDuration = 84827;
		int analyzeDuration = 41000;
		String domain = "test.at";
		double avg_article = 0.0;

		JSONObject validatorObject= new JSONObject();
		validatorObject.put("crawlDuration", crawlDuration);
		validatorObject.put("analyzeDuration", analyzeDuration);
		validatorObject.put("domain", domain);
		validatorObject.put("avg_article", avg_article);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);

		//Setup Article Timeline of Website
		int amount = 0;
		String crawldate = "2015-06-15";

		JSONObject validatorObject2= new JSONObject();
		validatorObject2.put("amount", amount);
		validatorObject2.put("crawldate", crawldate);
		JSONArray validator2= new JSONArray();
		validator2.put(validatorObject2);

		//QueryMock Average
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[3];
		retArr[0]=crawlDuration;
		retArr[1]=analyzeDuration;
		retArr[2]=domain;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("id", id)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);

		//QueryMock Timeline of Website
		List<Object[]> returnList2 = new ArrayList<Object[]>();
		Object[] retArr2= new Object[3];
		retArr2[0]=amount;
		retArr2[1]=crawldate;
		retArr2[2]=id;
		returnList2.add(retArr2);

		Query mockedQuery2 = createMock(Query.class);
		expect(mockedQuery2.setParameter("id", id)).andReturn(mockedQuery2);
		expect(mockedQuery2.getResultList()).andReturn(returnList2);
		replay(mockedQuery2);

		//EM Mock Average
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT SUM(ws.crawlDuration)/COUNT(ws.id), SUM(ws.analyzeDuration)/COUNT(ws.analyzeDuration), ws.website.domain "
						+ "FROM WebsiteStat ws WHERE ws.website.id=:id GROUP BY ws.website.id, ws.website.domain ORDER BY ws.website.domain")
				).andReturn(mockedQuery);

		//EM Mock Timeline of Website
		expect(mockEm.createQuery(
				"SELECT COUNT(DISTINCT c.article.url), c.logDate, c.article.id "
						+ "FROM Container c WHERE c.website.id=:id GROUP BY c.logDate, c.website.id ORDER BY c.article.id")
				).andReturn(mockedQuery2);
		replay(mockEm);

		WebsiteDao wDAO= new WebsiteDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.getAverageWebsiteStats(id);

		//Verify
		verify(mockEm);
		
		Assert.assertEquals(validator.toString(),testResult.toString());
	}
	
	@Test
	public void testfindWordsOfSite(){
		//Setup
		int id = 5;
		int amount = 18;
		String text = "word";

		JSONObject validatorObject= new JSONObject();
		validatorObject.put("word", text);
		validatorObject.put("amount", amount);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);

		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[2];
		retArr[0]=text;
		retArr[1]=amount;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("id", id)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);
		
		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT c.word.text, sum(c.amount) "
						+ "FROM Container c WHERE c.website.id=:id AND c.word.active = TRUE "
						+ "GROUP BY c.word ORDER BY sum(c.amount) DESC")
				).andReturn(mockedQuery);
		replay(mockEm);

		WebsiteDao wDAO= new WebsiteDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.findWordsOfSite(id, 0);

		//Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(),testResult.toString());
	}
	
	@Test
	public void testtimeLine4WordAndSite(){
		//Setup
		int id = 5;
		int amount = 18;
		String date = "2015-05-05";
		String startdate = "2015-05-04";
		String enddate = "2015-05-06";
		String word = "test";

		JSONObject validatorObject= new JSONObject();
		validatorObject.put("date", date);
		validatorObject.put("amount", amount);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);

		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[2];
		retArr[0]=date;
		retArr[1]=amount;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("id", id)).andReturn(mockedQuery);
		expect(mockedQuery.setParameter("startdate", startdate)).andReturn(mockedQuery);
		expect(mockedQuery.setParameter("enddate", enddate)).andReturn(mockedQuery);
		expect(mockedQuery.setParameter("word", word)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);
		
		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT co.logDate, SUM(co.amount) " + "FROM Container co "
						+ "WHERE co.website.id = :id " + "AND (co.logDate BETWEEN :startdate AND :enddate) "
						+ "AND co.word.text = :word " + "GROUP BY co.word.text, co.logDate")
				).andReturn(mockedQuery);
		replay(mockEm);

		WebsiteDao wDAO= new WebsiteDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.timeLine4WordAndSite(id, word, startdate, enddate);

		//Verify
		verify(mockEm);
		Assert.assertEquals(validator.toString(),testResult.toString());
	}
	
	@Test
	public void testfindAllArticlesOfOneWebsite(){
		//Setup
		int id = 5;
		String url = "test.at";
		
		List<Article> list = new ArrayList<>();

		Article ar = new Article();
		ar.setId(id);
		ar.setUrl(url);

		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[2];
		retArr[0]=id;
		retArr[1]=url;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("id", id)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);
		
		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT DISTINCT c.article.id, c.article.url "
						+ "FROM Container c WHERE c.website.id=:id ORDER BY c.article.id")
				).andReturn(mockedQuery);
		replay(mockEm);

		WebsiteDao wDAO= new WebsiteDao();
		wDAO.setEntityManager(mockEm);

		//Test
		List<Article> testResult=wDAO.findAllArticlesOfOneWebsite(id);

		//Verify
		verify(mockEm);
	}
	
	@Test
	public void testfindAllArticlesOfOneWebsiteJSON(){
		//Setup NON JSON
		int id = 5;
		String url = "test.at";
		
		List<Article> list = new ArrayList<>();

		Article ar = new Article();
		ar.setId(id);
		ar.setUrl(url);
		
		//Setup JSON
		JSONObject validatorObject= new JSONObject();
		validatorObject.put("id", id);
		validatorObject.put("url", url);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);

		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[2];
		retArr[0]=id;
		retArr[1]=url;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("id", id)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);
		
		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT DISTINCT c.article.id, c.article.url "
						+ "FROM Container c WHERE c.website.id=:id ORDER BY c.article.id")
				).andReturn(mockedQuery);
		replay(mockEm);

		WebsiteDao wDAO= new WebsiteDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.findAllArticlesOfOneWebsiteJSON(id);

		//Verify
		verify(mockEm);
	}
	
	@Test
	public void testgetAverageWordAmountofWebsite(){
		//Setup
		int id = 5;
		int avgwords = 56;
		double avgwordlength = 8.76545631;
		
		JSONObject validatorObject= new JSONObject();
		validatorObject.put("avgwords", avgwords);
		validatorObject.put("avgwordlength", avgwordlength);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);

		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[2];
		retArr[0]=avgwords;
		retArr[1]=avgwordlength;
		returnList.add(retArr);

		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("id", id)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);
		
		//EM Mock
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT SUM(c.amount)*1.0/COUNT(DISTINCT c.article.id), SUM(LENGTH(c.word.text))*1.0/COUNT(c.word.text) "
						+ "FROM Container c WHERE c.website.id=:id GROUP BY c.website.id ORDER BY c.website.domain")
				).andReturn(mockedQuery);
		replay(mockEm);

		WebsiteDao wDAO= new WebsiteDao();
		wDAO.setEntityManager(mockEm);

		//Test
		JSONArray testResult=wDAO.getAverageWordAmountofWebsite(id);

		//Verify
		verify(mockEm);
	}

}
