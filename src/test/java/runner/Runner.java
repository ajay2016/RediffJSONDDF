package runner;

import java.util.ArrayList;
import java.util.List;

public class Runner {

	public static void main(String[] args) {
		
		TestNGRunner testng = new TestNGRunner(1);
		testng.createSuite("Stock Management", false);
		testng.addListener("listener.MyTestNGListener");
		testng.addTest("Add New Stock Test");
		testng.addTestParameters("action", "addstock");
		List<String> includedMethods = new ArrayList<>();
		includedMethods.add("selectPortFolio");
		testng.addTestClass("testcases.rediff.PortfolioManagement", includedMethods);
		//Reinitialize
		includedMethods = new ArrayList<>();
		includedMethods.add("addNewStock");
		includedMethods.add("verifyStockPresent");
		includedMethods.add("verifyStockQuantity");
		includedMethods.add("verifyTransactionHistory");
		testng.addTestClass("testcases.rediff.StockManagement", includedMethods);
		
		//run TestNG
		testng.run();
		
		

	}

}
