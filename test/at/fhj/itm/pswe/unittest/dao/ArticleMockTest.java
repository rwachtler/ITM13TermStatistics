package at.fhj.itm.pswe.unittest.dao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Test;

import at.fhj.itm.pswe.dao.ArticleDao;
import at.fhj.itm.pswe.dao.WordDao;
import at.fhj.itm.pswe.model.Article;
import at.fhj.itm.pswe.model.Word;

public class ArticleMockTest {


	
	@Test
	public void createArticle(){
	Article a= new Article();
	a.setUrl("www.koeckman.at");

	ArticleDao aDAO= new ArticleDao();

	EntityManager mockEm = createMock(EntityManager.class);
	expect(mockEm.find(Article.class,a.getUrl())).andReturn(a);
	replay(mockEm);
	mockEm.persist(a);
	EasyMock.expectLastCall().once();
	mockEm.flush();
	EasyMock.expectLastCall().once();
	replay(mockEm);
	
	aDAO.setEntityManager(mockEm);
	aDAO.createArticle(a.getUrl());
	aDAO.readArticle(a.getUrl());

	verify(mockEm);
	
	}	
	
}
