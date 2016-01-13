package at.fhj.itm.pswe.selenium;

import at.fhj.itm.pswe.dao.WebsiteDao;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.rest.WebsiteEndpoint;
import at.fhj.itm.pswe.rest.WordEndpoint;

import com.sun.syndication.feed.atom.Content;
import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import junit.framework.Assert;

public class TestAddNewPage {
	private FirefoxDriver selenium;
	private String url;

	@Before
	public void setUp() throws Exception {
		selenium=new FirefoxDriver();
		selenium.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public void addSite(){
		url="http://rehcu.at";
		selenium.get("http://localhost:8080/TermStatistics/");
		selenium.findElementByLinkText("New").click();
		selenium.findElement(By.id("DTE_Field_address")).sendKeys(url);
		selenium.findElement(By.id("DTE_Field_description")).sendKeys("Testseite");
		selenium.findElement(By.id("DTE_Field_depth")).sendKeys("1");
		WebElement web=selenium.findElementByCssSelector("button.btn");
		web.click();
	}

	@Test
	public void testAddSite() throws Exception {
		addSite();
		Thread.sleep(1000);
		String output=selenium.findElement(By.id("site-list-table")).getText();
		System.out.println(output);
		if(output.contains(url)){
			Assert.assertTrue(true);
		}else{
			Assert.assertFalse("Site was not added",true);
		}
	}
	
	@Test
	public void testAddSameSite() throws Exception {
		addSite();
		addSite();
		WebElement element = (new WebDriverWait(selenium, 10)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.DTE_Form_Error")));
		WebElement error=selenium.findElementByCssSelector("div.DTE_Form_Error");
		Assert.assertEquals(error.getText(),"Website already in the Database!");
	}

	@After
	public void tearDown() throws Exception {
		selenium.get("http://localhost:8080/TermStatistics/rest/website/rehcu.at/delete");
		selenium.quit();
	}
}
