package at.fhj.itm.pswe.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import junit.framework.Assert;

public class TestDetails {
	private FirefoxDriver selenium;
	private String url;

	@Before
	public void setUp() throws Exception {
		selenium = new FirefoxDriver();
		selenium.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public void addSite() {
		url = "http://koeckman.at/";
		selenium.get("http://itm13jenkins.redirectme.net:8080/TermStatistics/");
		selenium.findElementByLinkText("New").click();
		selenium.findElement(By.id("DTE_Field_address")).sendKeys(url);
		selenium.findElement(By.id("DTE_Field_description")).sendKeys("Testseite");
		selenium.findElement(By.id("DTE_Field_depth")).sendKeys("1");

		WebElement web = selenium.findElementByCssSelector("div.DTE_Form_Buttons > button.btn");
		web.click();
	}
	
	
	public void searchRightSite() {
		addSite();
		selenium.get("http://itm13jenkins.redirectme.net:8080/TermStatistics/");
		selenium.findElementByCssSelector("input[type='search']").sendKeys("koeckman");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testDetailsSite() {
		searchRightSite();
		selenium.findElementByLinkText("Details").click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String output = selenium.findElement(By.id("word-list-table")).getText();
		System.out.println(output);

		if (output.contains("World") && output.contains("inneren")) {
			Assert.assertTrue(true);
		} else {
			Assert.assertFalse("The rigth details were not found!", true);
		}
	}

//	@Test
	public void testDetailsWord() {
		selenium.get("http://itm13jenkins.redirectme.net:8080/TermStatistics/");
		// that the test always starts with the right element in the word tabel
		int startnumber;
		String checkamount = selenium.findElement(By.id("site-list-table")).getText();
		System.out.println(checkamount);
		String[] lines = checkamount.split("\n");
		startnumber = lines.length;
		System.out.println(startnumber + "!!!!!!!");
		selenium.findElementByXPath("(//a[contains(text(),'Details')])[" + startnumber + "]").click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String output = selenium.findElement(By.id("site-list-table")).getText();

		if (output.contains("http://pfiff.me/pswengi/")) {
			Assert.assertTrue(true);
		} else {
			Assert.assertFalse("The rigth details were not found!", true);
		}
	}

	@After
	public void tearDown() throws Exception {
		selenium.get("http://itm13jenkins.redirectme.net:8080/TermStatistics/rest/website/koeckman.at/delete");
		selenium.quit();
	}

}
