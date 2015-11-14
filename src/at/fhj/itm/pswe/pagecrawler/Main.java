package at.fhj.itm.pswe.pagecrawler;

import at.fhj.itm.pswe.pagecrawler.linkcrawler.Init_LinkCrawler;

public class Main {

	public static void main(String[] args) {
		try {
			// Writes all gathered Words from the given URL into <Project-Root>/result/crawl/result.txt
			Init_LinkCrawler linkCrawler = new Init_LinkCrawler("http://pswengi.bamb.at");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO Start Wordanalyzer and write Words in DB
	}

}
