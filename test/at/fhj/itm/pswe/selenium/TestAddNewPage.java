package at.fhj.itm.pswe.selenium;

import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import junit.framework.Assert;

public class TestAddNewPage {
	private FirefoxDriver selenium;

	@Before
	public void setUp() throws Exception {
		selenium=new FirefoxDriver();
		selenium.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void testAddSite() throws Exception {
		selenium.get("http://localhost:8080/TermStatistics/");
		selenium.findElementByLinkText("New").click();
		selenium.findElement(By.id("DTE_Field_address")).sendKeys("http://rehcu.at");
		selenium.findElement(By.id("DTE_Field_description")).sendKeys("Testseite");
		selenium.findElement(By.id("DTE_Field_depth")).sendKeys("1");
		selenium.findElementByCssSelector("button.btn").click();
		
	//Assert.assertEquals(selenium.findElementsByXPath("//table[@id='site-list-table']/tbody/tr[2]/td[2]"), "http://rehcu.at");
	}

	@After
	public void tearDown() throws Exception {
		selenium.quit();
	}
}
