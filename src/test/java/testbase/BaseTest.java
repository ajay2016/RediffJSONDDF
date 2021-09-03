package testbase;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import extreports.ExtentManager;
import keywords.ApplicationKeywords;
import runner.DataUtil;

public class BaseTest {

	// needs 3 objects in every test
	public ApplicationKeywords app;
	public ExtentReports rep;
	public ExtentTest test;

	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext context) throws NumberFormatException, FileNotFoundException, IOException, ParseException {

		System.out.println("*********Before Test**********");
		String dataFilePath = context.getCurrentXmlTest().getParameter("dataFilePath");
		String dataFlag = context.getCurrentXmlTest().getParameter("dataflag");
		String iterations = context.getCurrentXmlTest().getParameter("Iterations");
		//System.out.println(dataFilePath);
		//System.out.println(dataFlag);
		//System.out.println(iterations);
		JSONObject data = (JSONObject) new DataUtil().getTestData(dataFilePath, dataFlag, Integer.parseInt(iterations));
		//set data on context to share
		context.setAttribute("data", data);
		String runmode = (String) data.get("runmode");

		app = new ApplicationKeywords();
		

		// Initialize report
		rep = ExtentManager.getReports();
		// name of the test form xml
		test = rep.createTest(context.getCurrentXmlTest().getName());
		test.log(Status.INFO, "Starting Test  " + context.getCurrentXmlTest().getName());
		test.log(Status.INFO, " Data"+data.toJSONString());
		// report and test object sharing
				context.setAttribute("report", rep);
				context.setAttribute("test", test);
		
		if(!runmode.equals("Y")) {
			test.log(Status.SKIP, "Skipping the Test since Runmode is N");
			throw new SkipException("Skipping the Test since Runmode is N");
			
		}

		// Intialize report on application class to set test object
		app.setReport(test);

		// report and test object sharing
		context.setAttribute("report", rep);
		context.setAttribute("test", test);

		app.openBrowser("chrome");
		app.defaultLogin();
		context.setAttribute("app", app);

	}

	@BeforeMethod(alwaysRun = true)

	public void beforeMethod(ITestContext context) {

		System.out.println("*********Before Method***********");

		// Before running method we retrieve app, rep and test from Before Test
		
		test = (ExtentTest) context.getAttribute("test");

		// to stop method on critical failure
		String criticalFailure = (String) context.getAttribute("criticalFailure");

		if (criticalFailure != null && criticalFailure.equalsIgnoreCase("Y")) {

			test.log(Status.SKIP, "Critical failure in prevoius Tests");// Extent Reports
			throw new SkipException("Critical failure in prevoius Tests");// TestNG

		}
		app = (ApplicationKeywords) context.getAttribute("app");
		rep = (ExtentReports) context.getAttribute("report");

	}

	@AfterTest(alwaysRun = true)
	public void quit(ITestContext context) {

		app = (ApplicationKeywords) context.getAttribute("app");
		if (app != null) {
			app.quit();
		}
		rep = (ExtentReports) context.getAttribute("report");
		if (rep != null) {

			rep.flush();
		}

	}

}
