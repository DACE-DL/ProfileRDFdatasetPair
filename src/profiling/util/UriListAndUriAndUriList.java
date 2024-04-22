package profiling.util;

import java.util.ArrayList;

public class UriListAndUriAndUriList {
	
	private ArrayList<Uri> uriList1;
	private Uri uri;
	private ArrayList<Uri> uriList2;
	
	public UriListAndUriAndUriList() {
	}

	public UriListAndUriAndUriList(ArrayList<Uri> uriList1, Uri uri, ArrayList<Uri> uriList2) {
		this.uriList1 = uriList1;
		this.uri = uri;
		this.uriList2 = uriList2;
	}


	public ArrayList<Uri> getUriList1() {
		return uriList1;
	}

	public void setUriList1(ArrayList<Uri> uriList1) {
		this.uriList1 = uriList1;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
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
        sb.append("{");
		sb.append(" [ ");
        for (int i = 0; i < uriList1.size(); i++) {
            sb.append(uriList1.get(i).toString());
            if (i < uriList1.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ] , ");
		sb.append(uri);
		sb.append(" , [ ");
        for (int i = 0; i < uriList2.size(); i++) {
            sb.append(uriList2.get(i).toString());
            if (i < uriList2.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ] ");
		sb.append(" } ");
        return sb.toString();
    }
}
