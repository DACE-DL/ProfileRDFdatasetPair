package profiling.util;

import java.util.ArrayList;
import java.util.Objects;

public class UriList {
	
	
	private ArrayList<Uri> uriList;
	
	
	public UriList() {
	}
	
	public UriList(ArrayList<Uri> uriList) {
		this.uriList = uriList;
	}

	public ArrayList<Uri> getUriList() {
		return uriList;
	}

	public void setUriList(ArrayList<Uri> uriList) {
		this.uriList = uriList;
	}

     // Méthode get() surchargée
     public Uri get(int index) {
        return uriList.get(index);
    }

    // Méthode size() surchargée
    public int size() {
        return uriList.size();
    }
	
	// Méthode toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < uriList.size(); i++) {
            sb.append(uriList.get(i).toString());
            if (i < uriList.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UriList that = (UriList) obj;
        return  Objects.equals(uriList, that.uriList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uriList);
    }

   
	
}
