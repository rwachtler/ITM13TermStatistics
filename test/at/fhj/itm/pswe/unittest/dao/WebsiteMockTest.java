package at.fhj.itm.pswe.unittest.dao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Test;

import at.fhj.itm.pswe.dao.WebsiteDao;
import at.fhj.itm.pswe.model.Website;

public class WebsiteMockTest {
	
	@Test
	public void deleteWebsite(){
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
	public void createWebsite(){
		Website w = new Website();
		w.setDomain("http://test.at");
		w.setDescription("Test");
		w.setCrawldepth(1);
		w.setActive(true);
		
		WebsiteDao wDao=new WebsiteDao();
		
		EntityManager mockEm = EasyMock.createMock(EntityManager.class);
		
		mockEm.persist(w);
		EasyMock.expectLastCall();
		
		//set to replay state
		replay(mockEm);
		//Set Mock Object
		wDao.setEntityManager(mockEm);

		//TEST
		Website result = wDao.createWebsite("http://test.at", "Test", 1);

		//Verify
		verify(mockEm);
	}

}
