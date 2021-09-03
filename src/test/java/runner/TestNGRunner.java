package runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

public class TestNGRunner {
	
	/*
	TestNG - complete testng
	XmlSuite - test Suite
	XmlTest - test case under a suite
	XmlClass - class under test
	XmlInclude - include method under class 
*/ 

	TestNG testng;// TestNG
	XmlSuite suite;// Suite
	List<XmlSuite> allsuites;// all suite
	XmlTest test;// test cases
	List<XmlTest> testCases;// all test cases on suite
	Map<String, String> testParameters;// test parameters
	List<XmlClass> classes;
	
	

	public TestNGRunner(int suitThreadPoolSize) {
		allsuites = new ArrayList<XmlSuite>();//empty
		testng = new TestNG();
		testng.setSuiteThreadPoolSize(suitThreadPoolSize);
		testng.setXmlSuites(allsuites);//associates to testNG
	}

	//
	public void createSuite(String suiteName, boolean parallelTests) {
		
		suite = new XmlSuite();
		suite.setName(suiteName);
		if(parallelTests) {
			suite.setParallel(ParallelMode.TESTS);
		}
		
		//adding suite to all suites
		allsuites.add(suite);

	}

	public void addTest(String testName) {
		test = new XmlTest(suite);
		test.setName(testName);
		testParameters = new HashMap<String, String>();
		classes = new ArrayList<XmlClass>();
		test.setParameters(testParameters);
		test.setClasses(classes);

	}

	public void addTestParameters(String name, String value) {
		testParameters.put(name, value);

	}

	public void addTestClass(String className, List<String> includedMethodNames) {
		
		XmlClass testClass = new XmlClass(className);//test class name
		List<XmlInclude> classMethods = new ArrayList<XmlInclude>();
		
		//to maintain test order
		int priority = 1;
		//methods inside the class
		for(String methodName:includedMethodNames) {
			XmlInclude method = new XmlInclude(methodName, priority);
			classMethods.add(method);
			priority++;
			
		}
		//attach methods to class
		testClass.setIncludedMethods(classMethods);
		//attach class to classes
		classes.add(testClass);

	}

	public void addListener(String listenerFile) {
		
		suite.addListener("listener.MyTestNGListener");
		

	}
	
	public void run() {
		testng.run();
	}
}
