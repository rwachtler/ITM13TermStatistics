package at.fhj.itm.pswe.pagecrawler;

import at.fhj.itm.pswe.pagecrawler.linkcrawler.Init_LinkCrawler;
import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;
import at.fhj.itm.pswe.view.Charts_db;

public class MainCrawler {
	
	private String url;
	
	public MainCrawler(String url){
		this.url = url;
	}

	public void crawl() {
		try {
			// Writes all gathered Words from the given URL into <Project-Root>/result/crawl/result.txt
			Init_LinkCrawler linkCrawler = new Init_LinkCrawler(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO Start Wordanalyzer and write Words in DB
		Analyzer analyzer = new Analyzer();
		
		analyzer.analyzeResults();
		
		System.out.println("FINISHED ANALYZING");
		
		Charts_db charts=new Charts_db();
		charts.linechartOverPeriod();
		charts.barchartfrequentWordofSide();
		charts.barchartOneWordAllSides();
	}

}
