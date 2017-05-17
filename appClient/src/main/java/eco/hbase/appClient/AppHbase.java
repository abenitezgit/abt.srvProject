package eco.hbase.appClient;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abt.srvProject.srvRutinas.Rutinas;
import eco.hbase.dataAccess.HBaseDB;
import eco.hbase.dataAccess.sqlDB;
import eco.hbase.model.RowModel;
import eco.hbase.services.DataC2C;
import eco.hbase.services.DataGrab;

/**
 * Hello world!
 *
 */
public class AppHbase {
	static Rutinas mylib = new Rutinas();
	
	
    public static void main( String[] args )
    {
    	try {
    		//parametros de control
    		boolean borra = false;
    		boolean loadGrab = true;
    		boolean loadOreka = false;
    		boolean loadc2c = false;
    		
    		
    		//Conexion y seteo de HBASE
	    	//Connect HBAse using Conf Files
	        HBaseDB hbConn = new HBaseDB();
	        
	        String filePropertiesPath = "/usr/local/hadoop/conf/hadoop.properties";
	        String HBProperty = "ecoprod01";
	        String tbName = "grabdata";
	        
	        hbConn.setConfig(filePropertiesPath, HBProperty, tbName);
	        
	        mylib.console("Conectado a Hbase...");
	        
	        
	        //Borra todos los datos de hbase grabdata
	        if (borra) {
		        mylib.console("Eiminando todos los registros de la tabla: "+tbName);
		        
		        hbConn.deleteKeys(hbConn.getAllKeys());
		        
		        mylib.console("Registros Eliminados");
	        }

    		
    		//Extrae datos de tabla "grabaciones"
	        if (loadGrab) {
	        	DataGrab dg = new DataGrab("2017-02-10 00:00:00","2017-03-01 00:00:00");
	        	dg.executeDataGrab();
		        hbConn.putRow(dg.getMapGrab());
		        
		        mylib.console("Total de Rows insertadas en HBASE: "+dg.getMapGrab().size());
	        }
	    	

    		//Extrae datos de tabla "call_record"
	        if (loadc2c) {
	        	DataC2C dc = new DataC2C("2016-12-01 00:00:00","2016-12-31 23:59:59");
	        	dc.executeDataGrab();
		        hbConn.putRow(dc.getMapGrab());
		        
		        mylib.console("Total de Rows insertadas en HBASE: "+dc.getMapGrab().size());
	        }

	        
	        
	        System.exit(0);
	        
	        
	        //Consulta si existe tabla
	        
	        
	        boolean existe;
	        
	        if (true) {
	            existe = hbConn.isTableAvailable();
	            
	            System.out.println("tabla existe?: "+existe);
	        	
	        }
        
        	String key = "99917836301";
            existe = hbConn.isRowFound(key);
            
            System.out.println("row existe?: "+existe);
            
            String[] family = {"f0"};
            
            HashMap<String, HashMap<String,String>> mapRowModel =  hbConn.getRows(key, family);
        	
            System.out.println(mylib.serializeObjectToJSon(mapRowModel, true));

            //HashMap<String, HashMap<String,String>> mapRowModel2 = hbConn.scan(family, 10000);
            
            //System.out.println(mylib.serializeObjectToJSon(mapRowModel2, true));
            
            //System.out.println("numRows: "+mapRowModel2.size());
	        
        
    	} catch (Exception e) {
    		mylib.console(1,"Error : "+e.getMessage());
    	}
        
    }

}
