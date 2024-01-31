package profiling.util;

import java.util.ArrayList;

public class UriAndUriListAndUriList {
	
	private String uri;
	private ArrayList<String> uriList1;
	private ArrayList<String> uriList2;
	
	
	public UriAndUriListAndUriList() {
	}
	
	public UriAndUriListAndUriList(String uri, ArrayList<String> uriList1, ArrayList<String> uriList2) {
		this.uri = uri;
		this.uriList1 = uriList1;
		this.uriList2 = uriList2;
	}

	public ArrayList<String> getUriList1() {
		return uriList1;
	}

	public void setUriList1(ArrayList<String> uriList1) {
		this.uriList1 = uriList1;
	}

	public ArrayList<String> getUriList2() {
		return uriList2;
	}

	public void setUriList2(ArrayList<String> uriList2) {
		this.uriList2 = uriList2;
	}

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
