package at.fhj.itm.pswe.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import junit.framework.Assert;

public class TestDetails {
	private FirefoxDriver selenium;

	@Before
	public void setUp() throws Exception {
		selenium = new FirefoxDriver();
		selenium.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void testDetailsSite() {
		selenium.get("http://itm13jenkins.redirectme.net:8080/TermStatistics/");
		// clicks always the detail button of the first website
		// (http://pfiff.me/pswengi)
		selenium.findElementByLinkText("Details").click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String output = selenium.findElement(By.id("word-list-table")).getText();
		System.out.println(output);

		if (output.contains("testseite") && output.contains("seite")) {
			Assert.assertTrue(true);
		} else {
			Assert.assertFalse("The rigth details were not found!", true);
		}
	}

	@Test
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
		selenium.quit();
	}

}
