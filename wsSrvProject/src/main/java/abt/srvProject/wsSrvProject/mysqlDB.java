/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abt.srvProject.wsSrvProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ABT
 */
public class mysqlDB {

	Connection connection;
	Statement stm;
    
    String dbHost;
    String dbName;
    String dbPort;
    String dbUser;
    String dbPass;
    int timeOut; 
    boolean connected;
    
    public mysqlDB() {}
    
    public mysqlDB(String dbHost, String dbName, String dbPort, String dbUser, String dbPass, int timeOut) {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.timeOut = timeOut;
    }
    
    /**
     * Getter and Setter
     * @return
     */
    
    
    public String getDbHost() {
		return dbHost;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
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

	
	/**
	 * Procedimientos Internos
	 * @throws SQLException
	 */

    public mysqlDB open() throws Exception {
        
    	//Define la clase de conexion
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception(e.getMessage());
		}
        
        //Crea String de Conexion
    	String StringConnection = "jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName;

        
        //Setea TimeOut en Driver de Conexion
        DriverManager.setLoginTimeout(timeOut);
        
        //Establece la conexion a la BD
        try {
			connection = DriverManager.getConnection(StringConnection,dbUser,dbPass);
		} catch (SQLException e) {
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
    	try {
	    	stm = connection.createStatement();
	    	stm.executeUpdate(upd);
	    	return stm.getUpdateCount();
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public boolean executeQuery(String sql) throws Exception {
    	try {
    		stm = connection.createStatement();
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
