package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataUtil {

	public Map<String, String> loadClassMethods() throws FileNotFoundException, IOException, ParseException {
		// Class name and method should be loaded first as a map
		Map<String, String> classMethodMap = new HashMap<String, String>();

		// TO Read JSON json simple jar needed--- json file path
		String path = System.getProperty("user.dir") + "\\src\\test\\resources\\jsons\\classmethods.json";
		// To parse Json

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(path)));
		// System.out.println(json.toString());
		JSONArray classDetails = (JSONArray) json.get("classdetails");
		System.out.println(classDetails.toJSONString());
		for (int i = 0; i < classDetails.size(); i++) {
			JSONObject classDetail = (JSONObject) classDetails.get(i);
			// System.out.println(classDetail);
			String className = (String) classDetail.get("class");
			// System.out.println(className);
			// System.out.println("------------------------");
			JSONArray methodName = (JSONArray) classDetail.get("methods");
			// System.out.println(methodName);
			for (int j = 0; j < methodName.size(); j++) {
				String method = (String) methodName.get(j);
				// System.out.println(method);
				classMethodMap.put(method, className);

			}
			// System.out.println("***********************");
		}
		System.out.println(classMethodMap);
		return classMethodMap;
	}

	public int getTestDataSets(String filePath, String dataFlag)
			throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(filePath)));
		JSONArray testDataSets = (JSONArray) json.get("testdata");
		for (int dataId = 0; dataId < testDataSets.size(); dataId++) {
			JSONObject testdata = (JSONObject) testDataSets.get(dataId);
			String flag = (String) testdata.get("flag");
			if (dataFlag.equals(flag)) {
				JSONArray dataSets = (JSONArray) testdata.get("data");
				return dataSets.size();
			}
		}
		return -1;

	}
	
	public JSONObject getTestData(String filePath, String dataFlag,int iteration) throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(new FileReader(new File(filePath)));
		JSONArray testDataSets = (JSONArray) json.get("testdata");
		for (int dataId = 0; dataId < testDataSets.size(); dataId++) {
			JSONObject testdata = (JSONObject) testDataSets.get(dataId);
			String flag = (String) testdata.get("flag");
			if (dataFlag.equals(flag)) {
				JSONArray dataSets = (JSONArray) testdata.get("data");
				JSONObject data = (JSONObject) dataSets.get(iteration);
				return data;
			}
		
	}
		return null;

}
}
