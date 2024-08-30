package profiling.util;

public class UriAndUriAndNumber {
	
	private String uri1;
	private String uri2;
	private Integer number;
	
	public UriAndUriAndNumber() {
	}
	
	public UriAndUriAndNumber(String uri1, String uri2, Integer number) {
		this.uri1 = uri1;
		this.uri2 = uri2;
		this.number = number;
	}
	public String getUri1() {
		return uri1;
	}
	public void setUri1(String uri1) {
		this.uri1 = uri1;
	}
	public String getUri2() {
		return uri2;
	}
	public void setUri2(String uri2) {
		this.uri2 = uri2;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}

	public void incrementNumber() {
        this.number++;
    }
}
