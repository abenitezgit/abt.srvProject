package abt.srvProject.dataAccess;

import java.util.ArrayList;
import java.util.List;

import abt.srvProject.model.Mov;
import abt.srvProject.model.MovMatch;
import abt.srvProject.srvRutinas.Rutinas;

public class MetaQuery {
	Rutinas mylib = new Rutinas();
	String dbType;
	
	public MetaQuery(String dbType) {
		this.dbType = dbType;
	}
	
	public String parseFieldType(String fieldName, String fieldType) {
		String response="";
		switch (dbType) {
		case "SQL":
			switch (fieldType) {
				case "F":
					response = fieldName;
					break;
				case "T":
					response = (char)34 + fieldName + (char)34;
					break;
				case "N":
					response = "NULL";
					break;
			}
			break;
		case "mySQL":
			switch (fieldType) {
				case "F":
					response = fieldName;
					break;
				case "T":
					response = (char)34 + fieldName + (char)34;
					break;
				case "N":
					response = "NULL";
					break;
			}
			break;
		}
		return response;
	}
	
	public String parseTableName(String tbName, String dbName, String dbOwner) {
		String response = "";
		switch (dbType) {
			case "SQL":
				response = "["+dbName+"].["+dbOwner+"].["+tbName+"]";
				break;
			case "mySQL":
				response = dbName+"."+tbName;
				break;
		}
		return response;
	}
	
	public String getSqlTableExist(String tbName) {
		String vSql = "";
		switch(dbType) {
			case "SQL":
				vSql = "select * from sysobjects where type='U' and upper(name)='"+tbName.toUpperCase()+"'";
				break;
			case "mySQL":
				vSql = "";
				break;
		}
		return vSql;
	}
	
	public String parseaCreateField(MovMatch mm) {
		String vSql="";
		switch(dbType) {
			case "SQL":
				switch(mm.getSourceType()) {
					case "int":
						vSql = mm.getDestField() + " int ";
						break;
					case "varchar":
						if (mm.getSourceLength()==0) {
							vSql = mm.getDestField() + " varchar(255)";
						} else {
							vSql = mm.getDestField() + " varchar(" + String.valueOf(mm.getSourceLength()) + ")"; 
						}
						break;
					default:
						vSql = mm.getDestField() + " " + mm.getSourceType();
						break;
				}
				break;
			case "mySQL":
				break;
		}
		return vSql;
	}
	
	public String getSqlDeleteRows(Mov mov) throws Exception {
		try {
			String vSql="";
			switch (dbType) {
				case "SQL":
					vSql = "delete from ["+mov.getDDBNAME()+"].["+mov.getDDBOWNER()+"].["+mov.getDTBNAME()+"]";
					//Valida si hay where asociado
					if (mov.getWHEREACTIVE()==1) {
						vSql = vSql + " " + mov.getQUERYBODY();
					}
					break;
				case "mySQL":
					break;
			}
			
			return vSql;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String getSqlCreateTable(Mov mov) throws Exception {
		String vSql="";
		try {
			switch (dbType) {
			case "SQL":
				List<MovMatch> lstmm = new ArrayList<>();
				lstmm = mov.getLstMovMatch();
				boolean isFirst=true;
				for (int i=0; i<lstmm.size(); i++) {
					if (isFirst) {
						isFirst = false;
					} else {
						vSql = vSql + ",";
					}
					vSql = vSql + parseaCreateField(lstmm.get(i));
				}
				vSql = "create table ["+mov.getDDBNAME()+"].["+mov.getDDBOWNER()+"].["+mov.getDTBNAME()+"] ("+vSql+")";
				break;
			case "mySQL":
				break;
			}
			
			return vSql;
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
	}
	
	public String getSqlQuerySource(Mov mov) {
		String vSQL="";
		switch (dbType) {
			case "SQL":
				List<MovMatch> lstmm = new ArrayList<>();
				lstmm = mov.getLstMovMatch();
				
				if (lstmm.size()>0) {
					MovMatch mm;
					boolean isFirst = true;
					for (int i=0; i<lstmm.size(); i++) {
						mm = new MovMatch();
						mm = lstmm.get(i);
						if (isFirst) {
							isFirst = false;
						} else {
							vSQL = vSQL + ",";
						}
						vSQL = vSQL + parseFieldType(mm.getSourceField(), mm.getFieldType());
					}
					
					//Si se conformaron correctamente los campos a seleccionar se arma la sentencia de select
					if (!mylib.isNullOrEmpty(vSQL)) {
						//Agrega el select
						vSQL = "select " + vSQL;
						
						//Agrega la tabla de consulta
						vSQL = vSQL + " from " + parseTableName(mov.getSTBNAME(), mov.getSDBNAME(), mov.getSDBOWNER());
						
						//Valida si se agrega Where
						if (mov.getWHEREACTIVE()==1) {
							vSQL = vSQL + " " + mov.getQUERYBODY();
						}
						
						
					} else {
						//Error, no se conformo bien la sentencia
					}
				} else {
					//Error, no hay campos a recuperar
				}
				break;
			case "mySQL":
				break;
		}
		
		return vSQL;
	}
}
