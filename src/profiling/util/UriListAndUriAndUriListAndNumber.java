package profiling.util;

import java.util.ArrayList;

public class UriListAndUriAndUriListAndNumber {
	
	private ArrayList<Uri> uriList1;
	private Uri uri;
	private ArrayList<Uri> uriList2;
	private Integer number;
	
	public UriListAndUriAndUriListAndNumber() {
	}

	public UriListAndUriAndUriListAndNumber(ArrayList<Uri> uriList1, Uri uri, ArrayList<Uri> uriList2, Integer number) {
		this.uriList1 = uriList1;
		this.uri = uri;
		this.uriList2 = uriList2;
		this.number = number;
	}


	public ArrayList<Uri> getUriList1() {
		return uriList1;
	}

	public void setUriList1(ArrayList<Uri> uriList1) {
		this.uriList1 = uriList1;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public ArrayList<Uri> getUriList2() {
		return uriList2;
	}

	public void setUriList2(ArrayList<Uri> uriList2) {
		this.uriList2 = uriList2;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	
	

	
	
}
