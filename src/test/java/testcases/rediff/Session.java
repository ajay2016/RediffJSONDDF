package testcases.rediff;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import testbase.BaseTest;

public class Session extends BaseTest {
	
	@Test
	public void doLogin(ITestContext context) {
		
		//System.out.println("Logging in");
		app.log("Logging in");
		
		app.openBrowser("chrome");
		app.navigate("url");
		String url = app.validateTitle();
		Assert.assertEquals(url, "Indian stock markets: Login to Portfolio");
		app.type("username_id", "ashishthakur1983");
		//app.reportFailure("text incorrect", false);
		app.type("password_id", "pass@1234");
		//app.validateElementPresent("loginsubmit");
		app.click("login_submit_id");
		
	}
	
	@Test
	public void doLogout() {
		
		//System.out.println("Logout");
		app.log("Logout");
		
	}

}
