package at.fhj.itm.pswe.pagecrawler.linkcrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class Init_LinkCrawler {
	
	private String filename;

	public Init_LinkCrawler(String url, int depth) throws Exception{
		
		String startSeed=url;
		WebURL startUrl=new WebURL();
		startUrl.setURL(startSeed);
		
		// Add to Project Root folders result/crawl where our result.txt is saved
		String crawlStorageFolder = "./result/crawl/";
		int numberOfCrawlers = 10;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(25);
		config.setMaxDepthOfCrawling(depth);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		controller.addSeed(startSeed);
		
		controller.setCustomData(startUrl);

		final Calendar cal = Calendar.getInstance();

		filename = url.replace(".", "_").replace("http://", "") + "_" + (cal.get(Calendar.MONTH) + 1) + "_"
				+ cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.HOUR) + "_"
				+ cal.get(Calendar.MINUTE) + ".txt";
		String path_to_file = crawlStorageFolder + filename;
		File f = new File(path_to_file);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path_to_file, true)));
		// Schreibe bei erstellen der Datei wichtigste Daten in die ersten 2
		// Zeilen
		if (f.length() == 0) {
			out.println(url);
			out.println(cal.getTime().toString());
		}
		if (out != null) {
			out.close();
		}
		
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyJCrawler.class, numberOfCrawlers);
		System.out.println("Finished");

	}
	
	public String getFilename(){
		return filename;
	}

}
