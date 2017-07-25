package eco.model;

public class DataRequest {
	String metodo;	
	String establec; 	//Establecimiento
	String tipoEstab;  	//Tipo Establecimiento
	String comuna;		//Comuna
	String direccion; 	//Direccion
	String telefono;	//Telefono
	int limit;			//maximo rows a retornar

	//Getter and setter
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public String getEstablec() {
		return establec;
	}
	public void setEstablec(String establec) {
		this.establec = establec;
	}
	public String getTipoEstab() {
		return tipoEstab;
	}
	public void setTipoEstab(String tipoEstab) {
		this.tipoEstab = tipoEstab;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}
