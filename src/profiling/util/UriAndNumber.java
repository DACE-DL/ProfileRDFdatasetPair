package profiling.util;

public class UriAndNumber {
	
	private String uri;
	private Integer number;
	

	
	public UriAndNumber(String uri, Integer number) {
		this.uri = uri;
		this.number = number;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
