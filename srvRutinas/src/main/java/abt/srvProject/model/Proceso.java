/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abt.srvProject.model;

/**
 *
 * @author andresbenitez
 */
public class Proceso {
    String procID;
    int nOrder;
    int critical;
    String type;
    
    Object params = new Object();
    
    //Getter and Setter

	public void setParams(Object params) {
		this.params = params;
	}

    public Object getParams() {
		return params;
	}

		public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getnOrder() {
        return nOrder;
    }

    public void setnOrder(int nOrder) {
        this.nOrder = nOrder;
    }

    public String getProcID() {
        return procID;
    }

    public void setProcID(String procID) {
        this.procID = procID;
    }
}
