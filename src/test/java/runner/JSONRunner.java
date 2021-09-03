package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONRunner {

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

		// 1.First load classMethods
		Map<String, String> classMethods = new DataUtil().loadClassMethods();
		// to read testconfig.json
		String path = System.getProperty("user.dir") + "\\src\\test\\resources\\jsons\\testconfig.json";
		// To parse Json

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(path)));
		// System.out.println(json.toJSONString());
		String parallelSuites = (String) json.get("parallelsuites");
		// TestNG
		TestNGRunner testng = new TestNGRunner(Integer.parseInt(parallelSuites));
		//testng.addListener("listener.MyTestNGListener");
		JSONArray testSuites = (JSONArray) json.get("testsuites");

		for (int i = 0; i < testSuites.size(); i++) {

			JSONObject testSuite = (JSONObject) testSuites.get(i);
			String runMode = (String) testSuite.get("runmode");
			if (runMode.equals("Y")) {
				String name = (String) testSuite.get("name");
				String testdatajsonfile = System.getProperty("user.dir") + "\\src\\test\\resources\\jsons\\"+(String) testSuite.get("testdatajsonfile");
				// String testdataxlsfile = (String) testSuite.get("testdataxlsfile");
				String suitefilename = (String) testSuite.get("suitefilename");
				// System.out.println(suitefilename);
				String paralleltests = (String) testSuite.get("paralleltests");

				System.out.println(runMode + "---------" + name);
				// Create Suite...Check Parallel tests
				boolean pTests = false;
				if (paralleltests.equals("Y"))
					pTests = true;
				testng.createSuite(name, pTests);
				//Listner after adding test suites
				testng.addListener("listener.MyTestNGListener");

				// Start adding tests
				// to read stocksuite.json
				String pathSuiteJSON = System.getProperty("user.dir") + "\\src\\test\\resources\\jsons\\"
						+ suitefilename;
				JSONParser suiteParser = new JSONParser();
				JSONObject suiteJSON = (JSONObject) suiteParser.parse(new FileReader(new File(pathSuiteJSON)));
				// System.out.println(suiteJSON);
				JSONArray testcases = (JSONArray) suiteJSON.get("testcases");

				for (int testId = 0; testId < testcases.size(); testId++) {
					// inside array JSON Object
					JSONObject suiteTestcases = (JSONObject) testcases.get(testId);
					// System.out.println(testcase);
					String tname = (String) suiteTestcases.get("name");
					JSONArray parameteres = (JSONArray) suiteTestcases.get("parameternames");
					JSONArray executions = (JSONArray) suiteTestcases.get("executions");
					for (int eId = 0; eId < executions.size(); eId++) {
						JSONObject suitetestcase = (JSONObject) executions.get(eId);
						String runmode = (String) suitetestcase.get("runmode");
						if(runmode !=null && runmode.equals("Y")) {
						String executionname = (String) suitetestcase.get("executionname");
						String dataflag=(String)suitetestcase.get("dataflag");
						int dataSets = new DataUtil().getTestDataSets(testdatajsonfile, dataflag);
						for(int dSId = 0; dSId < dataSets; dSId++) {
						//testng.addTestParameters("dataflag", dataflag);
						// System.out.println("Test Cases with their Runmode");
						 System.out.println(dataflag + "---------------" + executionname);

						// parametervalues
						JSONArray parametervalues = (JSONArray) suitetestcase.get("parametervalues");
						System.out.println(parametervalues);
						// for (int pvalues = 0; pvalues < parametervalues.size(); pvalues++) {

						// String parametervalue = (String) parametervalues.get(pvalues);
						// System.out.println("parametervalues is " + parametervalue);

						// }
						// methods
						JSONArray methods = (JSONArray) suitetestcase.get("methods");
						System.out.println(methods);
						// System.out.println("*****************************");
						// for (int mId = 0; mId < methods.size(); mId++) {

						// String method = (String) methods.get(mId);
						// System.out.println("Method is " + method);

						// }
						// <test name="Add New Stock Test">
						testng.addTest(tname+" "+ executionname+"  "+"Iterations   "+(dSId+1));
						// <parameter name="action" value="addstock" />
						// choose parameters or parametersvalue to iterate
						for (int pId = 0; pId < parameteres.size(); pId++) {

							testng.addTestParameters((String) parameteres.get(pId), (String) parametervalues.get(pId));

						}
						//Parameters for data and Iterations
						testng.addTestParameters("dataFilePath", testdatajsonfile);
						testng.addTestParameters("dataflag", dataflag);
						testng.addTestParameters("Iterations", String.valueOf(dSId));

						// To include method name..empty list
						List<String> includedMethods = new ArrayList<String>();
						for (int mId = 0; mId < methods.size(); mId++) {
							// extract method name and method class
							String method = (String) methods.get(mId);
							String methodClass = classMethods.get(method);
							// System.out.println(methodClass+"____________" + method);
							// To check method belongs to different class

							if(mId==methods.size()-1 || !((String)classMethods.get((String)methods.get(mId+1))).equals(methodClass)) {
								// next method is from different class
								includedMethods.add(method);
								testng.addTestClass(methodClass, includedMethods);
								includedMethods = new ArrayList<String>();
								
							} else {
								// same class
								includedMethods.add(method);
							}

						}
						}
					}
					}
				}
					
			}
		}
		testng.run();
	}
}
