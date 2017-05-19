package abt.srvProject.utiles;

public class DataRequest {
	String org;
	String suborg;
	String ani;
	String dnis;
	String fechaDesde; //Formato YYYYMMDDHHMISS
	String fechaHasta; //Formato YYYYMMDDHHMISS
	String uniqueid;
	String qrytext;
	String fname;
	int limit;   //Limite de registros a retornar -1: unlimited
	
	//Getter ans Setter
	
	public String getOrg() {
		return org;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getSuborg() {
		return suborg;
	}
	public void setSuborg(String suborg) {
		this.suborg = suborg;
	}
	public String getAni() {
		return ani;
	}
	public void setAni(String ani) {
		this.ani = ani;
	}
	public String getDnis() {
		return dnis;
	}
	public void setDnis(String dnis) {
		this.dnis = dnis;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getQrytext() {
		return qrytext;
	}
	public void setQrytext(String qrytext) {
		this.qrytext = qrytext;
	}
	
}
