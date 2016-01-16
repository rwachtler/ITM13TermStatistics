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
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import at.fhj.itm.pswe.dao.WebsiteDao;
import at.fhj.itm.pswe.dao.WordDao;
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
		Website w = new Website();
		w.setDomain("http://test.at");
		w.setDescription("Test");
		w.setCrawldepth(1);
		w.setActive(true);
		w.setLast_crawldate("1970-01-01");
		w.setId(1);
		
		EntityManager mockEm = EasyMock.createMock(EntityManager.class);
		
		mockEm.persist(w);
		EasyMock.expectLastCall();
		
		//set to replay state
		replay(mockEm);
		//Set Mock Object

		WebsiteDao wDao=new WebsiteDao();
		wDao.setEntityManager(mockEm);

		//TEST
		Website result = wDao.createWebsite("http://test.at", "Test", 1);

		//Verify
		verify(mockEm);
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

}
