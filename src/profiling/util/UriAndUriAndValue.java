package profiling.util;

public class UriAndUriAndValue {
	
	private Uri uri1;
	private Uri uri2;
	private String value;
	
	public UriAndUriAndValue() {
	}

	public UriAndUriAndValue(Uri uri1, Uri uri2, String value) {
		this.uri1 = uri1;
		this.uri2 = uri2;
		this.value = value;
	}

	public Uri getUri1() {
		return uri1;
	}

	public void setUri1(Uri uri1) {
		this.uri1 = uri1;
	}

	public Uri getUri2() {
		return uri2;
	}

	public void setUri2(Uri uri2) {
		this.uri2 = uri2;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	// Méthode toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(uri1);
		sb.append(" , ");
		sb.append(uri2);
		sb.append(" , ");
		sb.append(value);
        sb.append("}");
        return sb.toString();
    }
	
}
