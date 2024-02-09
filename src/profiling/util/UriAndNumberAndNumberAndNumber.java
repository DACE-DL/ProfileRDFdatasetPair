package profiling.util;

public class UriAndNumberAndNumberAndNumber {
	
	private String uri;
	private Integer number;
	private Integer number2;
	private Integer number3;
	
	public UriAndNumberAndNumberAndNumber() {
	}
	
	public UriAndNumberAndNumberAndNumber(String uri, Integer number, Integer number2, Integer number3) {
		this.uri = uri;
		this.number = number;
		this.number2 = number2;
		this.number3 = number3;
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

	public Integer getNumber2() {
		return number2;
	}

	public void setNumber2(Integer number2) {
		this.number2 = number2;
	}
	
	public Integer getNumber3() {
		return number3;
	}

	public void setNumber3(Integer number3) {
		this.number3 = number3;
	}

	// Méthode toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(uri);
		sb.append(",");
		sb.append(number);
		sb.append(",");
		sb.append(number2);
		sb.append(",");
		sb.append(number3);
        sb.append("}");
        return sb.toString();
    }
}
