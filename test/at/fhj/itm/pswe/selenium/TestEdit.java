package at.fhj.itm.pswe.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestEdit {
	private FirefoxDriver selenium;
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
		WebElement web=selenium.findElementByCssSelector("button.btn");
		web.click();
	}
	
	@Test
	public void testEditSite(){
		addSite();
		selenium.get("http://localhost:8080/TermStatistics/");
		//TODO: ein Element aus der Tabelle wählen->auf Edit-> active änderen-> dann alles aus Tabelle ausgeben lassen und prüfen ob es beim richtigen umgestellt worden ist
	}

	@AfterClass
	public void tearDown() throws Exception {
		selenium.get("http://localhost:8080/TermStatistics/rest/website/koeckman.at/delete");
		selenium.quit();
	}

}
