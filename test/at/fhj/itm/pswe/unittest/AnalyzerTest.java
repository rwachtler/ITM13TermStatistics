package at.fhj.itm.pswe.unittest;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;

public class AnalyzerTest {

	private Analyzer analyzer;
	
	@Before
	public void setup()
	{
		analyzer=new Analyzer();
	}
	
	//test if the counter works
	@Test
	public void testCalculateWordMap(){
		HashMap<String,Integer> result=new HashMap<String, Integer>();
		result=analyzer.calculateWordMap("Das ist ein Test Test bei dem Wörter schon ausgefiltert wurden sind");
		
		if(result !=null){
			int wurden=result.get("wurden");
			Assert.assertEquals(wurden, 1);
			
			int test=result.get("test");
			Assert.assertEquals(test, 2);

		}
	}
	
	//test if the filter works
	@Test (expected=java.lang.NullPointerException.class)
	public void testFilterCalculateWordMap(){
		HashMap<String,Integer> result=new HashMap<String, Integer>();
		result=analyzer.calculateWordMap("Das ist ein Test Test bei dem Wörter schon ausgefiltert wurden sind");
		
		int ist=result.get("ist");
		Assert.assertEquals(ist, 1);
	}
}
