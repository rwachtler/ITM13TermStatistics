package at.fhj.itm.pswe.unittest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.fhj.itm.pswe.pagecrawler.MainCrawler;

public class CrawlerTest {
	private MainCrawler mc;
	private String time;
	private String dateActual;
	private String dateFile;
	@Before
	public void setup()
	{
		Calendar cal = Calendar.getInstance();
		

		time=cal.get(Calendar.HOUR) + "_" + cal.get(Calendar.MINUTE);
		
		dateFile = cal.get(Calendar.MONTH) + 1+ "_" + cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.YEAR);
		
		dateActual = cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "."+ cal.get(Calendar.YEAR);
		
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
					fr = new FileReader("result/crawl/pfiff_me_pswengi__"+dateFile+"-"+time+"/pfiff_me_pswengi__"+dateFile+"-"+time+".txt");
					BufferedReader br = new BufferedReader(fr);

				    String url = br.readLine();
				    Assert.assertEquals(mc.getUrl(), url);
				    
				    String dateString=br.readLine();

				    Assert.assertEquals(dateActual, dateString);
				    
				    String nextURL=br.readLine();
				    Assert.assertEquals("http://pfiff.me/pswengi/Seite2.html",nextURL);
				    
				    String textsecondPage=br.readLine();
				    Assert.assertEquals("Dies ist eine andere Testseite", textsecondPage);
				    
				    String urlStartpage=br.readLine();
				    Assert.assertEquals(mc.getUrl(), urlStartpage);
				    
				    String textfirstPage=br.readLine();
				    Assert.assertEquals("Das ist eine Testseite Seite 2", textfirstPage);
				    
				    br.close();
			} catch (FileNotFoundException ef) {
				ef.printStackTrace();
			} catch (IOException ei) {
				ei.printStackTrace();
			}
		
	}

}
