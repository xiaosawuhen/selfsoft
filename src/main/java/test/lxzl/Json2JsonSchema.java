package test.lxzl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Quota.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Json2JsonSchema {

	public static void main(String[] args) {
		new Json2JsonSchema().formatJsonSchema();
	}

	public void formatJsonSchema() {
		String content = getFileContent("/json");

		JSONObject jsonObject = JSONObject.parseObject(content);

		Map<String, Object> jsonKeyMap = getJsonKeyList(jsonObject);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("result"));
			writeJsonSchema(bw, jsonKeyMap, null);

			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeJsonSchema(BufferedWriter bw, Map<String, Object> jsonKeyMap, String key) throws IOException {
		if (key == null) {
			writeLine(bw, "{");
		} else {
			writeLine(bw, "\"" + key + "\": {");
		}

		// type
		writeSubLine(bw, "\"type\": \"object\",");

		Map<String, Object> jsonSubKeyMap = new HashMap<String, Object>();
		List<String> keyList = new ArrayList<String>();

		Map<String, Object> jsonSubArrayKeyMap = new HashMap<String, Object>();

		for (Map.Entry<String, Object> entry : jsonKeyMap.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Map) {
				jsonSubKeyMap.put(entry.getKey(), value);
			} else if (value instanceof List) {
				jsonSubArrayKeyMap.put(entry.getKey(), value);
			} else {
				keyList.add((String) value);
			}
		}

		// required
		StringBuffer sb = new StringBuffer();
		sb.append("\"required\": [");

		for (int i = 0; i < keyList.size(); i++) {
			if (i == keyList.size() - 1) {
				sb.append("\"" + keyList.get(i) + "\"");
			} else {
				sb.append("\"" + keyList.get(i) + "\",");
			}
		}

		sb.append("],");
		writeLine(bw, sb.toString());

		// properties
		writeLine(bw, "\"properties\": {");

		writeSubLine(bw, "");
		for (int i = 0; i < keyList.size(); i++) {
			writeLine(bw, "\"" + keyList.get(i) + "\": {");
			writeSubLine(bw, "\"type\": \"string\"");
			if (i == keyList.size() - 1) {
				writeParentLine(bw, "}");
			} else {
				writeParentLine(bw, "},");
			}
		}
		for (Map.Entry<String, Object> subEntry : jsonSubKeyMap.entrySet()) {
			Object value = subEntry.getValue();
			writeJsonSchema(bw, (Map<String, Object>) value, subEntry.getKey());
		}

		for (Map.Entry<String, Object> subArrayEntry : jsonSubArrayKeyMap.entrySet()) {
			List<Map<String, Object>> subArrayKeyList = (List<Map<String, Object>>) subArrayEntry.getValue();
			for (Map<String, Object> subArrayKey : subArrayKeyList) {
				writeJsonArraySchema(bw, subArrayKey, subArrayEntry.getKey());
			}
		}

		writeParentLine(bw, "}");
		writeParentLine(bw, "}");
	}

	private void writeJsonArraySchema(BufferedWriter bw, Map<String, Object> jsonKeyMap, String key)
			throws IOException {
		if (key == null) {
			writeLine(bw, "{");
		} else {
			writeLine(bw, "\"" + key + "\": {");
		}

		// type
		writeSubLine(bw, "\"type\": \"array\",");
		
		String type =  (String)jsonKeyMap.get("JsonArray_type");
		
		if(type != null && type.equals("obj")) {
			jsonKeyMap.remove("JsonArray_type");
			writeJsonSchema(bw, jsonKeyMap, "items");
		} else {
			jsonKeyMap.remove("JsonArray_type");
			writeLine(bw, "\"items\": {");
			writeSubLine(bw, "\"type\": \"string\"");
			writeParentLine(bw, "}");
		}

		writeParentLine(bw, "}");
	}

	private int spacesCnt = 0;

	public void write(BufferedWriter bw, String value) throws IOException {

		bw.write(getSpaces() + value);
	}

	public void writeLine(BufferedWriter bw, String value) throws IOException {

		bw.write(getSpaces() + value);
		bw.newLine();
	}

	private String getSpaces() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < spacesCnt; i++) {
			sb.append("	");
		}

		return sb.toString();
	}

	public void writeParentLine(BufferedWriter bw, String value) throws IOException {
		spacesCnt--;
		bw.write(getSpaces() + value);
		bw.newLine();
	}

	public void writeSubLine(BufferedWriter bw, String value) throws IOException {
		spacesCnt++;
		bw.write(getSpaces() + value);
		bw.newLine();
	}

	public void writeNewLine(BufferedWriter bw) throws IOException {
		bw.newLine();
	}

	private List<Map<String, Object>> getJsonArrayKeyList(JSONArray jsonArray) {
		List<Map<String, Object>> jsonArrayList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < jsonArray.size(); i++) {
			Object value = jsonArray.get(i);
			if (value instanceof JSONObject) {
				Map<String, Object> jsonObjKeyMap = getJsonKeyList((JSONObject) value);
				jsonObjKeyMap.put("JsonArray_type", "obj");
				jsonArrayList.add(jsonObjKeyMap);
				break;
			} else {
				Map<String, Object> jsonKeyMap = new HashMap<String, Object>();
				jsonKeyMap.put("JsonArray_type", "str");
				jsonKeyMap.put((String)value, (String)value);
				jsonArrayList.add(jsonKeyMap);
				break;
			}
		}

		return jsonArrayList;
	}

	private Map<String, Object> getJsonKeyList(JSONObject jsonObject) {

		Map<String, Object> jsonKeyMap = new HashMap<String, Object>();

		Set<String> keySet = jsonObject.keySet();
		for (String key : keySet) {
			Object value = jsonObject.get(key);

			if (value instanceof JSONObject) {
				Map<String, Object> subJsonKeyMap = getJsonKeyList(((JSONObject) value));
				jsonKeyMap.put(key, subJsonKeyMap);
			} else if (value instanceof JSONArray) {
				List<Map<String, Object>> jsonArrayKeyList = getJsonArrayKeyList(((JSONArray) value));
				jsonKeyMap.put(key, jsonArrayKeyList);
			} else {
				jsonKeyMap.put(key, key);
			}
		}

		return jsonKeyMap;
	}

	private String getFileContent(String path) {
		String result = "";

		InputStream is = Resource.class.getResourceAsStream(path);

		try {
			int size = is.available();

			byte[] contentArray = new byte[size];
			is.read(contentArray);
			result = new String(contentArray, Charset.forName("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
