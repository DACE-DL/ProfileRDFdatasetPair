package profiling.util;

import java.util.Objects;

public class UriAndUriAndUri {
	
	private String uri1;
	private String uri2;
	private String uri3;
	
	public UriAndUriAndUri() {
	}

	public UriAndUriAndUri(String uri1, String uri2, String uri3) {
		this.uri1 = uri1;
		this.uri2 = uri2;
		this.uri3 = uri3;
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
	public String getUri3() {
		return uri3;
	}
	public void setUri3(String uri3) {
		this.uri3 = uri3;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UriAndUriAndUri other = (UriAndUriAndUri) obj;
        return Objects.equals(uri1, other.uri1) &&
               Objects.equals(uri2, other.uri2) &&
               Objects.equals(uri3, other.uri3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri1, uri2, uri3);
    }
}
