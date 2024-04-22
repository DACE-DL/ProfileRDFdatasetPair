package profiling.util;

import java.util.ArrayList;

public class UriListAndUriListAndNumberListAndNumber {
	
	private ArrayList<Uri> uriList;
	private ArrayList<UriListAndNumber> uriListAndNumberList;
	private Integer number;
	
	public UriListAndUriListAndNumberListAndNumber() {
	}

	public UriListAndUriListAndNumberListAndNumber(ArrayList<Uri> uriList, ArrayList<UriListAndNumber> uriListAndNumberList,
			Integer number) {
		this.uriList = uriList;
		this.uriListAndNumberList = uriListAndNumberList;
		this.number = number;
	}

	public ArrayList<Uri> getUriList() {
		return uriList;
	}

	public void setUriList(ArrayList<Uri> uriList) {
		this.uriList = uriList;
	}

	public ArrayList<UriListAndNumber> getUriListAndNumberList() {
		return uriListAndNumberList;
	}

	public void setUriListAndNumberList(ArrayList<UriListAndNumber> uriListAndNumberList) {
		this.uriListAndNumberList = uriListAndNumberList;
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
