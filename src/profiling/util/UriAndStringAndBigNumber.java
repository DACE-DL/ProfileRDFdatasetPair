package profiling.util;

public class UriAndStringAndBigNumber {
	
	private String uri;
	private String str;
	private Long number;
	

	
	public UriAndStringAndBigNumber(String uri, String str, Long number) {
		this.uri = uri;
		this.str = str;
		this.number = number;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
}
