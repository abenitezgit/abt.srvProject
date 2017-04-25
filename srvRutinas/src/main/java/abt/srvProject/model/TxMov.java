package abt.srvProject.model;

public class TxMov {
	int rowsRead;
	int rowsLoad;
	int intentos;
	
	//Getter and Setter
	
	public int getRowsRead() {
		return rowsRead;
	}
	public void setRowsRead(int rowsRead) {
		this.rowsRead = rowsRead;
	}
	public int getRowsLoad() {
		return rowsLoad;
	}
	public void setRowsLoad(int rowsLoad) {
		this.rowsLoad = rowsLoad;
	}
	public int getIntentos() {
		return intentos;
	}
	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}
	
	//Metodos adicionales
	
	public synchronized void incRowsRead() {
		rowsRead++;
	}
	
	public synchronized void incRowsLoad() {
		rowsLoad++;
	}
	
	public int getRowsError() {
		return rowsRead-rowsLoad;
	}
}
