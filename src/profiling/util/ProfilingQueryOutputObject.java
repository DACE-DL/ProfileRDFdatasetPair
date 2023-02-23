package profiling.util;

public class ProfilingQueryOutputObject {
	private ProfilingQueryObject query;
	private String queryResponse;
	
	public ProfilingQueryOutputObject(ProfilingQueryObject query, String queryResponse) {
		super();
		this.query = query;
		this.queryResponse = queryResponse;
	}

	public ProfilingQueryObject getQuery() {
		return query;
	}

	public void setQuery(ProfilingQueryObject query) {
		this.query = query;
	}

	public String getQueryResponse() {
		return queryResponse;
	}

	public void setQueryResponse(String queryResponse) {
		this.queryResponse = queryResponse;
	}
	
	
	
}
