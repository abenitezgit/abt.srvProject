/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abt.srvProject.dataAccess;

import java.sql.Connection;
import java.sql.ResultSet;

import abt.srvProject.model.DbParam;
import abt.srvProject.srvRutinas.Rutinas;

/**
 *
 * @author andresbenitez
 */
public class MetaData {
    DbParam dbParam = new DbParam();
    Rutinas mylib = new Rutinas();
    
    private mysqlDB myConn;
    private sqlDB sqlConn;
    
    
    public MetaData (DbParam dbParam) {
        this.dbParam = dbParam;
    }
    
    public void open() throws Exception{
        switch (dbParam.getDbType()) {
            case "mySQL":
                try {
                    myConn = new mysqlDB(dbParam.getDbHost(), dbParam.getDbName(), String.valueOf(dbParam.getDbPort()), dbParam.getDbUser(), dbParam.getDbPass(), 10);
                    myConn.open();
                } catch (Exception e) {
                	throw new Exception(e.getMessage());
                }
                break;
            case "SQL":
                try {
                    sqlConn = new sqlDB(dbParam.getDbHost(), dbParam.getDbName(), String.valueOf(dbParam.getDbPort()), dbParam.getDbUser(), dbParam.getDbPass(), 10);
                    sqlConn.open();
                } catch (Exception e) {
                	throw new Exception(e.getMessage());
                }
                break;
            default:
                break;
        }
    }
    
    public Connection getConnection() {
    	switch (dbParam.getDbType()) {
    		case "mySQL":
    			return myConn.getConnection();
    		case "SQL":
    			return sqlConn.getConnection();
			default:
				return null;
    	} 
    }
    
    public boolean isConnected() throws Exception {
    	try {
	        switch (dbParam.getDbType()) {
	            case "mySQL":
	                return myConn.isConnected();
	            case "SQL":
	                return sqlConn.isConnected();
	            default:
	                return false;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public boolean executeQuery(String vSQL) throws Exception {
    	try {
	        switch (dbParam.getDbType()) {
	        	case "mySQL":
	    			return myConn.executeQuery(vSQL);
	        	case "SQL":
	    			return sqlConn.executeQuery(vSQL);
	    		default:
	    			return false;
	        }
    	}
        catch (Exception e) {
        	throw new Exception(e.getMessage());
    	}
    }
    
    public ResultSet getQuery() throws Exception{
    	try {
	        switch (dbParam.getDbType()) {
	            case "mySQL":
                    return myConn.getQuery();
	            case "SQL":
                    return sqlConn.getQuery();
	            default:
	                return null;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public int executeUpdate(String upd) throws Exception{
    	try {
	        switch (dbParam.getDbType()) {
	            case "mySQL":
                    return myConn.executeUpdate(upd);
	            case "SQL":
                    return sqlConn.executeUpdate(upd);
	            default:
	                return 0;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public boolean isExistRows() throws Exception {
    	try {
	        switch (dbParam.getDbType()) {
            case "mySQL":
                return myConn.isExistRows();
            case "SQL":
                return sqlConn.isExistRows();
            default:
                return false;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    
    public void close() throws Exception {
    	try {
	        switch (dbParam.getDbType()) {
	            case "mySQL":
	                myConn.close();
	                break;
	            case "SQL":
	                sqlConn.close();
	                break;
	            default:
	                break;
	        }
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
}
