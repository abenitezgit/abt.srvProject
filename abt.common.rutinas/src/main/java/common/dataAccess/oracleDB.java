/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.dataAccess;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 *
 * @author ABT
 */
public class oracleDB {

	Connection connection;
	Statement stm;
        
    String dbHost;
    String dbName;
    String dbPort;
    String dbUser;
    String dbPass;
    int timeOut;
    boolean connected;

    public oracleDB() {
    }
    
    public oracleDB(String dbHost, String dbName, String dbPort, String dbUser, String dbPass, int timeOut) {
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		this.timeOut = timeOut;
    }
    
	public boolean isConnected() throws Exception {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
    
    public oracleDB open() throws Exception  {
        
    	//Define la clase de la conexión
        try {
        	Class.forName("oracle.jdbc.OracleDriver");
        } catch (Exception e) {
        	throw new Exception(e.getMessage());
        }
        
        //Crea string de conexión
        
        String StringConnection = "jdbc:oracle:thin:@"+dbHost+":"+dbPort+":"+dbName;
        
        //Setea TimeOut en Driver de Conexion
        DriverManager.setLoginTimeout(timeOut);
        
        //Establece conexión a la BD
        try {
            connection = DriverManager.getConnection(StringConnection, dbUser, dbPass);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    
        return this;
    } 
    
	public void close() throws Exception {
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	public int executeUpdate(String upd) throws Exception {
		int updateCount=0;
		try {
			stm = connection.createStatement();
	    	stm.executeUpdate(upd);
	    	updateCount = stm.getUpdateCount();
	    	stm.close();
	    	return updateCount;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
    
	 public boolean executeQuery(String sql) throws Exception {
	    	try {
	    		stm = connection.createStatement(); //.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	    		return stm.execute(sql);
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
	    }
	 
	 public ResultSet getQuery() throws Exception {
	    	try {
	    		return stm.getResultSet();
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
	    }

	 public boolean isExistRows() throws Exception {
	    	try {
	    		boolean result;
	    		if (stm.getResultSet()!=null) {
	    			if (stm.getResultSet().next()) {
	    				result = true;
	    			} else {
	    				result = false;
	    			}
	    		} else {
	    			result = false;
	    		}
	    		
	    		return result;
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
	    }
}
