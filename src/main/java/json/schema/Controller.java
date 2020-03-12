package json.schema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import json.schema.bean.FileInfo;
import json.schema.service.AnalysisService;

public class Controller {

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.convert();
	}
	
	private String baseInPath = "";
	private String baseOutPath = "";
	
	public Controller() {
        File file = new File("");
        String filePath = null;
		try {
			filePath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.baseInPath = filePath + File.separator +  "input";
		this.baseOutPath = filePath + File.separator +  "output";
	}
	
	public boolean convert() {
		List<FileInfo> fileList = getFileList(baseInPath);

		AnalysisService analysisService = new AnalysisService();

		for (FileInfo fileInfo : fileList) {
			analysisService.convertJson(fileInfo.getAbsolutePath(), fileInfo.getOutPutPath());
		}
		
		return true;
	}

	public List<FileInfo> getFileList(String path) {
		List<FileInfo> fileList = new ArrayList<FileInfo>();

		File file = new File(path);

		if (file.isFile()) {
			FileInfo fileInfo = new FileInfo(baseInPath, baseOutPath);
			fileInfo.setAbsolutePath(path);
			fileList.add(fileInfo);
		} else {
			if (file.isDirectory()) {
				File[] subFileArray = file.listFiles();
				for (File subFile : subFileArray) {
					List<FileInfo> subFileList = getFileList(subFile.getAbsolutePath());
					fileList.addAll(subFileList);
				}
			}
		}

		return fileList;
	}
}
