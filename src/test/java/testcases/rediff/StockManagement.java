package testcases.rediff;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testbase.BaseTest;

public class StockManagement extends BaseTest {

	@Test
	public void addStock(ITestContext context) {

		JSONObject data = (JSONObject) context.getAttribute("data");

		// data using context
		String companyName = (String) data.get("stockname");
		String selectionDate = (String) data.get("date");
		String stockQuantity = (String) data.get("quantity");
		String stockPrice = (String) data.get("price");

		app.log("Adding  " + stockQuantity + " of new Stock " + companyName);
		int quatityBeforeModification = app.findCurrentStockQuantity(companyName);

		// use to context for verification later
		context.setAttribute("quatityBeforeModification", quatityBeforeModification);

		app.click("addStock_id");
		app.type("addstockname_css", companyName);
		app.wait(1);
		app.clickEnterButton("addstockname_css");
		app.click("stockPurchaseDate_id");
		// app.selectDateFromCalendar(selectionDate);
		app.selectDate(selectionDate);

		app.type("addstockqty_id", stockQuantity);
		app.type("addstockprice_id", stockPrice);
		app.click("addStockButton_id");
		app.waitForPageToLoad();
		app.log("Stocks added successfully ");

	}

	// sell or buy existing stock
	@Parameters({ "action" })
	@Test
	public void modifyStock(String action, ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");

		// data using context
		String companyName = (String) data.get("stockname");
		String selectionDate = (String) data.get("date");
		String stockQuantity = (String) data.get("quantity");
		String stockPrice = (String) data.get("price");
		
		app.log("Selling " + stockQuantity + " of company " + companyName);
		int quatityBeforeModification = app.findCurrentStockQuantity(companyName);
		context.setAttribute("quatityBeforeModification", quatityBeforeModification);

		app.goToBuySell(companyName);
		// Parameters
		if (action.equals("sellstock"))
			app.selectByVisibleText("equityaction_id", "Sell");
		else
			app.selectByVisibleText("equityaction_id", "Buy");

		app.click("buySellCalendar_id");
		app.log("Selecting Date " + selectionDate);
		// app.selectDateFromCalendar(selectionDate);
		app.selectDate(selectionDate);
		app.type("buysellqty_id", stockQuantity);
		app.type("buysellprice_id", stockPrice);
		app.click("buySellStockButton_id");
		app.waitForPageToLoad();
		app.log("Stock Sold ");

	}

	// checks if stock is present in the table
	@Test
	public void verifyStockPresent(ITestContext context) {
		
		JSONObject data = (JSONObject) context.getAttribute("data");

		// data using context
		String companyName = (String) data.get("stockname");
		//String companyName = "Birla Corporation Ltd";
		int row = app.getRowNumWithCellData("stocktable_css", companyName);
		if (row == -1)
			app.reportFailure("Stock Not present " + companyName, true);

		app.log("Stock Found in list " + companyName);

	}

	// checks the stock quantity
	@Parameters({ "action" })
	@Test
	public void verifyStockQuantity(String action, ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");

		// data using context
		String companyName = (String) data.get("stockname");
		//String selectionDate = (String) data.get("date");
		String stockQuantity = (String) data.get("quantity");
		//String stockPrice = (String) data.get("price");

		app.log("Verifying stock quantity after action - " + action);
		// quantity after adding/selling stocks
		int quatityAfterModification = app.findCurrentStockQuantity(companyName);
		int modifiedquantity = Integer.parseInt(stockQuantity);
		int expectedModifiedQuantity = 0;

		// quantity before adding/selling stocks
		int quatityBeforeModification = (Integer) context.getAttribute("quatityBeforeModification");
		if (action.equals("addstock"))
			expectedModifiedQuantity = quatityAfterModification - quatityBeforeModification;
		else if (action.equals("sellstock"))
			expectedModifiedQuantity = quatityBeforeModification - quatityAfterModification;

		app.log("Old Stock Quantity " + quatityBeforeModification);
		app.log("New Stock Quantity " + quatityAfterModification);

		if (modifiedquantity != expectedModifiedQuantity)
			app.reportFailure("Quantity did not match", true);

		app.log("Stock Quantity Changed as per expected " + modifiedquantity);
	}

	@Test
	public void verifyStockAvgBuyPrice() {

	}

	// verifies the transaction history
	@Parameters({ "action" })
	@Test
	public void verifyTransactionHistory(String action, ITestContext context) throws InterruptedException {
		JSONObject data = (JSONObject) context.getAttribute("data");

		// data using context
		String companyName = (String) data.get("stockname");
		//String selectionDate = (String) data.get("date");
		String stockQuantity = (String) data.get("quantity");
		//String stockPrice = (String) data.get("price");

		app.log("Verifying transaction History for " + action + "for quantity " + stockQuantity);
		app.goToTransactionHistory(companyName);
		String changedQuantityDisplayed = app.getText("latestShareChangeQuantity_xpath");

		app.log("Got Changed Quantity " + changedQuantityDisplayed);

		if (action.equals("sellstock"))
			stockQuantity = "-" + stockQuantity;

		if (!changedQuantityDisplayed.equals(stockQuantity))
			app.reportFailure("Got changed quantity in transaction history as " + changedQuantityDisplayed, true);

		app.log("Transaction History OK");
	}

}
