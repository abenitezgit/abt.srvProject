package abt.srvProject.srvClient;

import java.sql.ResultSet;

/**
 * by ABT!
 *
 */
public class appMain 
{
    public static void main( String[] args )
    {
    	try {
	    	mysqlDB dbConn = new mysqlDB("hwk13","srvConf","3306","process","usrProc-01",1);
	    	
	    	dbConn.open();
	    	
	    	if (dbConn.isConnected()) {
	    		System.out.println("Connected");
	    		
	    		System.out.println(dbConn.executeQuery("select count(*) from tb_movtb"));
	    		
	    		ResultSet rs = dbConn.getQuery();
	    		if (rs!=null) {
	    			if (rs.next()) {
	    				System.out.println(rs.getInt(1));
	    			}
	    		}
	    		
	    	}
	    	System.out.println("Termino");
    	}
    	catch (Exception e) {
    		System.out.println("Error: "+e.getMessage());
    	}
    	
        
        
    }
}
