package json.schema.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import json.schema.bean.JsonSchema;
import json.schema.bean.JsonValue;

public class JsonFormatWriter {

	private BufferedWriter bw = null;

	private int spacesCnt = 0;

	public JsonFormatWriter(String path) {
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
			writeLine(bw, "\"" + subKey + "\": {");
		}

		// type
		writeSubLine(bw, "\"type\": \"object\",");

		List<String> keyList = new ArrayList<String>();

		// properties
		writeLine(bw, "\"properties\": {");

		writeSubLine(bw, "");
		for (Map.Entry<String, JsonValue> subEntry : jsonSchema.getBaseKeyMap().entrySet()) {
			String key = subEntry.getKey();
			JsonValue value = subEntry.getValue();
			writeLine(bw, "\"" + key + "\": {");
			writeSubLine(bw, "\"type\": \"" + value.getType() + "\"");
			writeParentLine(bw, "},");

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
			writeJsonBaseArraySchema(bw, subArrayKeyList, subKey);

			keyList.add(key);
		}

		for (Map.Entry<String, List<JsonSchema>> subEntry : jsonSchema.getObjArrayKeyMap().entrySet()) {
			List<JsonSchema> subArrayKeyList = subEntry.getValue();
			String key = subEntry.getKey();
			writeJsonObjArraySchema(bw, subArrayKeyList, key);

			keyList.add(key);
		}

		writeParentLine(bw, "},");

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
		writeParentLine(bw, "}");
	}

	private void writeJsonBaseArraySchema(BufferedWriter bw, List<JsonValue> subArrayKeyList, String key)
			throws IOException {
		if (key == null) {
			writeLine(bw, "{");
		} else {
			writeLine(bw, "\"" + key + "\": {");
		}

		// type
		writeSubLine(bw, "\"type\": \"array\",");

		writeLine(bw, "\"items\": {");
		writeSubLine(bw, "\"type\": \"string\"");
		writeParentLine(bw, "}");

		writeParentLine(bw, "}");
	}

	private void writeJsonObjArraySchema(BufferedWriter bw, List<JsonSchema> jsonSchema, String key)
			throws IOException {
		if (key == null) {
			writeLine(bw, "{");
		} else {
			writeLine(bw, "\"" + key + "\": {");
		}

		// type
		writeSubLine(bw, "\"type\": \"array\",");

		writeJsonSchema(bw, jsonSchema.get(0), "items");

		writeParentLine(bw, "}");
	}

	private void write(BufferedWriter bw, String value) throws IOException {

		bw.write(getSpaces() + value);
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
