package at.fhj.itm.pswe.pagecrawler.linkcrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Init_LinkCrawler {

	private String filename, crawlStorageFolder, path_to_file, url_escaped;
	private Calendar cal;

	public Init_LinkCrawler(String url, int depth) throws Exception {

		// Safe starting time of Crawler
		cal = Calendar.getInstance();
		Date start_date = cal.getTime();

		url_escaped = url.replace(".", "_").replace("http://", "").replace("/", "_") + "_"
				+ (cal.get(Calendar.MONTH) + 1) + "_" + cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.YEAR)
				+ "-" + cal.get(Calendar.HOUR) + "_" + cal.get(Calendar.MINUTE);

		// Add to Project Root folders result/crawl where our result.txt is
		// saved
		crawlStorageFolder = "./result/crawl/" + url_escaped + "/";
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
		controller.addSeed(url);

		cal = Calendar.getInstance();

		filename = url_escaped + ".txt";
		path_to_file = crawlStorageFolder + filename;

		JSONObject obj = new JSONObject();
		obj.put("startUrl", url);
		obj.put("filepath", path_to_file);
		controller.setCustomData(obj);

		File f = new File(path_to_file);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path_to_file, true)));
		// Schreibe bei erstellen der Datei wichtigste Daten in die ersten 2
		// Zeilen
		if (f.length() == 0) {
			out.println(url);
			out.println(cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "."
					+ cal.get(Calendar.YEAR));
		}
		if (out != null) {
			out.close();
		}

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyJCrawler.class, numberOfCrawlers);

		// Reinit Calendar-Object to get new time
		cal = Calendar.getInstance();
		Date end_date = cal.getTime();

		// Calculate Seconds, Minutes and Hours of Crawling procedure
		long diff = end_date.getTime() - start_date.getTime();
		long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);
		long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
		long diffHours = TimeUnit.MILLISECONDS.toHours(diff);

		PrintWriter file_out = null;
		try {
			file_out = new PrintWriter(new BufferedWriter(new FileWriter(path_to_file, true)));
			file_out.println(diffHours + ":" + diffMinutes + ":" + diffSeconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				file_out.close();
			}
		}

		System.out.println("Crawler Finished");

	}

	public String getFileStoragePath() {
		return path_to_file;
	}

}
