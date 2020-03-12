package json.schema.bean;

public class JsonValue {

	private String type;
	
	private String value;
	
	public JsonValue() {
		
	}
	
	public JsonValue(String type, String... value) {
		this.type = type;
		
		if(value.length > 0) {
			this.value = value[0];
		} else {
			this.value = "";
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
