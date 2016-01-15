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
		replay(mockEm);
		
		wDao.setEntityManager(mockEm);
		
		wDao.deleteWebsite(1);
		
		verify(mockEm);
		
	}

}
