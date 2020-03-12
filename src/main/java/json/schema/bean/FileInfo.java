package json.schema.bean;

public class FileInfo {
	
	private String absolutePath;
	
	private String baseInPath;
	private String baseOutPath;
	
	public FileInfo(String baseInPath, String baseOutPath) {
		this.baseInPath = baseInPath;
		this.baseOutPath = baseOutPath;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public String getOutPutPath() {
		int idx = absolutePath.indexOf(baseInPath);
		
		return baseOutPath + absolutePath.substring(idx + baseInPath.length());
	}
}
