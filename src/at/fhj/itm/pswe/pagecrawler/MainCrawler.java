package at.fhj.itm.pswe.pagecrawler;


import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.ejb3.annotation.TransactionTimeout;

import at.fhj.itm.pswe.model.Container;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.model.Word;
import at.fhj.itm.pswe.pagecrawler.linkcrawler.Init_LinkCrawler;
import at.fhj.itm.pswe.pagecrawler.wordanalyzer.Analyzer;

@Stateful
@LocalBean
public class MainCrawler implements Runnable {

	private String url;
	private int depth;
	private Init_LinkCrawler linkCrawler;
		
	@Inject
	Analyzer analyzer;
		
	public MainCrawler(){}
	
	public MainCrawler(String url, int depth) {
		this.url = url;
		this.depth = depth;
		
	}
	//Timeout manuell höher gesetzt da sonst transaction von jta abgebrochen wird
	//Temporäre lösung, code muss unbedingt beschleunigt werden
	@TransactionTimeout(3600)
	public void crawl() {
		
		
		try {
			linkCrawler = new Init_LinkCrawler(url, depth);
			System.out.println(linkCrawler.getFileStoragePath());

			System.out.println("Analyzer started!");
			//analyzer.analyzeResults(linkCrawler.getFileStoragePath());
			analyzer.readResultFile(linkCrawler.getFileStoragePath());
			//analyzer.testPersist();

			System.out.println("FINISHED ANALYZING");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		crawl();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

}
