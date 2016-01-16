package at.fhj.itm.pswe.selenium;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestEdit {
	private static FirefoxDriver selenium;
	private String url;
	@Before
	public void setUp() throws Exception {
		selenium=new FirefoxDriver();
		selenium.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public void addSite(){
		url="http://koeckman.at/";
		selenium.get("http://localhost:8080/TermStatistics/");
		selenium.findElementByLinkText("New").click();
		selenium.findElement(By.id("DTE_Field_address")).sendKeys(url);
		selenium.findElement(By.id("DTE_Field_description")).sendKeys("Testseite");
		selenium.findElement(By.id("DTE_Field_depth")).sendKeys("1");
		
		WebElement web=selenium.findElementByCssSelector("div.DTE_Form_Buttons > button.btn");
		web.click();
	}
	
	@Test
	public void testEditSite(){
		addSite();
		selenium.get("http://localhost:8080/TermStatistics/");
		selenium.findElementByXPath("//table[@id='site-list-table']/tbody/tr/td[2]").click();
		selenium.findElementByLinkText("Edit").click();
		selenium.findElement(By.id("DTE_Field_active_0")).click();
		selenium.findElementByCssSelector("div.DTE_Form_Buttons > button.btn").click();
		
		int startnumber;
		String checkamount=selenium.findElement(By.id("site-list-table")).getText();
		System.out.println(checkamount);
		String[] lines=checkamount.split("\n");
		startnumber=lines.length;
		System.out.println(startnumber);
		
String output=selenium.findElement(By.id("site-list-table")).getText();
		
		if(output.contains("true")){
			Assert.assertTrue(true);
		}else{
			Assert.assertFalse("The rigth details were not found!",true);
		}
		
	
	
	
	
	}
	


	@AfterClass
	public static void tearDown() throws Exception {
		selenium.get("http://localhost:8080/TermStatistics/rest/website/koeckman.at/delete");
		selenium.quit();
	}

}
