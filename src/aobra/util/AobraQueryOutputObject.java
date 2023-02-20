package aobra.util;

public class AobraQueryOutputObject {
	private AobraQueryObject query;
	private String queryResponse;
	
	public AobraQueryOutputObject(AobraQueryObject query, String queryResponse) {
		super();
		this.query = query;
		this.queryResponse = queryResponse;
	}

	public AobraQueryObject getQuery() {
		return query;
	}

	public void setQuery(AobraQueryObject query) {
		this.query = query;
	}

	public String getQueryResponse() {
		return queryResponse;
	}

	public void setQueryResponse(String queryResponse) {
		this.queryResponse = queryResponse;
	}
	
	
	
}
