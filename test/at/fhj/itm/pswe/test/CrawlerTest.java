package at.fhj.itm.pswe.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.fhj.itm.pswe.pagecrawler.MainCrawler;

public class CrawlerTest {
	private MainCrawler mc;
	@Before
	public void setup()
	{
		mc=new MainCrawler();
		mc.setDepth(1);
		mc.setUrl("http://pfiff.me/pswengi/");
		mc.crawl();
	   
	}
	
	@Test
	public void testCrawler()
	{
		 FileReader fr;
			try {
				//anpassen
				fr = new FileReader("C:/Users/Viktoria/Documents/GitHub/result/crawl/pfiff_me_pswengi__12_29_2015-7_51.txt");
				 BufferedReader br = new BufferedReader(fr);

				    String url = br.readLine();
				    Assert.assertEquals(mc.getUrl(), url);
				    
				    String dateString=br.readLine();
				    SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
				    System.out.println(sdf.format(new Date()));
				    Assert.assertEquals(sdf.format(new Date()), dateString);
				    
				    String urlStartpage=br.readLine();
				    Assert.assertEquals(mc.getUrl(), urlStartpage);
				    
				    String textfirstPage=br.readLine();
				    Assert.assertEquals("Das ist eine Testseite Seite 2", textfirstPage);
				    
				    String nextURL=br.readLine();
				    Assert.assertEquals("http://pfiff.me/pswengi/Seite2.html",nextURL);
				    
				    String textsecondPage=br.readLine();
				    Assert.assertEquals("Dies ist eine andere Testseite", textsecondPage);

				    br.close();
			} catch (FileNotFoundException ef) {
				ef.printStackTrace();
			} catch (IOException ei) {
				ei.printStackTrace();
			}
		
	}

}
