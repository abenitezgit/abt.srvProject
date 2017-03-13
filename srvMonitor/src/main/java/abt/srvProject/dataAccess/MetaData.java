/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abt.srvProject.dataAccess;

import java.sql.Connection;
import java.sql.ResultSet;

import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;

/**
 *
 * @author andresbenitez
 */
public class MetaData {
    GlobalArea gDatos;
    Rutinas mylib = new Rutinas();
    
    private oracleDB oraConn;
    private sqlDB sqlConn;
    private mysqlDB myConn; 
    
    public MetaData (GlobalArea m) {
        gDatos = m;
    }
    
    public void openConnection() {
    	try {
            switch (gDatos.getInfo().getDbType()) {
	            case "ORA":
	                try {
	                	oraConn = new oracleDB(gDatos.getInfo().getDbHost(), gDatos.getInfo().getDbName(), String.valueOf(gDatos.getInfo().getDbPort()), gDatos.getInfo().getDbUser(), gDatos.getInfo().getDbPass());
	                    //oraConn = new oracleDB(gDatos.getServicio().getDbHost(), gDatos.getServicio().getDbName(), gDatos.getServicio().getDbPort(), gDatos.getServicio().getDbUser(), gDatos.getServicio().getDbPass());
	                    oraConn.conectar();
	                } catch (Exception e) {
	                    mylib.console(1,"Error de conexion a MetaData: "+e.getMessage());
	                }
	                break;
	            case "mySQL":
	                try {
	                    myConn = new mysqlDB(gDatos.getInfo().getDbHost(), gDatos.getInfo().getDbName(), String.valueOf(gDatos.getInfo().getDbPort()), gDatos.getInfo().getDbUser(), gDatos.getInfo().getDbPass());
	                    myConn.conectar();
	                } catch (Exception e) {
	                	mylib.console(1,"Error de conexion a MetaData: "+e.getMessage());
	                }
	                break;
	            case "SQL":
	                break;
	            case "HBASE":
	                break;
	            default:
	                break;
	        }
    	} catch (Exception e) {
    		mylib.console(1,"No puede abrir conección...: "+e.getMessage());
    	}
    }
    
    public Connection getConnection() {
    	switch (gDatos.getInfo().getDbType()) {
    		case "ORA":
    			return oraConn.getConexion();
    		case "SQL":
    			return sqlConn.getConexion();
    		case "mySQL":
    			return myConn.getConexion();
			default:
				return null;
    	} 
    }
    
    public boolean isConnected() {
        boolean connected = false;
        try  {
            switch (gDatos.getInfo().getDbType()) {
                case "ORA":
                    connected = oraConn.getConnStatus();
                    break;
                case "SQL":
                    connected = sqlConn.getConnStatus();
                    break;
                case "mySQL":
                    connected = myConn.getConnStatus();
                    break;
                default:
                    connected = false;
            }
            return connected;
        } catch (Exception e) {
            return connected;
        }
    }
    
    public int executeQuery(String vSQL) {
    	int result = 0;
        switch (gDatos.getInfo().getDbType()) {
        	case "mySQL":
        			result = myConn.execute(vSQL);
        		break;
    		default:
    			break;
        }
        return result;
    }
    
    public Object getQuery(String vSQL) {
        switch (gDatos.getInfo().getDbType()) {
            case "ORA":
                try {
                    ResultSet rs = oraConn.consultar(vSQL);
                    return rs;
                } catch (Exception e) {
                    mylib.console(1,"Error de Ejecucion SQL: "+ vSQL+ " details: "+ e.getMessage());
                }
                break;
            case "mySQL":
                try {
                    ResultSet rs = myConn.consultar(vSQL);
                    return rs;
                } catch (Exception e) {
                	mylib.console(1,"Error de Ejecucion SQL: "+ vSQL+ " details: "+ e.getMessage());
                }
                break;
            case "SQL":
                break;
            case "HBASE":
                break;
            default:
                break;
        }
        return null;
    }
    
    public void closeConnection() {
        switch (gDatos.getInfo().getDbType()) {
            case "ORA":
                try {
                    oraConn.closeConexion();
                } catch (Exception e) {
                	mylib.console(1,"Error Cerrando Conexion: "+e.getMessage());
                }
                break;
            case "mySQL":
                try {
                    myConn.closeConexion();
                } catch (Exception e) {
                	mylib.console(1,"Error Cerrando Conexion: "+e.getMessage());
                }
                break;
            case "SQL":
                break;
            case "HBASE":
                break;
            default:
                break;
        }
    }
    
    public String getSqlFindAgeActive(String iteratorMinute, String posmonth, String posdayOfMonth, String posdayOfWeek, String posweekOfYear, String posweekOfMonth, String posIteratorHour, String posIteratorMinute) {
    	String vSQL=null;
    	switch (gDatos.getInfo().getDbType()) {
    		case "ORA":
	            vSQL = "select "+iteratorMinute+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from tb_diary where "
	                    + "     ageEnable=1 "
	                    + "     and substr(month,"+posmonth+",1) = '1'"
	                    + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
	                    + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
	                    + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
	                    + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
	                    + "     and substr(hourOfDay,"+posIteratorHour +",1) = '1'"
	                    + "     and substr(minute,"+posIteratorMinute +",1) = '1'";
    			break;
    		case "SQL":
	            vSQL = "select "+iteratorMinute+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from tb_diary where "
	                    + "     ageEnable=1 "
	                    + "     and substr(month,"+posmonth+",1) = '1'"
	                    + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
	                    + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
	                    + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
	                    + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
	                    + "     and substr(hourOfDay,"+posIteratorHour +",1) = '1'"
	                    + "     and substr(minute,"+posIteratorMinute +",1) = '1'";
    			break;
    		case "mySQL":
	            vSQL = "select "+iteratorMinute+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from tb_diary where "
	                    + "     ageEnable=1 "
	                    + "     and substr(month,"+posmonth+",1) = '1'"
	                    + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
	                    + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
	                    + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
	                    + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
	                    + "     and substr(hourOfDay,"+posIteratorHour +",1) = '1'"
	                    + "     and substr(minute,"+posIteratorMinute +",1) = '1'";
    			break;
    		default:
    			break;
    	}
    	return vSQL;
    }
}
