package profiling.util;

import java.util.ArrayList;

public class UriListAndNumber {
	
	private ArrayList<Uri> uriList;
	private Integer number;
	
	public UriListAndNumber() {
	}
	
	public UriListAndNumber(ArrayList<Uri> uriList, Integer number) {
		this.uriList = uriList;
		this.number = number;
	}

	public ArrayList<Uri> getUriList() {
		return uriList;
	}

	public void setUriList(ArrayList<Uri> uriList) {
		this.uriList = uriList;
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
