package json.schema.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import json.schema.bean.JsonSchema;
import json.schema.bean.JsonValue;

public class JsonFormatWriter_v2 {

	private BufferedWriter bw = null;

	private int spacesCnt = 0;
	
	private ArrayList<String> pathList = new ArrayList<String>();
	
	public JsonFormatWriter_v2(String path) {
		try {
			bw = new BufferedWriter(new FileWriter(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeJsonFile(JsonSchema jsonSchema) {
		try {
			writeJsonSchema(bw, jsonSchema, null);
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeJsonSchema(BufferedWriter bw, JsonSchema jsonSchema, String subKey) throws IOException {
		if (subKey == null) {
			writeLine(bw, "{");
		} else {
			pathList.add(subKey);
			writeLine(bw, "\"" + subKey + "\": {");
		}
		writeSubLine(bw, "");

		formatBaseAttribute(bw, subKey, "object");

		List<String> keyList = formatProperties(bw, jsonSchema);

		formatRequired(bw, keyList);
		
		writeParentLine(bw, "}");

		if(pathList.size() > 0)
		pathList.remove(pathList.size()-1);
	}

	private void formatBaseAttribute(BufferedWriter bw, String subKey, String type) throws IOException {

		String flag = pathList.size() >=2 ? pathList.get(pathList.size() -2) : "";
		
		if(subKey == null) {
			writeLine(bw, "\"definitions\": {},");
			writeLine(bw, "\"$schema\": \"http://json-schema.org/draft-07/schema#\",");
			writeLine(bw, "\"$id\": \"" + getId(null) + "\",");
			writeLine(bw, "\"title\": \"The Root Schema\",");
			// type
			writeLine(bw, "\"type\": \"" + type + "\",");
		} else if(flag.equals("properties")) {
			writeLine(bw, "\"$id\": \"" + getId(null) + "\",");
			writeLine(bw, "\"title\": \"The " + subKey + " Schema\",");
			// type
			writeLine(bw, "\"type\": \"" + type + "\",");
			
			if(type.equals("string")) {

				writeLine(bw, "\"default\": \"\",");
				writeLine(bw, "\"examples\": [");
				writeLine(bw, "	\"xxx\"");
				writeLine(bw, "],");
				writeLine(bw, "\"pattern\": \"^(.*)$\"");
				
			} else if (type.equals("integer")) {

				writeLine(bw, "\"default\": 0,");
				writeLine(bw, "\"examples\": [");
				writeLine(bw, "	123");
				writeLine(bw, "]");
			}
		} else {
			writeLine(bw, "\"$id\": \"" + getId(null) + "\",");
			writeLine(bw, "\"title\": \"The " + subKey + " Schema\",");
			// type
			writeLine(bw, "\"type\": \"" + type + "\",");
		}
		  
	}

	private void formatRequired(BufferedWriter bw, List<String> keyList) throws IOException {
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
		writeLine(bw, sb.toString());
	}

	private List<String> formatProperties(BufferedWriter bw, JsonSchema jsonSchema) throws IOException {
		List<String> keyList = new ArrayList<String>();


		pathList.add("properties");
		// properties
		writeLine(bw, "\"properties\": {");

		writeSubLine(bw, "");
		for (Map.Entry<String, JsonValue> subEntry : jsonSchema.getBaseKeyMap().entrySet()) {
			String key = subEntry.getKey();
			JsonValue value = subEntry.getValue();
			writeLine(bw, "\"" + key + "\": {");
			pathList.add(key);
			writeSubLine(bw, "");
			formatBaseAttribute(bw, key, value.getType());
//			writeSubLine(bw, "\"type\": \"" + value.getType() + "\"");
			writeParentLine(bw, "},");
			pathList.remove(pathList.size()-1);

			keyList.add(key);
		}
		for (Map.Entry<String, JsonSchema> subEntry : jsonSchema.getObjKeyMap().entrySet()) {
			JsonSchema value = subEntry.getValue();
			String key = subEntry.getKey();
			writeJsonSchema(bw, value, key);

			keyList.add(key);
		}

		for (Map.Entry<String, List<JsonValue>> subEntry : jsonSchema.getBaseArrayKeyMap().entrySet()) {
			List<JsonValue> subArrayKeyList = subEntry.getValue();
			String key = subEntry.getKey();
			writeJsonBaseArraySchema(bw, subArrayKeyList, key);

			keyList.add(key);
		}

		for (Map.Entry<String, List<JsonSchema>> subEntry : jsonSchema.getObjArrayKeyMap().entrySet()) {
			List<JsonSchema> subArrayKeyList = subEntry.getValue();
			String key = subEntry.getKey();
			writeJsonObjArraySchema(bw, subArrayKeyList, key);

			keyList.add(key);
		}

		writeParentLine(bw, "},");
		
		pathList.remove(pathList.size()-1);
		return keyList;
	}

	private void writeJsonBaseArraySchema(BufferedWriter bw, List<JsonValue> subArrayKeyList, String key)
			throws IOException {
		if (key == null) {
			writeLine(bw, "{");
		} else {
			pathList.add(key);
			writeLine(bw, "\"" + key + "\": {");
		}

		pathList.add("items");
		formatBaseAttribute(bw, "items", "array");

		writeLine(bw, "\"items\": {");
		writeSubLine(bw, "\"type\": \"string\"");
		writeParentLine(bw, "}");
		pathList.remove(pathList.size()-1);

		writeParentLine(bw, "}");
		pathList.remove(pathList.size()-1);
	}

	private void writeJsonObjArraySchema(BufferedWriter bw, List<JsonSchema> jsonSchema, String key)
			throws IOException {
		if (key == null) {
			writeLine(bw, "{");
		} else {
			pathList.add(key);
			writeLine(bw, "\"" + key + "\": {");
		}

		formatBaseAttribute(bw, key, "array");

		writeJsonSchema(bw, jsonSchema.get(0), "items");

		writeParentLine(bw, "}");
		pathList.remove(pathList.size()-1);
	}

	private void writeLine(BufferedWriter bw, String value) throws IOException {

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

	private void writeParentLine(BufferedWriter bw, String value) throws IOException {
		spacesCnt--;
		bw.write(getSpaces() + value);
		bw.newLine();
	}

	private void writeSubLine(BufferedWriter bw, String value) throws IOException {
		spacesCnt++;
		bw.write(getSpaces() + value);
		bw.newLine();
	}

	private void writeNewLine(BufferedWriter bw) throws IOException {
		bw.newLine();
	}
}
