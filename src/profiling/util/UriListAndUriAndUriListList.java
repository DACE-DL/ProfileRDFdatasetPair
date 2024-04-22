package profiling.util;

import java.util.ArrayList;

public class UriListAndUriAndUriListList {
	
	private UriList uriList;
	private ArrayList<UriAndUriList> uriAndUriListList;
	
	
	public UriListAndUriAndUriListList(UriList uriList, ArrayList<UriAndUriList> uriAndUriListList) {
		this.uriList = uriList;
		this.uriAndUriListList = uriAndUriListList;
	}

	public UriListAndUriAndUriListList() {
	}

	
	public UriList getUriList() {
		return uriList;
	}

	public void setUriList(UriList uriList) {
		this.uriList = uriList;
	}

	public ArrayList<UriAndUriList> getUriAndUriListList() {
		return uriAndUriListList;
	}

	public void setUriAndUriListList(ArrayList<UriAndUriList> uriAndUriListList) {
		this.uriAndUriListList = uriAndUriListList;
	}

	// Méthode toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
		sb.append(" [ ");
        for (int i = 0; i < uriList.size(); i++) {
            sb.append(uriList.get(i).toString());
            if (i < uriList.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ] ");
		sb.append(", [ ");
        for (int i = 0; i < uriAndUriListList.size(); i++) {
            sb.append(" { ");
			sb.append(uriAndUriListList.get(i).getUri());
            sb.append(" , [");
			for (int ii = 0; ii < uriAndUriListList.get(i).getUriList().size(); ii++) {
			    sb.append(uriAndUriListList.get(i).getUriList().get(ii).getUri());
				if (ii < uriAndUriListList.get(i).getUriList().size() - 1) {
					sb.append(", ");
				}
			}
			sb.append(" ] ");
			sb.append(" } ");
			if (i < uriAndUriListList.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ] ");
		sb.append(" } ");
        return sb.toString();
    }
	
}
