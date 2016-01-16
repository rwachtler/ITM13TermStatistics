package at.fhj.itm.pswe.unittest.dao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import at.fhj.itm.pswe.dao.ArticleDao;
import at.fhj.itm.pswe.dao.WordDao;
import at.fhj.itm.pswe.model.Article;
import at.fhj.itm.pswe.model.Word;

public class ArticleMockTest {


	
	@Test
	public void createArticle(){
		String url = "www.koeckman.at";
		int id = 1;
		
		final Article a= new Article();
		a.setUrl(url);

		EntityManager mockEm = createMock(EntityManager.class);

		mockEm.persist(a);
		
		mockEm.flush();
		
		EasyMock.expectLastCall().andAnswer(new SideEffect() {
			
			@Override
			public void effect() throws Throwable {
				a.setId(1);
			}
		});
	
		replay(mockEm);
		
		ArticleDao aDAO= new ArticleDao();
		aDAO.setEntityManager(mockEm);
		Article result = aDAO.createArticle(a);
	
		Assert.assertEquals(a, result);
		verify(mockEm);
	
	}	
	
	@Test
	public void TestgetAVGAnalyzeDurationofArticle(){
		int tid = 12;
		int tsum = 10;
		int tcount = 2;
		
		int avgAnalyzeTest = 10;
		
		JSONObject validatorObject= new JSONObject();
		validatorObject.put("avgAnalyze", avgAnalyzeTest);
		//validatorObject.put("count", count);
		JSONArray validator= new JSONArray();
		validator.put(validatorObject);
		
		//QueryMock
		List<Object[]> returnList = new ArrayList<Object[]>();
		Object[] retArr= new Object[1];
		retArr[0]=avgAnalyzeTest;
		//retArr[1]=count;
		returnList.add(retArr);
		
		Query mockedQuery = createMock(Query.class);
		expect(mockedQuery.setParameter("id", tid)).andReturn(mockedQuery);
		expect(mockedQuery.getResultList()).andReturn(returnList);
		replay(mockedQuery);
		
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.createQuery(
				"SELECT SUM(ast.analyzeDuration)/COUNT(ast.analyzeDuration) "
						+ "FROM ArticleStat ast WHERE ast.article.id=:id ORDER BY ast.article.url")
				).andReturn(mockedQuery);
		replay(mockEm);
		ArticleDao aDAO= new ArticleDao();
		aDAO.setEntityManager(mockEm);
		//Test
		JSONArray testResult=aDAO.getAVGAnalyzeDurationofArticle(tid);
		//Verify
		verify(mockEm);
	}
	
}
