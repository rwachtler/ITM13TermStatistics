package at.fhj.itm.pswe.unittest.dao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.inject.New;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.xmlbeans.impl.jam.visitor.MVisitor;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.sun.jna.platform.win32.WinDef.WPARAM;

import at.fhj.itm.pswe.dao.AnalyzerDao;
import at.fhj.itm.pswe.model.Article;
import at.fhj.itm.pswe.model.ArticleStat;
import at.fhj.itm.pswe.model.Container;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.model.WebsiteStat;
import at.fhj.itm.pswe.model.Word;
import at.fhj.itm.pswe.model.WordlistEntry;
import at.fhj.itm.pswe.model.Wordtype;

public class AnalyzerMockTest {
	
	 @Test
	 public void testFindWord(){
		Word w= new Word();
		w.setText("Testwort");
		w.setActive(true);
		
		AnalyzerDao ad=new AnalyzerDao();
		
		EntityManager mockEm = createMock(EntityManager.class);
		expect(mockEm.find(Word.class, "Testwort")).andReturn(w);
		
		replay(mockEm);
		
		ad.setEntityManager(mockEm);
		
		ad.findWord("Testwort");
		
		verify(mockEm);

		 
	 }
	 
	 private Query mockQueryWordListEntry(String param, String replace,  List<WordlistEntry> results) {
			Query mockedQuery = createMock(Query.class);
			expect(mockedQuery.setParameter(param, replace)).andReturn(mockedQuery);
			expect(mockedQuery.getResultList()).andReturn(results);
			return mockedQuery;
	 }
	 
	 
	
	 @Test
	 public void testFindTypOfWord(){
		 Word w=new Word();
		 w.setText("Test");
		 
		 Wordtype wt=new Wordtype();
		 wt.setTexttype("nomen");
		 
		 WordlistEntry we=new WordlistEntry();
		 we.setWord(w.getText());
		 we.setWordtype(wt.getTexttype());
		 
		
		 
		 List<WordlistEntry> returnList = new ArrayList<WordlistEntry>();
		 returnList.add(we);
		 
		 AnalyzerDao ad=new AnalyzerDao();
		
		Query qMock=mockQueryWordListEntry("word", w.getText(),returnList);
		replay(qMock);

		EntityManager mockEm = createMock(EntityManager.class);
		
		expect(mockEm.createQuery(
				"SELECT wl FROM WordlistEntry wl WHERE wl.word = :word")
				).andReturn(qMock);
		
		replay(mockEm);

		ad.setEntityManager(mockEm);

		String testResult= ad.findTypeForWord(w.getText());

		verify(mockEm);

		Assert.assertEquals(testResult,we.getWordtype());
	 }
    
	 //unbekannter Typ----------------wie in wordmock umaendern----------------------
	 @Test
	 public void testFindNoTypOfWord(){
		 Word w=new Word();
		 w.setText("blabla");
		 
		 
		 WordlistEntry we=new WordlistEntry();
		 we.setWord(w.getText());
		 
		 
		 List<WordlistEntry> returnList = new ArrayList<WordlistEntry>();
		 returnList.add(we);
		 
		 AnalyzerDao ad=new AnalyzerDao();
		
		Query qMock=mockQueryWordListEntry("word", w.getText(),returnList);
		replay(qMock);

		EntityManager mockEm = createMock(EntityManager.class);
		
		expect(mockEm.createQuery(
				"SELECT wl FROM WordlistEntry wl WHERE wl.word = :word")
				).andReturn(qMock);
		
		replay(mockEm);

		ad.setEntityManager(mockEm);

		String testResult= ad.findTypeForWord(w.getText());
		System.out.println(testResult+"Word");
		System.out.println(we.getWordtype());
		
		verify(mockEm);

		Assert.assertEquals(testResult,we.getWordtype());
	 }

	 
	 private Query mockQuerySetTypOfWord(String param, String wtype,  List<Wordtype> results) {
			Query mockedQuery = createMock(Query.class);
			expect(mockedQuery.setParameter(param, wtype)).andReturn(mockedQuery);
			expect(mockedQuery.getResultList()).andReturn(results);
			return mockedQuery;
	 }
	 
    //testfall wenn wordtype existiert -------------------------------------------------------------
	 //Problem mit persist
	 @Test
	 public void testSetTypeForWord(){
		 
		 Word wNomen=new Word();
		 wNomen.setText("Test");
		 
		 Wordtype wtNomen=new Wordtype();
		 wtNomen.setTexttype("nomen");
		 
		 wNomen.setWordtype(wtNomen);
		 
		 List<Wordtype> wtList=new ArrayList<Wordtype>();
		wtList.add(wtNomen);
		 
		 AnalyzerDao ad=new AnalyzerDao();
		 	
		Query qMock=null;
		qMock=mockQuerySetTypOfWord("wordtype", wtNomen.getTexttype(),wtList);
		replay(qMock);
		

		EntityManager mockEm = createMock(EntityManager.class);

		expect(mockEm.createQuery(
				"SELECT wt FROM Wordtype wt WHERE wt.texttype = :wordtype")
				).andReturn(qMock);
			    

		mockEm.persist(wtNomen);
		EasyMock.expectLastCall().once();
		
		mockEm.persist(wNomen);
		EasyMock.expectLastCall().once();
		replay(mockEm);
		
		ad.setEntityManager(mockEm);

		System.out.println(wNomen.getWordtype().getTexttype());
		System.out.println(wtNomen.getTexttype());
		ad.setTypeForWord(wNomen, wtNomen.getTexttype());
		
		verify(mockEm);
		
		Assert.assertEquals(wNomen.getWordtype().getTexttype(), wtNomen.getTexttype());
 
		 
	 }
    
    //zweiter testfall wenn wordtype noch nicht vorhanden -------------------------------------------
	 
	 
	 private Query mockQueryArticle(String param, String replace, List<Object[]> results) {
			Query mockedQuery = createMock(Query.class);
			expect(mockedQuery.setParameter(param, replace)).andReturn(mockedQuery);
			expect(mockedQuery.getResultList()).andReturn(results);
			return mockedQuery;
	 }
	 

	 @Test
	 public void testFindArticle(){
		 Article at=new Article();
		 at.setUrl("http://test.at/test");
		 
		 List<Object[]> returnList = new ArrayList<Object[]>();

		Object[] retArr= new Object[3];
		retArr[0]=at.getUrl();
		
		returnList.add(retArr);
	 
		 AnalyzerDao adao=new AnalyzerDao();
		 
		EntityManager mockEm = createMock(EntityManager.class);
		
		 
		Query qMock=mockQueryArticle("url", at.getUrl(),returnList);
		replay(qMock);		

		expect(mockEm.createQuery(
				"SELECT a.id, a.url FROM Article a WHERE a.url = :url")
				).andReturn(qMock);
		
		expect(mockEm.find(Article.class, at.getUrl())).andReturn(at);
		replay(mockEm);


		adao.setEntityManager(mockEm);
		
		

		System.out.println(at.getUrl());
		Article testResult= adao.findArticle(at.getUrl());
		System.out.println(testResult.getUrl());
		
		verify(mockEm);
		
		Assert.assertEquals(testResult.getUrl(),at.getUrl());
		 
		 
	 }
	 //---------------------------------------------------------------------------
	 @Test
	 public void testFindArticleNotExisting(){
		 
		 List<Object[]> returnList = new ArrayList<Object[]>();
	 
		 AnalyzerDao adao=new AnalyzerDao();
		 
		EntityManager mockEm = createMock(EntityManager.class);
		
		 
		Query qMock=mockQueryArticle("url", "http://test.at/test",returnList);
		replay(qMock);		

		expect(mockEm.createQuery(
				"SELECT a.id, a.url FROM Article a WHERE a.url = :url")
				).andReturn(qMock);
		
		final Article a=new Article();
		a.setUrl("http://test.at/test");
		a.setId(1);
		mockEm.persist(a);
		//Fakes the db setting the id in the website object
		EasyMock.expectLastCall().andAnswer(new SideEffect() {
			
			@Override
			public void effect() throws Throwable {
				a.setId(1);
				
			}
		});
		replay(mockEm);


		adao.setEntityManager(mockEm);
		
		Article testResult= adao.findArticle("http://test.at/test");
		
		verify(mockEm);
		Assert.assertEquals(testResult.getUrl(),a.getUrl());
		 
		 
	 }


	 private Query mockQueryWebsite(String param, String replace,  List<Website> results) {
			Query mockedQuery = createMock(Query.class);
			expect(mockedQuery.setParameter(param, replace)).andReturn(mockedQuery);
			expect(mockedQuery.getResultList()).andReturn(results);
			return mockedQuery;
	 }
	 
	 @Test
	 public void testFindWebsite(){
		 Website ws=new Website();
		 ws.setActive(true);
		 ws.setDescription("Testseite");
		 ws.setDomain("http://test.at");
		 ws.setCrawldepth(1);
			
		 List<Website> returnList = new ArrayList<Website>();
		 returnList.add(ws);
		 
		 AnalyzerDao ad=new AnalyzerDao();
		
		 Query qMock=mockQueryWebsite("domain", ws.getDomain(),returnList);
			replay(qMock);

			EntityManager mockEm = createMock(EntityManager.class);

			expect(mockEm.createQuery(
					"SELECT w FROM Website w WHERE w.domain = :domain")
					).andReturn(qMock);

			replay(mockEm);

			ad.setEntityManager(mockEm);

			Website testResult= ad.findWebsite(ws.getDomain());
			verify(mockEm);

			Assert.assertEquals(testResult.getDomain(),ws.getDomain());
	 }
	 
	 @Test(expected=NullPointerException.class)
	 public void testFindNoWebsites(){

		 List<Website> returnList = new ArrayList<Website>();
		 AnalyzerDao ad=new AnalyzerDao();
		
		 Query qMock=mockQueryWebsite("domain", "http://test.at",returnList);
			replay(qMock);

			EntityManager mockEm = createMock(EntityManager.class);

			expect(mockEm.createQuery(
					"SELECT w FROM Website w WHERE w.domain = :domain")
					).andReturn(qMock);

			replay(mockEm);

			ad.setEntityManager(mockEm);

			Website testResult= ad.findWebsite("http://test.at");
			verify(mockEm);

			Assert.assertEquals(testResult.getDomain(),null);
	 }

	 @Test
	 public void testSaveContainer(){
		 Container c=new Container();
		 c.setAmount(1);
		 Article a=new Article();
		 a.setUrl("http://test.at/test");
		 c.setArticle(a);
		 Website ws=new Website();
		 ws.setDomain("http://test.at");
		 c.setWebsite(ws);
		 Word w=new Word();
		 w.setText("Test");
		 w.setActive(true);
		 c.setWord(w);
		 
		 AnalyzerDao ad=new AnalyzerDao();

		EntityManager mockEm=createMock(EntityManager.class);	
		
		
		mockEm.persist(c);
		EasyMock.expectLastCall().once();
		replay(mockEm);
				
		ad.setEntityManager(mockEm);
		
		ad.saveContainer(c);
			
		verify(mockEm);
	 }
	 
	 @Test
	 public void testSaveArticleStat(){
		 ArticleStat as=new ArticleStat();
		 Article a=new Article();
		 a.setUrl("http://test.at/test");
		 as.setArticle(a);
		 as.setAnalyzeDuration(42);
		 as.setLogDate("15.01.2015");
		 
		 AnalyzerDao ad=new AnalyzerDao();

		EntityManager mockEm=createMock(EntityManager.class);	
		
		mockEm.persist(as);
		EasyMock.expectLastCall().once();
		replay(mockEm);
				
		ad.setEntityManager(mockEm);
		
		ad.saveArticleStat(as);
			
		verify(mockEm);
	 }
	 
	 @Test
	 public void testSaveWebsiteStat(){
		 WebsiteStat ws=new WebsiteStat();
		 ws.setAnalyzeDuration(15);
		 ws.setCrawlDuration(42);
		 ws.setLogDate("15.01.2015");
		 Website w=new Website();
		 w.setDomain("http://test.at");
		 w.setActive(true);
		 w.setDescription("Test");
		 w.setLast_crawldate("14.01.2015");
		 ws.setWebsite(w);
		
		 AnalyzerDao ad=new AnalyzerDao();

		EntityManager mockEm=createMock(EntityManager.class);	
		
		mockEm.persist(ws);
		EasyMock.expectLastCall().once();
		replay(mockEm);
				
		ad.setEntityManager(mockEm);
		
		ad.saveWebsiteStat(ws);
			
		verify(mockEm);
	 }
	 
}
