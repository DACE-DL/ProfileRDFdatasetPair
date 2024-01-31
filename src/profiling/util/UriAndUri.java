package profiling.util;

import java.util.Objects;

public class UriAndUri {
	
	private String uri1;
	private String uri2;
	
	public UriAndUri() {
	}

	public UriAndUri(String uri1, String uri2) {
		this.uri1 = uri1;
		this.uri2 = uri2;
	}
	public String getUri1() {
		return uri1;
	}
	public void setUri1(String uri1) {
		this.uri1 = uri1;
	}
	public String getUri2() {
		return uri2;
	}
	public void setUri2(String uri2) {
		this.uri2 = uri2;
	}

	// Pour la methode .contains()
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UriAndUri other = (UriAndUri) obj;
        return Objects.equals(uri1, other.uri1) && Objects.equals(uri2, other.uri2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri1, uri2);
    }
	
}
