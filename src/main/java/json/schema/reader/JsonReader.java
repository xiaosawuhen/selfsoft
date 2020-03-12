package json.schema.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import json.schema.bean.JsonSchema;
import json.schema.bean.JsonValue;

public class JsonReader {

	public JsonSchema getJsonBean(String path) {
		String fileContent = getFileContent(path);
//		System.out.println(fileContent);
		JSONObject obj = JSONObject.parseObject(fileContent);
		JsonSchema jsonBean = getJsonSchema(obj);

		return jsonBean;
	}

	private void getJsonArrayKeyList(JsonSchema jsonSchema, String arrayKey, JSONArray jsonArray) {

		for (int i = 0; i < 1; i++) {
			Object value = jsonArray.get(i);
			if (value instanceof JSONObject) {
				JsonSchema objKey = getJsonSchema((JSONObject) value);
				List<JsonSchema> objKeyList = new ArrayList<JsonSchema>();
				objKeyList.add(objKey);
				jsonSchema.addObjArrayKey(arrayKey, objKeyList);
			} else {
				List<JsonValue> baseArrayKeyList = new ArrayList<JsonValue>();
				JsonValue jsonValue = new JsonValue();
				if (value instanceof String || value == null) {
					jsonValue.setType("string");
					jsonValue.setValue((String) value);
				} else if (value instanceof Integer) {
					jsonValue.setType("integer");
					jsonValue.setValue(String.valueOf((Integer) value));
				} else {
					System.out.println("Json Array Value Type Error !!!");
				}
				baseArrayKeyList.add(jsonValue);
				jsonSchema.addBaseArrayKey(arrayKey, baseArrayKeyList);
			}
		}

	}

	private JsonSchema getJsonSchema(JSONObject jsonObject) {
		JsonSchema jsonSchema = new JsonSchema();

		for (Map.Entry<String, Object> subEntry : jsonObject.entrySet()) {
			Object value = subEntry.getValue();
			String key = subEntry.getKey();

			if (value instanceof JSONObject) {
				JsonSchema objKey = getJsonSchema(((JSONObject) value));
				jsonSchema.addObjKey(key, objKey);
			} else if (value instanceof JSONArray) {
				getJsonArrayKeyList(jsonSchema, key, ((JSONArray) value));
			} else {
				if (value instanceof String || value == null) {
					JsonValue jsonValue = new JsonValue();
					jsonValue.setType("string");
					jsonValue.setValue((String) value);
					jsonSchema.addBaseKey(key, jsonValue);
				} else if (value instanceof Integer) {
					JsonValue jsonValue = new JsonValue();
					jsonValue.setType("integer");
					jsonValue.setValue(String.valueOf((Integer) value));
					jsonSchema.addBaseKey(key, jsonValue);
				} else {
					System.out.println("Json Object Value Type Error !!!");
				}
			}
		}

		return jsonSchema;
	}

	private String getFileContent(String path) {
		String result = "";

		System.out.println("getJsonFileContent =>" + path);

		InputStream is = null;

		try {
			is = new FileInputStream(path);
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
