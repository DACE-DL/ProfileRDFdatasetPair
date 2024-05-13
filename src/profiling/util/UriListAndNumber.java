package profiling.util;

import java.util.ArrayList;
import java.util.Objects;

public class UriListAndNumber {
	
	private ArrayList<Uri> uriList;
	private Integer number;
	
	public UriListAndNumber() {
	}
	
	public UriListAndNumber(ArrayList<Uri> uriList, Integer number) {
		this.uriList = uriList;
		this.number = number;
	}

	public UriListAndNumber(UriListAndNumber other) {
		this.uriList = other.uriList;
		this.number = other.number;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UriListAndNumber that = (UriListAndNumber) o;
		return Objects.equals(uriList, that.uriList) &&
		       Objects.equals(number, that.number);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uriList, number);
	}
}
