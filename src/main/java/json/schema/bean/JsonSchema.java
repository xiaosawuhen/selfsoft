package json.schema.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonSchema {

	private Map<String, JsonValue> baseKeyMap = new HashMap<String, JsonValue>();
	
	private Map<String, JsonSchema> objKeyMap = new HashMap<String, JsonSchema>();
	
	private Map<String, List<JsonValue>> baseArrayKeyMap = new HashMap<String, List<JsonValue>>();
	
	private Map<String, List<JsonSchema>> objArrayKeyMap = new HashMap<String, List<JsonSchema>>();

	public Map<String, JsonValue> getBaseKeyMap() {
		return baseKeyMap;
	}

	public void setBaseKeyMap(Map<String, JsonValue> baseKeyMap) {
		this.baseKeyMap = baseKeyMap;
	}
	
	public void addBaseKey(String key, JsonValue value) {
		this.baseKeyMap.put(key, value);
	}

	public Map<String, JsonSchema> getObjKeyMap() {
		return objKeyMap;
	}

	public void setObjKeyMap(Map<String, JsonSchema> objKeyMap) {
		this.objKeyMap = objKeyMap;
	}
	
	public void addObjKey(String key, JsonSchema value) {
		this.objKeyMap.put(key, value);
	}

	public Map<String, List<JsonValue>> getBaseArrayKeyMap() {
		return baseArrayKeyMap;
	}

	public void setBaseArrayKeyMap(Map<String, List<JsonValue>> baseArrayKeyMap) {
		this.baseArrayKeyMap = baseArrayKeyMap;
	}
	
	public void addBaseArrayKey(String key, List<JsonValue> value) {
		this.baseArrayKeyMap.put(key, value);
	}

	public Map<String, List<JsonSchema>> getObjArrayKeyMap() {
		return objArrayKeyMap;
	}

	public void setObjArrayKeyMap(Map<String, List<JsonSchema>> objArrayKeyMap) {
		this.objArrayKeyMap = objArrayKeyMap;
	}
	
	public void addObjArrayKey(String key, List<JsonSchema> value) {
		this.objArrayKeyMap.put(key, value);
	}
}
