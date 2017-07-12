package eco.hbase.appClient;

import java.util.Date;
import java.util.HashMap;

import abt.srvProject.srvRutinas.Rutinas;
import eco.hbase.dataAccess.HBaseDB;
import eco.hbase.services.DataC2C;
import eco.hbase.services.DataEcoOper;
import eco.hbase.services.DataGrab;
import eco.hbase.services.DataOreka;

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
    		boolean loadGrab = true;
    		boolean loadOreka = false;
    		boolean loadc2c = true;
    		boolean loadVtr = false;
    		
    		boolean delGrab = false;
    		boolean delOreka = false;
    		boolean delc2c = false;
    		boolean delVtr = false;
    		
        	String fecini 	= "2016-12-01 00:00:00";
        	String fecterm 	= "2017-01-01 00:00:00";
    		
    		//Conexion y seteo de HBASE
	    	//Connect HBAse using Conf Files
	        HBaseDB hbConn = new HBaseDB();
	        
	        String filePropertiesPath = "/usr/local/hadoop/conf/hadoop.properties";
	        String HBProperty = "ecoprod01";
	        String tbName = "grabdata";
	        
	        hbConn.setConfig(filePropertiesPath, HBProperty, tbName);
	        
	        mylib.console("Conectado a Hbase...");
	        
        	Date dFecIni = mylib.getDate(fecini, "yyyy-MM-dd HH:mm:SS");
        	Date dFecTerm = mylib.getDate(fecterm, "yyyy-MM-dd HH:mm:SS");
        	
        	int dias= mylib.getDaysDiff(dFecTerm, dFecIni);
        	
        	Date fecItIni = dFecIni;
        	Date fecItFin = dFecIni;
        	
        	for (int i=0; i < dias; i++) {
        		fecItIni = mylib.getDateAddDays(dFecIni,i);
        		fecItFin = mylib.getDateAddDays(fecItIni,1);
        		if (i==(dias-1)) {
        			fecItFin = dFecTerm;
        		}
        		
        		String sfecItIni = mylib.getStringDate(fecItIni, "yyyy-MM-dd HH:mm:SS");
        		String sfecItFin = mylib.getStringDate(fecItFin, "yyyy-MM-dd HH:mm:SS");
        		
        		mylib.console("fecini: "+sfecItIni);
        		mylib.console("fecfin: "+sfecItFin);
        		
    	        //Inicia ciclo de fechas por día
        		
        		//Borra datos cargados en HBASE
    	        if (delGrab) {
    	        	mylib.console("DELETE GRAB - en HBASE");
    	        	mylib.console("Fecha Desde: "+sfecItIni);
    	        	mylib.console("Fecha Hasta: "+sfecItFin);
    	        	String org = "2";
    	        	String suborg = "0";
    	        	DataGrab dg = new DataGrab(sfecItIni,sfecItFin,org,suborg);
    	        	
    	        	hbConn.deleteKeys(dg.getOnlyKeys());
    		        
    		        mylib.console("Termino proceso Delete GRAB");
    	        }

        		//Borra datos cargados en HBASE
    	        if (delOreka) {
    	        	mylib.console("DELETE Oreka - en HBASE");
    	        	mylib.console("Fecha Desde: "+sfecItIni);
    	        	mylib.console("Fecha Hasta: "+sfecItFin);
    	        	String org = "2";
    	        	String suborg = "1";
    	        	DataOreka dg = new DataOreka(sfecItIni,sfecItFin,org,suborg);
    	        	
    	        	hbConn.deleteKeys(dg.getOnlyKeys());
    		        
    		        mylib.console("Termino proceso Delete OREKA");
    	        }
    	        
        		//Borra datos cargados en HBASE
    	        if (delc2c) {
    	        	mylib.console("DELETE C2C - en HBASE");
    	        	mylib.console("Fecha Desde: "+sfecItIni);
    	        	mylib.console("Fecha Hasta: "+sfecItFin);
    	        	String org = "2";
    	        	String suborg = "2";
    	        	DataC2C dg = new DataC2C(sfecItIni,sfecItFin,org,suborg);
    	        	
    	        	hbConn.deleteKeys(dg.getOnlyKeys());
    		        
    		        mylib.console("Termino proceso Delete C2C");
    	        }
    	        
        		//Borra datos cargados en HBASE
    	        if (delVtr) {
    	        	mylib.console("DELETE VTR - en HBASE");
    	        	mylib.console("Fecha Desde: "+sfecItIni);
    	        	mylib.console("Fecha Hasta: "+sfecItFin);
    	        	String org = "3";
    	        	String suborg = "0";
    	        	DataEcoOper dg = new DataEcoOper(sfecItIni,sfecItFin,org,suborg);
    	        	
    	        	hbConn.deleteKeys(dg.getOnlyKeys());
    		        
    		        mylib.console("Termino proceso Delete VTR");
    	        }
	        	
        		//Extrae datos de tabla "grabaciones"
    	        if (loadGrab) {
    	        	mylib.console("ETL CLARO - tabla grabaciones");
    	        	mylib.console("Fecha Desde: "+sfecItIni);
    	        	mylib.console("Fecha Hasta: "+sfecItFin);
    	        	String org = "2";
    	        	String suborg = "0";
    	        	DataGrab dg = new DataGrab(sfecItIni,sfecItFin,org,suborg);
    	        	dg.executeDataGrab();
    		        hbConn.putRow(dg.getMapGrab());
    		        
    		        mylib.console("Total de Rows insertadas en HBASE: "+dg.getMapGrab().size());
    	        }
    	    	
        		//Extrae datos de tabla "base_oreka"
    	        if (loadOreka) {
    	        	mylib.console("ETL CLARO - tabla call_record base oreka");
    	        	mylib.console("Fecha Desde: "+sfecItIni);
    	        	mylib.console("Fecha Hasta: "+sfecItFin);
    	        	String org = "2";
    	        	String suborg = "1";
    	        	DataOreka dg = new DataOreka(sfecItIni,sfecItFin,org,suborg);
    	        	dg.executeDataGrab();
    		        hbConn.putRow(dg.getMapGrab());
    		        
    		        mylib.console("Total de Rows insertadas en HBASE: "+dg.getMapGrab().size());
    	        }

        		//Extrae datos de tabla "call_record"
    	        if (loadc2c) {
    	        	mylib.console("ETL CLARO - C2C ");
    	        	mylib.console("Fecha Desde: "+sfecItIni);
    	        	mylib.console("Fecha Hasta: "+sfecItFin);
    	        	String org = "2";
    	        	String suborg = "2";
    	        	DataC2C dc = new DataC2C(sfecItIni,sfecItFin,org,suborg);
    	        	dc.executeDataGrab();
    		        hbConn.putRow(dc.getMapGrab());
    		        
    		        mylib.console("Total de Rows insertadas en HBASE: "+dc.getMapGrab().size());
    	        }

        		//Extrae datos de tabla "call_record"
    	        if (loadVtr) {
    	        	mylib.console("ETL VTR - tabla call_record ECO_OPER S1");
    	        	mylib.console("Fecha Desde: "+sfecItIni);
    	        	mylib.console("Fecha Hasta: "+sfecItFin);
    	        	String org = "3";
    	        	String suborg = "0";
    	        	DataEcoOper dc = new DataEcoOper(sfecItIni,sfecItFin,org,suborg);
    	        	dc.executeDataGrab();
    		        hbConn.putRow(dc.getMapGrab());
    		        
    		        mylib.console("Total de Rows insertadas en HBASE: "+dc.getMapGrab().size());
    	        }
        		
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
