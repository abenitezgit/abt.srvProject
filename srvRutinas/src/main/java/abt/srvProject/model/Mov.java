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
public class Mov {
    String movID;
    String movDesc;
    int enable;
    String CliDesc;
    int WHEREACTIVE;
    String QUERYBODY;
    String STBNAME;
    String DTBNAME;
    String SIP;
    String SDBDESC;
    String SDBNAME;
    String SDBTYPE;
    String SDBPORT;
    String SDBINSTANCE;
    String SDBCONF;
    String SDBJDBC;
    String SDBOWNER;
    String SUSERNAME;
    String SUSERPASS;
    String SUSERTYPE;
    String DIP;
    String DDBDESC;
    String DDBNAME;
    String DDBTYPE;
    String DDBPORT;
    String DDBINSTANCE;
    String DDBCONF;
    String DDBJDBC;
    String DDBOWNER;
    String DUSERNAME;
    String DUSERPASS;
    String DUSERTYPE;
    int append;
    int offset;
    int createDest;
    int maxRowsError;
    int maxPctError;
    int rollbackOnError;
    
    List<MovMatch> lstMovMatch = new ArrayList<>();
    
    //Getter and Setter
    //
    
	public String getMovID() {
		return movID;
	}

	public String getSDBOWNER() {
		return SDBOWNER;
	}

	public void setSDBOWNER(String sDBOWNER) {
		SDBOWNER = sDBOWNER;
	}

	public String getDDBOWNER() {
		return DDBOWNER;
	}

	public void setDDBOWNER(String dDBOWNER) {
		DDBOWNER = dDBOWNER;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCreateDest() {
		return createDest;
	}

	public void setCreateDest(int createDest) {
		this.createDest = createDest;
	}

	public int getMaxRowsError() {
		return maxRowsError;
	}

	public void setMaxRowsError(int maxRowsError) {
		this.maxRowsError = maxRowsError;
	}

	public int getMaxPctError() {
		return maxPctError;
	}

	public void setMaxPctError(int maxPctError) {
		this.maxPctError = maxPctError;
	}

	public int getRollbackOnError() {
		return rollbackOnError;
	}

	public void setRollbackOnError(int rollbackOnError) {
		this.rollbackOnError = rollbackOnError;
	}

	public int getAppend() {
		return append;
	}

	public void setAppend(int append) {
		this.append = append;
	}

	public void setMovID(String movID) {
		this.movID = movID;
	}

	public String getMovDesc() {
		return movDesc;
	}

	public void setMovDesc(String movDesc) {
		this.movDesc = movDesc;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getCliDesc() {
		return CliDesc;
	}

	public void setCliDesc(String cliDesc) {
		CliDesc = cliDesc;
	}

	public int getWHEREACTIVE() {
		return WHEREACTIVE;
	}

	public void setWHEREACTIVE(int wHEREACTIVE) {
		WHEREACTIVE = wHEREACTIVE;
	}

	public String getQUERYBODY() {
		return QUERYBODY;
	}

	public void setQUERYBODY(String qUERYBODY) {
		QUERYBODY = qUERYBODY;
	}

	public String getSTBNAME() {
		return STBNAME;
	}

	public void setSTBNAME(String sTBNAME) {
		STBNAME = sTBNAME;
	}

	public String getDTBNAME() {
		return DTBNAME;
	}

	public void setDTBNAME(String dTBNAME) {
		DTBNAME = dTBNAME;
	}

	public String getSIP() {
		return SIP;
	}

	public void setSIP(String sIP) {
		SIP = sIP;
	}

	public String getSDBDESC() {
		return SDBDESC;
	}

	public void setSDBDESC(String sDBDESC) {
		SDBDESC = sDBDESC;
	}

	public String getSDBNAME() {
		return SDBNAME;
	}

	public void setSDBNAME(String sDBNAME) {
		SDBNAME = sDBNAME;
	}

	public String getSDBTYPE() {
		return SDBTYPE;
	}

	public void setSDBTYPE(String sDBTYPE) {
		SDBTYPE = sDBTYPE;
	}

	public String getSDBPORT() {
		return SDBPORT;
	}

	public void setSDBPORT(String sDBPORT) {
		SDBPORT = sDBPORT;
	}

	public String getSDBINSTANCE() {
		return SDBINSTANCE;
	}

	public void setSDBINSTANCE(String sDBINSTANCE) {
		SDBINSTANCE = sDBINSTANCE;
	}

	public String getSDBCONF() {
		return SDBCONF;
	}

	public void setSDBCONF(String sDBCONF) {
		SDBCONF = sDBCONF;
	}

	public String getSDBJDBC() {
		return SDBJDBC;
	}

	public void setSDBJDBC(String sDBJDBC) {
		SDBJDBC = sDBJDBC;
	}

	public String getSUSERNAME() {
		return SUSERNAME;
	}

	public void setSUSERNAME(String sUSERNAME) {
		SUSERNAME = sUSERNAME;
	}

	public String getSUSERPASS() {
		return SUSERPASS;
	}

	public void setSUSERPASS(String sUSERPASS) {
		SUSERPASS = sUSERPASS;
	}

	public String getSUSERTYPE() {
		return SUSERTYPE;
	}

	public void setSUSERTYPE(String sUSERTYPE) {
		SUSERTYPE = sUSERTYPE;
	}

	public String getDIP() {
		return DIP;
	}

	public void setDIP(String dIP) {
		DIP = dIP;
	}

	public String getDDBDESC() {
		return DDBDESC;
	}

	public void setDDBDESC(String dDBDESC) {
		DDBDESC = dDBDESC;
	}

	public String getDDBNAME() {
		return DDBNAME;
	}

	public void setDDBNAME(String dDBNAME) {
		DDBNAME = dDBNAME;
	}

	public String getDDBTYPE() {
		return DDBTYPE;
	}

	public void setDDBTYPE(String dDBTYPE) {
		DDBTYPE = dDBTYPE;
	}

	public String getDDBPORT() {
		return DDBPORT;
	}

	public void setDDBPORT(String dDBPORT) {
		DDBPORT = dDBPORT;
	}

	public String getDDBINSTANCE() {
		return DDBINSTANCE;
	}

	public void setDDBINSTANCE(String dDBINSTANCE) {
		DDBINSTANCE = dDBINSTANCE;
	}

	public String getDDBCONF() {
		return DDBCONF;
	}

	public void setDDBCONF(String dDBCONF) {
		DDBCONF = dDBCONF;
	}

	public String getDDBJDBC() {
		return DDBJDBC;
	}

	public void setDDBJDBC(String dDBJDBC) {
		DDBJDBC = dDBJDBC;
	}

	public String getDUSERNAME() {
		return DUSERNAME;
	}

	public void setDUSERNAME(String dUSERNAME) {
		DUSERNAME = dUSERNAME;
	}

	public String getDUSERPASS() {
		return DUSERPASS;
	}

	public void setDUSERPASS(String dUSERPASS) {
		DUSERPASS = dUSERPASS;
	}

	public String getDUSERTYPE() {
		return DUSERTYPE;
	}

	public void setDUSERTYPE(String dUSERTYPE) {
		DUSERTYPE = dUSERTYPE;
	}

	public List<MovMatch> getLstMovMatch() {
		return lstMovMatch;
	}

	public void setLstMovMatch(List<MovMatch> lstMovMatch) {
		this.lstMovMatch = lstMovMatch;
	}
    
    
}
