package profiling.util;


public class UriAndUriListAndUriListAndUriList {
	
	private Uri uri;
	private UriList oneOf;
	private UriList unionOf;
	private UriList intersectionOf;
	
	
	public UriAndUriListAndUriListAndUriList() {
	}
	
	public UriAndUriListAndUriListAndUriList(Uri uri, UriList oneOf, UriList unionOf, UriList intersectionOf) {
		this.uri = uri;
		this.oneOf = oneOf;
		this.unionOf = unionOf;
		this.intersectionOf = intersectionOf;
	}

	public UriList getOneOf() {
		return oneOf;
	}

	public void setOneOf(UriList oneOf) {
		this.oneOf = oneOf;
	}

	public UriList getUnionOf() {
		return unionOf;
	}

	public void setUnionOf(UriList unionOf) {
		this.unionOf = unionOf;
	}

	public UriList getIntersectionOf() {
		return intersectionOf;
	}

	public void setIntersectionOf(UriList intersectionOf) {
		this.intersectionOf = intersectionOf;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}
}
