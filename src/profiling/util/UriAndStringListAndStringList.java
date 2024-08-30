package profiling.util;

import java.util.ArrayList;

public class UriAndStringListAndStringList {
	
	private String uri;
	private ArrayList<String> listDomainAnomalies;
	private ArrayList<String> listRangeAnomalies;
	
	public UriAndStringListAndStringList() {
	}

	public UriAndStringListAndStringList(String uri, ArrayList<String> listDomainAnomalies,
			ArrayList<String> listRangeAnomalies) {
		this.uri = uri;
		this.listDomainAnomalies = listDomainAnomalies;
		this.listRangeAnomalies = listRangeAnomalies;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ArrayList<String> getListDomainAnomalies() {
		return listDomainAnomalies;
	}

	public void setListDomainAnomalies(ArrayList<String> listDomainAnomalies) {
		this.listDomainAnomalies = listDomainAnomalies;
	}

	public ArrayList<String> getListRangeAnomalies() {
		return listRangeAnomalies;
	}

	public void setListRangeAnomalies(ArrayList<String> listRangeAnomalies) {
		this.listRangeAnomalies = listRangeAnomalies;
	}
}
