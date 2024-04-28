package profiling.util;

import java.util.ArrayList;

public class UriAndUriListAndNumberListAndNumber {
	
	private Uri uri;
	private ArrayList<UriListAndNumber> uriListAndNumberList;
	private Integer number;
	
	public UriAndUriListAndNumberListAndNumber() {
	}

	public UriAndUriListAndNumberListAndNumber(Uri uri, ArrayList<UriListAndNumber> uriListAndNumberList, Integer number) {
		this.uri = uri;
		this.uriListAndNumberList = uriListAndNumberList;
		this.number = number;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public ArrayList<UriListAndNumber> getUriListAndNumberList() {
		return uriListAndNumberList;
	}

	public void setUriListAndNumberList(ArrayList<UriListAndNumber> uriListAndNumberList) {
		this.uriListAndNumberList = uriListAndNumberList;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	
}
