package profiling.util;

public class Uri {
	
	private String uri;
	
	public Uri() {
	}
	
	public Uri(String uri) {
		this.uri = uri;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	// Méthode toString()
    @Override
    public String toString() {
        return uri;
    }
	// Méthode equals() et hashCode() pour la comparaison des objets Uri
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Uri uriObj = (Uri) obj;
        return uri.equals(uriObj.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
