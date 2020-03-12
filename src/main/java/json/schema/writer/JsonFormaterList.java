package json.schema.writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import json.schema.bean.JsonSchema;
import json.schema.bean.JsonValue;

public class JsonFormaterList {

	private int spacesCnt = 0;
	
	private ArrayList<String> pathList = new ArrayList<String>();
	private ArrayList<String> formatedList = new ArrayList<String>();
	
	public ArrayList<String> getFormatedList(JsonSchema jsonSchema) {
		try {
			writeJsonSchema(jsonSchema, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return formatedList;
	}

	private void writeJsonSchema(JsonSchema jsonSchema, String subKey) throws IOException {
		if (subKey == null) {
			writeLine("{");
		} else {
			pathList.add(subKey);
			writeLine("\"" + subKey + "\": {");
		}
		writeSubLine("");

		formatBaseAttribute(subKey, new JsonValue("object"));

		List<String> keyList = formatProperties(jsonSchema);

		formatRequired(keyList);
		
		writeParentLine("}");

		if(pathList.size() > 0)
		pathList.remove(pathList.size()-1);
	}

	private void formatBaseAttribute(String subKey, JsonValue jsonValue) throws IOException {

		String flag = pathList.size() >=2 ? pathList.get(pathList.size() -2) : "";
		
		if(subKey == null) {
			writeLine("\"definitions\": {},");
			writeLine("\"$schema\": \"http://json-schema.org/draft-07/schema#\",");
			writeLine("\"$id\": \"" + getId(null) + "\",");
			writeLine("\"title\": \"The Root Schema\",");
			// type
			writeLine("\"type\": \"" + jsonValue.getType() + "\",");
		} else if(flag.equals("properties")) {
			writeLine("\"$id\": \"" + getId(null) + "\",");
			writeLine("\"title\": \"The " + subKey + " Schema\",");
			// type
			writeLine("\"type\": \"" + jsonValue.getType() + "\",");
			
			if(jsonValue.getType().equals("string")) {

				writeLine("\"default\": \"\",");
				writeLine("\"examples\": [");
				writeLine("	\"" + jsonValue.getValue() + "\"");
				writeLine("],");
				writeLine("\"pattern\": \"^(.*)$\"");
				
			} else if (jsonValue.getType().equals("integer")) {

				writeLine("\"default\": 0,");
				writeLine("\"examples\": [");
				writeLine("	" + jsonValue.getValue());
				writeLine("]");
			}
		} else {
			writeLine("\"$id\": \"" + getId(null) + "\",");
			writeLine("\"title\": \"The " + subKey + " Schema\",");
			// type
			writeLine("\"type\": \"" + jsonValue.getType() + "\",");
		}
		  
	}

	private void formatRequired(List<String> keyList) throws IOException {
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

		sb.append("]");
		writeLine(sb.toString());
	}

	private List<String> formatProperties(JsonSchema jsonSchema) throws IOException {
		List<String> keyList = new ArrayList<String>();


		pathList.add("properties");
		// properties
		writeLine("\"properties\": {");

		writeSubLine("");
		for (Map.Entry<String, JsonValue> subEntry : jsonSchema.getBaseKeyMap().entrySet()) {
			String key = subEntry.getKey();
			JsonValue value = subEntry.getValue();
			writeLine("\"" + key + "\": {");
			pathList.add(key);
			writeSubLine("");
			formatBaseAttribute(key, value);
//			writeSubLine("\"type\": \"" + value.getType() + "\"");
			writeParentLine("},");
			pathList.remove(pathList.size()-1);

			keyList.add(key);
		}
		
		boolean baseOnly = true;
		
		for (Map.Entry<String, JsonSchema> subEntry : jsonSchema.getObjKeyMap().entrySet()) {
			JsonSchema value = subEntry.getValue();
			String key = subEntry.getKey();
			writeJsonSchema(value, key);

			baseOnly = false;
			keyList.add(key);
		}

		for (Map.Entry<String, List<JsonValue>> subEntry : jsonSchema.getBaseArrayKeyMap().entrySet()) {
			List<JsonValue> subArrayKeyList = subEntry.getValue();
			String key = subEntry.getKey();
			writeJsonBaseArraySchema(subArrayKeyList, key);

			baseOnly = false;
			keyList.add(key);
		}

		for (Map.Entry<String, List<JsonSchema>> subEntry : jsonSchema.getObjArrayKeyMap().entrySet()) {
			List<JsonSchema> subArrayKeyList = subEntry.getValue();
			String key = subEntry.getKey();
			writeJsonObjArraySchema(subArrayKeyList, key);

			baseOnly = false;
			keyList.add(key);
		}
		
		if(baseOnly) {
			System.out.println("the lasted base key exist !!");
			String lastedContent = formatedList.get(formatedList.size() - 1);
			lastedContent = lastedContent.substring(0, lastedContent.length() - 1);
			formatedList.set(formatedList.size() - 1, lastedContent);
		}

		writeParentLine("},");
		
		pathList.remove(pathList.size()-1);
		return keyList;
	}

	private void writeJsonBaseArraySchema(List<JsonValue> subArrayKeyList, String key)
			throws IOException {
		if (key == null) {
			writeLine("{");
		} else {
			pathList.add(key);
			writeLine("\"" + key + "\": {");
		}

		pathList.add("items");
		formatBaseAttribute("items", new JsonValue("array"));

		writeLine("\"items\": {");
		writeSubLine("\"type\": \"string\"");
		writeParentLine("}");
		pathList.remove(pathList.size()-1);

		writeParentLine("}");
		pathList.remove(pathList.size()-1);
	}

	private void writeJsonObjArraySchema(List<JsonSchema> jsonSchema, String key)
			throws IOException {
		if (key == null) {
			writeLine("{");
		} else {
			pathList.add(key);
			writeLine("\"" + key + "\": {");
		}

		formatBaseAttribute(key, new JsonValue("array"));

		writeJsonSchema(jsonSchema.get(0), "items");

		writeParentLine("}");
		pathList.remove(pathList.size()-1);
	}

	private void writeLine(String value) throws IOException {
		formatedList.add(getSpaces() + value);
	}

	private String getSpaces() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < spacesCnt; i++) {
			sb.append("	");
		}

		return sb.toString();
	}
	
	private String getId(String key) {
		
		if(pathList.isEmpty()) {
			return "http://example.com/root.json";
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("#");
			for (String path : pathList) {
				sb.append("/");
				sb.append(path);
			}
			if(key != null && !key.isEmpty()) {
				sb.append("/");
				sb.append(key);
			}
			
			return sb.toString();
		}
	}

	private void writeParentLine(String value) throws IOException {
		spacesCnt--;
		formatedList.add(getSpaces() + value);
	}

	private void writeSubLine(String value) throws IOException {
		spacesCnt++;
		formatedList.add(getSpaces() + value);
	}
}
