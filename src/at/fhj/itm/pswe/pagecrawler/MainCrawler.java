package at.fhj.itm.pswe.pagecrawler;

import at.fhj.itm.pswe.pagecrawler.linkcrawler.Init_LinkCrawler;
import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;

public class MainCrawler implements Runnable {

	private String url;
	private int depth;
	private Init_LinkCrawler linkCrawler;

	public MainCrawler(String url, int depth) {
		this.url = url;
		this.depth = depth;
	}

	public void crawl() {
		try {
			linkCrawler = new Init_LinkCrawler(url, depth);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Analyzer created!");
		Analyzer analyzer = new Analyzer(linkCrawler.getFileStoragePath());

		System.out.println("Analyzer started!");
		analyzer.analyzeResults();

		System.out.println("FINISHED ANALYZING");
	}

	@Override
	public void run() {
		crawl();
	}

}
