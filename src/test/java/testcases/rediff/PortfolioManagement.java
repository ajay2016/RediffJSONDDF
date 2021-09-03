package testcases.rediff;


import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import testbase.BaseTest;

public class PortfolioManagement extends BaseTest {
	
	@Test
	public void createPortfolio(ITestContext context) {
		
		String portfolioName ="Port_111111";	
		test.log(Status.INFO, "Creating Portfolio");
		app.click("create_portfolio_id");
		
		app.clear("portfolio_create_id");
		app.type("portfolio_create_id", portfolioName);
		app.click("portfolio_button_id");
		
		// verify selected value
		app.waitForPageToLoad();
		//app.waitTillSelectionDisplayed(portfolioName);
		app.validateSelectedValueInDropDown("portfolio_dropdown_id", portfolioName);
		
		app.assertAll();

		
	}
	
	@Test
	public void deletePortfolio() throws InterruptedException {
		
		String portfolioName ="Port_111111";
		test.log(Status.INFO, "Deleting Portfolio");
		app.log("Deleting Profolio");
        app.selectByVisibleText("portfolio_dropdown_id", portfolioName);
        app.waitForPageToLoad();
        app.click("deletePortfolio_id");
        app.acceptAlert();
       // app.waitForPageToLoad();
       // wait(5);
      //  app.validateSelectedValueNotInDropDown("portfolio_dropdown_id",portfolioName);
	}
	
	@Test
	public void selectPortFolio(ITestContext context) {
		
		//String portfolioName ="Port_111111";
		//JSONObject data = (JSONObject)context.getAttribute("data");
		//String portfolioName=(String)data.get("portfolioname");		
		app.log("Selecting Profolio");
		app.selectByVisibleText("portfolio_dropdown_id", "Cat");
        app.waitForPageToLoad();
	}

}
