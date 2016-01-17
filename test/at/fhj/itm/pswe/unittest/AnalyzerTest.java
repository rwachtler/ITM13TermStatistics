package at.fhj.itm.pswe.unittest;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;
import junit.framework.Assert;

public class AnalyzerTest {

	private Analyzer analyzer;

	@Before
	public void setup() {
		analyzer = new Analyzer();
	}

	// test if the counter works
	@Test
	public void testCalculateWordMap() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		result = analyzer.calculateWordMap("Das ist ein Test Test bei dem W�rter schon ausgefiltert worden sind");

		if (result != null) {
			int worden = result.get("worden");
			Assert.assertEquals(worden, 1);

			int test = result.get("Test");
			Assert.assertEquals(test, 2);

		}
	}

	// test if the filter works
	@Test(expected = java.lang.NullPointerException.class)
	public void testFilterCalculateWordMap() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		result = analyzer.calculateWordMap("Das ist ein Test Test bei dem W�rter schon ausgefiltert wurden sind");

		int ist = result.get("ist");
		Assert.assertEquals(ist, 1);
	}
}
