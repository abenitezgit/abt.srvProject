/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abt.srvProject.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andresbenitez
 */
public class Grupo {
    String grpID;
    String grpDESC;
    String enable;
    String cliID;
    String cliDesc;
    String horDesc;
    String numSecExec;
    int maxTimeExec;
    String typeBalance;
    List<Proceso> lstProcess = new ArrayList<>();
    List<Dependence> lstDepend = new ArrayList<>();

    //Getter and Setter
    
	public List<Dependence> getLstDepend() {
		return lstDepend;
	}

	public String getTypeBalance() {
		return typeBalance;
	}

	public void setTypeBalance(String typeBalance) {
		this.typeBalance = typeBalance;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public int getMaxTimeExec() {
		return maxTimeExec;
	}

	public void setMaxTimeExec(int maxTimeExec) {
		this.maxTimeExec = maxTimeExec;
	}

	public String getNumSecExec() {
		return numSecExec;
	}

	public void setNumSecExec(String numSecExec) {
		this.numSecExec = numSecExec;
	}

	public String getCliID() {
		return cliID;
	}

	public void setCliID(String cliID) {
		this.cliID = cliID;
	}

	public String getCliDesc() {
		return cliDesc;
	}

	public void setCliDesc(String cliDesc) {
		this.cliDesc = cliDesc;
	}

	public String getHorDesc() {
		return horDesc;
	}

	public void setHorDesc(String horDesc) {
		this.horDesc = horDesc;
	}

	public void setLstDepend(List<Dependence> lstDepend) {
		this.lstDepend = lstDepend;
	}

    public List<Proceso> getLstProcess() {
        return lstProcess;
    }

    public synchronized void setLstProcess(List<Proceso> lstProcess) {
        this.lstProcess = lstProcess;
    }

    public String getGrpDESC() {
        return grpDESC;
    }

    public void setGrpDESC(String grpDESC) {
        this.grpDESC = grpDESC;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }
    
}
