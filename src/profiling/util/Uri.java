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
}
