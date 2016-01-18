package at.fhj.itm.pswe.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import junit.framework.Assert;

public class TestSearch {
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

	@Test
	public void testSearch() {
		addSite();
		selenium.get("http://itm13jenkins.redirectme.net:8080/TermStatistics/");
		selenium.findElementByCssSelector("input[type='search']").sendKeys("koeckman");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String output = selenium.findElement(By.id("site-list-table")).getText();
		System.out.println(output);
		if (output.contains("koeckman"))
			Assert.assertTrue(true);
		else
			Assert.assertFalse("nothing was found", true);
	}

	@Test
	public void testSearchWrongInput() {
		addSite();
		selenium.get("http://itm13jenkins.redirectme.net:8080/TermStatistics/");
		selenium.findElementByCssSelector("input[type='search']").sendKeys("blablabla1234567890blablabla");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String output = selenium.findElement(By.id("site-list-table")).getText();
		System.out.println(output);
		if (output.contains("No matching records found"))
			Assert.assertTrue(true);
		else
			Assert.assertFalse("something was found", true);
	}

	@After
	public void tearDown() throws Exception {
		selenium.get("http://itm13jenkins.redirectme.net:8080/TermStatistics/rest/website/koeckman.at/delete");
		selenium.quit();
	}

}
