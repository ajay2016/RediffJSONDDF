package keywords;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import extreports.ExtentManager;



public class GenericKeywords {

	public WebDriver driver;
	public Properties prop;
	public Properties envProp;
	public ExtentTest test;
	public SoftAssert softAssert;
	public String screenshotName;

	public void openBrowser(String browserName) {

		log("Opening a browser  " + browserName);
		
		//To run on grid
		if(prop.get("grid_run").equals("Y")) {
			//remote web driver
			DesiredCapabilities cap=new DesiredCapabilities();
			if(browserName.equals("Mozilla")){
				
				cap.setBrowserName("firefox");
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				
			}else if(browserName.equals("chrome")){
				 cap.setBrowserName("chrome");
				 cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			
			try {
				// hit the hub for GRID 3
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
				//driver = new RemoteWebDriver(new URL("http://localhost:4444"), cap);
			} catch (Exception e) {
			  e.printStackTrace();
			}
		}
			
			
			
		else {  //local machine

		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
			System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "null");
			ChromeOptions options = new ChromeOptions();
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			driver = new ChromeDriver(options);

		} else if (browserName.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\drivers\\geckodriver.exe");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "null");
			driver = new FirefoxDriver();

		} else if (browserName.equals("ie")) {
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\drivers\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();

		} else if (browserName.equals("Edge")) {
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "\\drivers\\msedgedriver.exe");
			System.setProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY, "null");
			System.setProperty(EdgeDriverService.EDGE_DRIVER_LOG_PROPERTY, "null");
			driver = new EdgeDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}

	public void navigate(String urlKey) {

		log("Navigating to   " + urlKey);
		driver.navigate().to(envProp.getProperty(urlKey));

	}

	public void type(String locatorKey, String data) {

		log("Typing data on " + locatorKey + "with the value " + data);
		getElement(locatorKey).sendKeys(data);

	}

	public void click(String locatorKey) {

		log("Clicking on " + locatorKey);
		// navigate according to environment
		getElement(locatorKey).click();

	}

	public String getText(String locatorKey) {
		return getElement(locatorKey).getText();
	}
	
	
	public void clickEnterButton(String locatorKey) {
		log("Clinking enter button");
		getElement(locatorKey).sendKeys(Keys.ENTER);
	}

	public void clear(String locatorKey) {
		getElement(locatorKey).clear();

	}

	public void selectByVisibleText(String locatorKey, String data) {
		Select s = new Select(getElement(locatorKey));
		s.selectByVisibleText(data);
	}

	public WebElement getElement(String locatorKey) {
		// Element present
		if (!isElementPresent(locatorKey)) {
			// System.out.println("Element not Present");
			// Report failure Critical
			reportFailure("Element not present " + locatorKey, true);
		}
		// Element Visible
		if (!isElementVisible(locatorKey)) {
			// System.out.println("Element not visible");
			reportFailure("Element not visible " + locatorKey, true);

		}
		WebElement e = driver.findElement(getLocator(locatorKey));
		/*
		 * try {
		 * 
		 * if (locatorKey.endsWith("_id")) { e =
		 * driver.findElement(By.id(prop.getProperty(locatorKey))); } else if
		 * (locatorKey.endsWith("_name")) { e =
		 * driver.findElement(By.name(prop.getProperty(locatorKey))); } else if
		 * (locatorKey.endsWith("_xapth")) { e =
		 * driver.findElement(By.xpath(prop.getProperty(locatorKey)));
		 * 
		 * } else if (locatorKey.endsWith("_css")) { e =
		 * driver.findElement(By.cssSelector(prop.getProperty(locatorKey)));
		 * 
		 * } else { Assert.fail("Locator is not correct  " + locatorKey); } } catch
		 * (Exception e1) { // TODO Auto-generated catch block e1.printStackTrace(); }
		 */

		return e;
	}

	public boolean isElementVisible(String locatorKey) {

		log("Checking visibility of  " + locatorKey);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locatorKey)));
		} catch (Exception e) {
			return false;

		}
		return true;

	}

	public boolean isElementPresent(String locatorKey) {

		log("Checking presence of  " + locatorKey);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locatorKey)));
		} catch (Exception e) {

			return false;

		}
		return true;

	}

	public void wait(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public By getLocator(String locatorKey) {
		By by = null;

		try {
			if (locatorKey.endsWith("_id")) {
				by = By.id(prop.getProperty(locatorKey));
			} else if (locatorKey.endsWith("_name")) {
				by = By.name(prop.getProperty(locatorKey));

			} else if (locatorKey.endsWith("_xapth")) {
				by = By.xpath(prop.getProperty(locatorKey));

			} else if (locatorKey.endsWith("_css")) {
				by = By.cssSelector(prop.getProperty(locatorKey));

			}
		} catch (Exception e) {
			Assert.fail("Locator is not present " + locatorKey);
			reportFailure("Locator is not present " + locatorKey, true);
			e.printStackTrace();
		}

		return by;
	}

	// Reporting function avoid writing test.log all the time
	public void log(String msg) {

		test.log(Status.INFO, msg);

	}

	public void reportFailure(String failureMsg, boolean stopOnFailure) {

		System.out.println(failureMsg);
		test.log(Status.FAIL, failureMsg);// Extent reports
		takeScreenShot();
		softAssert.fail(failureMsg);// TestNG reports

		if (stopOnFailure) {

			// To stop the test on critical failure we need flag of Y
			Reporter.getCurrentTestResult().getTestContext().setAttribute("criticalFailure", "Y");
			assertAll();// stops tests
		}

	}

	// to get all failure messages
	public void assertAll() {

		softAssert.assertAll();
	}

	public void takeScreenShot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// take screenshot
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			// copy File object to location
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + "//" + screenshotFile));
			// put screenshot file in reports
			test.log(Status.INFO, "Screenshot-> "
					+ test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath + "//" + screenshotFile));
			// test.log(Status.FAIL,"Screenshot->
			// "+MediaEntityBuilder.createScreenCaptureFromPath(ExtentManager.screenshotFolderPath).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// For Extent reports different version
	public void captureScreenshot() {

		// File scrFile = ((TakesScreenshot)
		// DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		Date d = new Date();
		screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "\\reports\\" + screenshotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void waitForPageToLoad() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int i = 0;

		while (i != 10) {
			String state = (String) js.executeScript("return document.readyState;");
			System.out.println(state);

			if (state.equals("complete"))
				break;
			else
				wait(2);

			i++;
		}
		// check for jquery status
		i = 0;
		while (i != 10) {

			Long d = (Long) js.executeScript("return jQuery.active;");
			System.out.println(d);
			if (d.longValue() == 0)
				break;
			else
				wait(2);
			i++;

		}

	}

	public void waitTillSelectionDisplayed(String expected) {
		int i = 0;
		while (i != 10) {
			WebElement e = driver.findElement(By.id("portfolioid"));
			Select s = new Select(e);
			String actual = s.getFirstSelectedOption().getText();
			System.out.println(actual);
			if (actual.equals(expected))
				return;
			else
				wait(1);
			i++;
		}
		// reach here
		Assert.fail("Value never changed in Select box");

	}
	
	public void acceptAlert(){
		test.log(Status.INFO, "Switching to alert");
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.alertIsPresent());
		try{
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			test.log(Status.INFO, "Alert accepted successfully");
		}catch(Exception e){
				reportFailure("Alert not found when mandatory",true);
		}
		
	}
	
	// finds the row number of the data
		public int getRowNumWithCellData(String tableLocator, String data) {
			//gives table
			WebElement table = getElement(tableLocator);
			//all rows
			List<WebElement> rows = table.findElements(By.tagName("tr"));
			for(int rNum=0;rNum<rows.size();rNum++) {
				//first row
				WebElement row = rows.get(rNum);
				List<WebElement> cells = row.findElements(By.tagName("td"));
				for(int cNum=0;cNum<cells.size();cNum++) {
					WebElement cell = cells.get(cNum);
					System.out.println("Text "+ cell.getText());
					if(!cell.getText().trim().equals(""))
						if(data.startsWith(cell.getText()))
							return(rNum+1);
				}
			}
			
			return -1; // data is not found
		}
	
	//used in @AfterTest by app	
	public void quit() {
		driver.quit();
		
	}

}
