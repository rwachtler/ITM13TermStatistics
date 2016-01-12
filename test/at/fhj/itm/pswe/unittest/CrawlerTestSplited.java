package at.fhj.itm.pswe.unittest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import at.fhj.itm.pswe.pagecrawler.MainCrawler;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CrawlerTestSplited {
	private static MainCrawler mc;
	private static BufferedReader br;
	private static String dateFile;
	
	@BeforeClass
	public static void setup()
	{
		Calendar cal = Calendar.getInstance();
		
		String start_time=cal.get(Calendar.HOUR) + "_" + cal.get(Calendar.MINUTE);
		
		String start_date = cal.get(Calendar.MONTH) + 1+ "_" + cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.YEAR);
		
		mc=new MainCrawler();
		mc.setDepth(1);
		mc.setUrl("http://pfiff.me/pswengi/");
		mc.crawl();
	    
		dateFile = cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "."+ cal.get(Calendar.YEAR);
		
	    FileReader fr;
		try {
			fr = new FileReader("result/crawl/pfiff_me_pswengi__"+start_date+"-"+start_time+"/pfiff_me_pswengi__"+start_date+"-"+start_time+".txt");
			 br = new BufferedReader(fr);
		} catch (FileNotFoundException ef) {
			ef.printStackTrace();
		} catch (IOException ei) {
			ei.printStackTrace();
		}
	   
	}
	
	
	@Test
	public void atestURL()
	{
	    String url;
		try {
			url = br.readLine();
		    Assert.assertEquals(mc.getUrl(), url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Test 1");
	    
	}
	
	@Test
	public void btestDate(){
		 String dateString;
		try {
			dateString = br.readLine();
			 Assert.assertEquals(dateFile, dateString);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		System.out.println("Test 2");
	}
	
	@Test
	public void ctestStartURL(){
		   String urlStartpage;
		try {
			urlStartpage = br.readLine();
			Assert.assertEquals(mc.getUrl(), urlStartpage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 System.out.println("Test 3");  
	}
	
	@Test
	public void dtestFirstPageText(){
		   
	    String textfirstPage;
		try {
			textfirstPage = br.readLine();
			Assert.assertEquals("Das ist eine Testseite Seite 2", textfirstPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    System.out.println("Test 4");
	}
	
	@Test
	public void etestUrlSecondPage(){
		  
	    String nextURL;
		try {
			nextURL = br.readLine();
			Assert.assertEquals("http://pfiff.me/pswengi/Seite2.html",nextURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Test 5");
	  
	}
	
	@Test
	public void ftestTextSecondPage(){ 
	    String textsecondPage;
		try {
			textsecondPage = br.readLine();
		    Assert.assertEquals("Dies ist eine andere Testseite", textsecondPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Test 6");
	}
	
	@AfterClass
	public static void teardown(){
	    try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
