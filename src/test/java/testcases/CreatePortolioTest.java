package testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import keywords.ApplicationKeywords;

public class CreatePortolioTest {

	@Test
	public void createPortfolioTest() {

		ApplicationKeywords app = new ApplicationKeywords();
		app.openBrowser("chrome");
		app.navigate("url");
		String url = app.validateTitle();
		Assert.assertEquals(url, "Indian stock markets: Login to Portfolio");
		app.type("username_id", "ashishthakur1983");
		app.type("password_id", "pass@1234");
		//app.validateElementPresent("loginsubmit");
		app.click("login_submit_id");
		app.click("create_portfolio_id");
		app.clear("portfolio_create_id");
		app.type("portfolio_create_id", "Portf_12345678");
		app.click("portfolio_button_id");
		
		// verify selected value
		app.waitForPageToLoad();
		app.waitTillSelectionDisplayed("Portf_12345678");

	}

}
