package profiling.util;

import java.util.ArrayList;

public class UriListAndUriList {
	
	private ArrayList<Uri> uriList1;
	private ArrayList<Uri> uriList2;
	
	
	public UriListAndUriList() {
	}
	
	public UriListAndUriList(ArrayList<Uri> uriList1, ArrayList<Uri> uriList2) {
		this.uriList1 = uriList1;
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
	
	// Méthode toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
        .append("[");
        for (int i = 0; i < uriList1.size(); i++) {
            sb.append(uriList1.get(i).toString());
            if (i < uriList1.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]")
        .append(", [");
        for (int i = 0; i < uriList2.size(); i++) {
            sb.append(uriList2.get(i).toString());
            if (i < uriList2.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
