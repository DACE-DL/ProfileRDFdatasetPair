package profiling.util;

import java.util.ArrayList;
import java.util.Objects;

public class UriListAndUriList {
	
	private ArrayList<String> uriList1;
	private ArrayList<String> uriList2;
	
	public UriListAndUriList() {
	}
	
	public UriListAndUriList(ArrayList<String> uriList1, ArrayList<String> uriList2) {
		this.uriList1 = uriList1;
		this.uriList2 = uriList2;
	}

	public ArrayList<String> getUriList1() {
		return uriList1;
	}

	public void setUriList1(ArrayList<String> uriList1) {
		this.uriList1 = uriList1;
	}

	public ArrayList<String> getUriList2() {
		return uriList2;
	}

	public void setUriList2(ArrayList<String> uriList2) {
		this.uriList2 = uriList2;
	}
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UriListAndUriList that = (UriListAndUriList) obj;
        return  Objects.equals(uriList2, that.uriList2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uriList2);
    }

	// Méthode toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        for (int i = 0; i < uriList1.size(); i++) {
            sb.append(uriList1.get(i));
            if (i < uriList1.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("],[");
        for (int i = 0; i < uriList2.size(); i++) {
            sb.append(uriList2.get(i));
            if (i < uriList2.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
