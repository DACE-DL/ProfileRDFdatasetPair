package profiling.util;

import java.util.ArrayList;

public class UriAndUriAndUriListList {
	
	private String uri;
	private ArrayList<UriAndUriList> uriAndUriList;
	
	
	public UriAndUriAndUriListList() {
	}
	
	public UriAndUriAndUriListList(String uri, ArrayList<UriAndUriList> uriAndUriList) {
		this.uri = uri;
		this.uriAndUriList = uriAndUriList;
	}


	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

	public ArrayList<UriAndUriList> getUriAndUriList() {
		return uriAndUriList;
	}

	public void setUriAndUriList(ArrayList<UriAndUriList> uriAndUriList) {
		this.uriAndUriList = uriAndUriList;
	}

	// Méthode toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
		sb.append(uri);
		sb.append(", [ ");
        for (int i = 0; i < uriAndUriList.size(); i++) {
            sb.append(uriAndUriList.get(i).toString());
            if (i < uriAndUriList.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("} ]");
        return sb.toString();
    }
	
}
