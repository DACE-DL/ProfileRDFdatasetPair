package profiling.util;

public class ProfilingQueryObject {
	private Integer iDquery;
	private String titleQuery;
	private String commentQuery;
	private String typeQuery;
	private String stringQuery;
	
	
	public ProfilingQueryObject() {
		super();
	}

	public ProfilingQueryObject(Integer iDquery, String titleQuery,String commentQuery, String typeQuery, String stringQuery) {
		this.iDquery = iDquery;
		this.titleQuery = titleQuery;
		this.commentQuery = commentQuery;
		this.typeQuery = typeQuery;
		this.stringQuery = stringQuery;
	}

	public Integer getIDquery() {
		return iDquery;
	}

	public void setIDquery(Integer iDquery) {
		this.iDquery = iDquery;
	}

	public String getTypeQuery() {
		return typeQuery;
	}

	public void setTypeQuery(String typeQuery) {
		this.typeQuery = typeQuery;
	}

	public String getTitleQuery() {
		return titleQuery;
	}

	public void setTitleQuery(String titleQuery) {
		this.titleQuery = titleQuery;
	}

	public String getCommentQuery() {
		return commentQuery;
	}

	public void setCommentQuery(String commentQuery) {
		this.commentQuery = commentQuery;
	}


	public String getStringQuery() {
		return stringQuery;
	}

	public void setStringQuery(String stringQuery) {
		this.stringQuery = stringQuery;
	}

}
