package at.fhj.itm.pswe.pagecrawler;

import java.util.HashMap;

import at.fhj.itm.pswe.pagecrawler.linkcrawler.Init_LinkCrawler;
import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;

public class Main {

	public static void main(String[] args) {
		try {
			// Writes all gathered Words from the given URL into <Project-Root>/result/crawl/result.txt
			//Init_LinkCrawler linkCrawler = new Init_LinkCrawler("http://pswengi.bamb.at");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO Start Wordanalyzer and write Words in DB
		Analyzer analyzer = new Analyzer();
		
		analyzer.analyzeResults();
	}

}
