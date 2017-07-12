package eco.model;

public class DataRequest {
	String metodo;	
	String cantEnt; 	//flag
	String entAct;  	//entidad
	String intEnc;		//intencion
	String accTrans; 	//accion_transferencia
	String mesgAcc;		//mensaje_accion_audio
	String exist;		//existe
	int limit;			//maximo rows a retornar

	//Getter and setter
	
	public String getMetodo() {
		return metodo;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getAccTrans() {
		return accTrans;
	}
	public void setAccTrans(String accTrans) {
		this.accTrans = accTrans;
	}
	public String getMesgAcc() {
		return mesgAcc;
	}
	public void setMesgAcc(String mesgAcc) {
		this.mesgAcc = mesgAcc;
	}
	public String getExist() {
		return exist;
	}
	public void setExist(String exist) {
		this.exist = exist;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public String getCantEnt() {
		return cantEnt;
	}
	public void setCantEnt(String cantEnt) {
		this.cantEnt = cantEnt;
	}
	public String getEntAct() {
		return entAct;
	}
	public void setEntAct(String entAct) {
		this.entAct = entAct;
	}
	public String getIntEnc() {
		return intEnc;
	}
	public void setIntEnc(String intEnc) {
		this.intEnc = intEnc;
	}
}
