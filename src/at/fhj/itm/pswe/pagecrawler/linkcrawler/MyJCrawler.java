package at.fhj.itm.pswe.pagecrawler.linkcrawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyJCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$");

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
	 * parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		JSONObject obj = (JSONObject) this.myController.getCustomData();
		String start_url = (String) obj.get("startUrl");
		WebURL seedUrl = new WebURL();
		seedUrl.setURL(start_url);
		return !FILTERS.matcher(href).matches() && url.getDomain().equals(seedUrl.getDomain());
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String domain = page.getWebURL().getDomain();
		String subdomain = page.getWebURL().getSubDomain();
		String total_domain = subdomain + "." + domain;
		String url = page.getWebURL().toString();

		// System.out.println("Subdomain: " + subdomain + "| Domain: " +
		// domain);
		// System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

			Document doc = Jsoup.parseBodyFragment(htmlParseData.getHtml());
			getPageText(doc, total_domain, url);
		}
	}

	public void getPageText(Document doc, String domain, String url) {
		PrintWriter out = null;
		JSONObject obj = (JSONObject) this.myController.getCustomData();
		String path_to_file = obj.getString("filepath");
		// System.out.println("In MyJCrawler Path: " + path_to_file);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(path_to_file, true)));
			// Falls kein HTML gelesen werden kann (Bsp.: XML)
			if (doc.body() != null && !(doc.body().text().trim().equals(""))) {
				out.println(url);
				out.println(doc.body().text());
				// System.out.println("Write to file");
			}
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
			System.err.println("Error with Result-FileHandling: " + e.getMessage());
		} finally {
			if (out != null) {
				out.close();
			}
		}

	}

}
