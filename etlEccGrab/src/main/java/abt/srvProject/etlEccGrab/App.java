package abt.srvProject.etlEccGrab;

import java.util.ArrayList;
import java.util.List;

import abt.srvProject.dataAccess.HBaseDB;
import abt.srvProject.srvRutinas.Rutinas;

/**
 * Hello world!
 *
 */
public class App {
	static Rutinas mylib = new Rutinas();
	
    public static void main( String[] args )
    {
    	try {
			//Conexion y seteo de HBASE
	    	//Connect HBAse using Conf Files
	        HBaseDB hbConn = new HBaseDB();
	        
	        String filePropertiesPath = "/usr/local/hadoop/conf/hadoop.properties";
	        String HBProperty = "ecchdp1";
	        String tbName = "grabaciones";
	        
	        hbConn.setConfig(filePropertiesPath, HBProperty, tbName);
	        
	        mylib.console("Conectado a Hbase...");
	        
			String fecIni = System.getProperty("fecIni"); 
			String fecFin = System.getProperty("fecFin");

	        
	        //Seteando Fechas del Extractor
//        	String fecini 	= "2017-06-01 00:00:00";
//        	String fecterm 	= "2017-06-01 14:00:00";
			
//			//Borrando datos de HBASE
//			List<String> keys = new ArrayList<>();
//			mylib.console("Extrayendo keys");
//			keys = hbConn.getAllKeys();
//			mylib.console("borrando "+keys.size()+ " keys");
//			hbConn.deleteKeys(keys);
//			System.exit(0);
        	
        	//Iniciando copia de datos desde Oracle a HBase
        	
        	mylib.console("COPIANDO DATOS DE GRABACIONES");
        	mylib.console("Fecha Desde: "+fecIni);
        	mylib.console("Fecha Hasta: "+fecFin);

        	DataGrab dg = new DataGrab(fecIni,fecFin);
        	dg.executeDataGrab();
	        hbConn.putRow(dg.getMapGrab());	
	        
	        mylib.console("Termino proceso Copiado Grabaciones");

    	} catch (Exception e) {
    		System.out.println("Error: "+e.getMessage());
    	}
        
    }
}
