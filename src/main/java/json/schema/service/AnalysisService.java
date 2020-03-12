package json.schema.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import json.schema.bean.JsonSchema;
import json.schema.reader.JsonReader;
import json.schema.writer.JsonFormatWriter_v2;
import json.schema.writer.JsonFormaterList;

public class AnalysisService {

	public void convertJson(String file, String outFile) {
		JsonReader jr = new JsonReader();
		JsonSchema js = jr.getJsonBean(file);
		
//		JsonFormatWriter_v2 jf = new JsonFormatWriter_v2(outFile);
//		jf.writeJsonFile(js);
		
		JsonFormaterList jfl = new JsonFormaterList();
		List<String> formatedList = jfl.getFormatedList(js);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			for (String content : formatedList) {
				bw.write(content);
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void convertJson() {
		JsonReader jr = new JsonReader();
		JsonSchema js = jr.getJsonBean("/json.txt");
		
//		JsonWriter jw = new JsonWriter("result");
//		jw.writeJsonFile(js);
		
		JsonFormatWriter_v2 jf = new JsonFormatWriter_v2("result");
		jf.writeJsonFile(js);
	}
}
