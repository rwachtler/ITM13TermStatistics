package at.fhj.itm.pswe.pagecrawler;

import at.fhj.itm.pswe.pagecrawler.linkcrawler.Init_LinkCrawler;
import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;
import at.fhj.itm.pswe.view.Charts_db;

public class MainCrawler implements Runnable{
	
	private String url;
	private int depth;
	public MainCrawler(String url, int depth){
		this.url = url;
		this.depth=depth;
	}

	public void crawl() {
		try {
			// Writes all gathered Words from the given URL into <Project-Root>/result/crawl/result.txt
			Init_LinkCrawler linkCrawler = new Init_LinkCrawler(url,depth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO Start Wordanalyzer and write Words in DB
		System.out.println("Analyzer created!");
		Analyzer analyzer = new Analyzer();
		
		System.out.println("Analyzer started!");
		analyzer.analyzeResults();
		
		System.out.println("FINISHED ANALYZING");
		
		Charts_db charts=new Charts_db();
		charts.linechartOverPeriod();
		charts.barchartfrequentWordofSide();
		charts.barchartOneWordAllSides();
	}

	@Override
	public void run() {
		crawl();
	}

}
