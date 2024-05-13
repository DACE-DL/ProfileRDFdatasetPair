package profiling.util;

import java.util.ArrayList;
import java.util.Objects;

public class UriListAndUriListAndNumberListAndNumber {
	
	private ArrayList<Uri> uriList;
	private ArrayList<UriListAndNumber> uriListAndNumberList;
	private Integer number;
	
	public UriListAndUriListAndNumberListAndNumber() {
	}

	public UriListAndUriListAndNumberListAndNumber(UriListAndUriListAndNumberListAndNumber other) {
		this.uriList = other.uriList;
		ArrayList<UriListAndNumber> thisUriListAndNumberList = new ArrayList<UriListAndNumber>();
		for (UriListAndNumber otherUriListAndNumberList : other.uriListAndNumberList) {
			thisUriListAndNumberList.add(new UriListAndNumber(otherUriListAndNumberList));
		}
		this.uriListAndNumberList = thisUriListAndNumberList;
		this.number = other.number;
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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriListAndUriListAndNumberListAndNumber that = (UriListAndUriListAndNumberListAndNumber) o;
		return Objects.equals(uriList, that.uriList) &&
		       Objects.equals(uriListAndNumberList, that.uriListAndNumberList) &&
		       Objects.equals(number, that.number);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uriList, uriListAndNumberList, number);
	}
}
