package profiling.util;

import java.util.ArrayList;

public class UriListAndNumberAndUriList {
	
	private ArrayList<Uri> uriList1;
    private Integer number;
	private ArrayList<Uri> uriList2;
	
	
	public UriListAndNumberAndUriList() {
	}
	
	public UriListAndNumberAndUriList(ArrayList<Uri> uriList1, Integer number, ArrayList<Uri> uriList2) {
		this.uriList1 = uriList1;
        this.number = number;
		this.uriList2 = uriList2;
	}

	public ArrayList<Uri> getUriList1() {
		return uriList1;
	}

	public void setUriList1(ArrayList<Uri> uriList1) {
		this.uriList1 = uriList1;
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
