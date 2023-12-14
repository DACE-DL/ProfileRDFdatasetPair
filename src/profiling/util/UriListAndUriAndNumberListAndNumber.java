package profiling.util;

import java.util.ArrayList;

public class UriListAndUriAndNumberListAndNumber {
	
	private ArrayList<Uri> uriList;
	private ArrayList<UriAndNumber> uriAndNumberList;
	private Integer number;
	
	public UriListAndUriAndNumberListAndNumber() {
	}

	public UriListAndUriAndNumberListAndNumber(ArrayList<Uri> uriList, ArrayList<UriAndNumber> uriAndNumberList,
			Integer number) {
		this.uriList = uriList;
		this.uriAndNumberList = uriAndNumberList;
		this.number = number;
	}

	public ArrayList<Uri> getUriList() {
		return uriList;
	}

	public void setUriList(ArrayList<Uri> uriList) {
		this.uriList = uriList;
	}

	public ArrayList<UriAndNumber> getUriAndNumberList() {
		return uriAndNumberList;
	}

	public void setUriAndNumberList(ArrayList<UriAndNumber> uriAndNumberList) {
		this.uriAndNumberList = uriAndNumberList;
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
