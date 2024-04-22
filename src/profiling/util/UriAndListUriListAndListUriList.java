package profiling.util;

import java.util.ArrayList;

public class UriAndListUriListAndListUriList {
	
	private String uri;
	private ArrayList<ArrayList<Uri>> listUriList1;
	private ArrayList<ArrayList<Uri>> listUriList2;
	
	public UriAndListUriListAndListUriList() {
	}
	
	public UriAndListUriListAndListUriList(String uri, ArrayList<ArrayList<Uri>> listUriList1,
			ArrayList<ArrayList<Uri>> listUriList2) {
		this.uri = uri;
		this.listUriList1 = listUriList1;
		this.listUriList2 = listUriList2;
	}


	public String getUri() {
		return uri;
	}


	public void setUri(String uri) {
		this.uri = uri;
	}


	public ArrayList<ArrayList<Uri>> getListUriList1() {
		return listUriList1;
	}


	public void setListUriList1(ArrayList<ArrayList<Uri>> listUriList1) {
		this.listUriList1 = listUriList1;
	}


	public ArrayList<ArrayList<Uri>> getListUriList2() {
		return listUriList2;
	}


	public void setListUriList2(ArrayList<ArrayList<Uri>> listUriList2) {
		this.listUriList2 = listUriList2;
	}
}
