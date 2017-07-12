package eco.model;

public class QueryResponse {
	int status;
	String message;
	String query;
	int rows;
	int limit;
	String data;	//JSON_String object
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
