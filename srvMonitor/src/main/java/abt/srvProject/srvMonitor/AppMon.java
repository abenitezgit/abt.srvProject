package abt.srvProject.srvMonitor;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import abt.srvProject.model.Info;
import abt.srvProject.service.srvMonitor;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;

public class AppMon {
	static GlobalArea gDatos = new GlobalArea();
	static Rutinas mylib = new Rutinas();
	
    public static void main( String[] args ) {	
		try {
			/**
			 * Inicia Modulo Leyendo parametros de entrada
			 */
			Map<String, String> mainParam = new HashMap<>();
			mainParam.put("pathProperties", System.getProperty("path"));
			mainParam.put("fileProperties", System.getProperty("file"));
			
			if (	mainParam.get("pathProperties")!=null &&
					mainParam.get("fileProperties")!=null ) {
				Properties fileConf = new Properties();
				fileConf = getFileConf(mainParam);
				
				parseaProperties(fileConf, mainParam);
				
				initComponent();

				mylib.console("Iniciando servicio srvMonitor a las "+mylib.getDateNow());
				mylib.console("Parametros Leidos:");
				mylib.console("Enable: "+gDatos.getMapService().get(gDatos.getInfo().getSrvId()).isEnable());
				mylib.console("srvId : "+gDatos.getInfo().getSrvId());
				mylib.console("srvIp: "+gDatos.getInfo().getSrvIp());
				mylib.console("srvPort: "+gDatos.getInfo().getSrvPort());
				mylib.console("dbHost : "+gDatos.getInfo().getDbHost());
				mylib.console("dbName : "+gDatos.getInfo().getDbName());
				mylib.console("dbType : "+gDatos.getInfo().getDbType());
				mylib.console("dbUser : "+gDatos.getInfo().getDbUser());
				mylib.console("pathProperties: "+gDatos.getInfo().getPathProperties());
				mylib.console("fileProperties: "+gDatos.getInfo().getFileProperties());
				mylib.console("logProperties: "+gDatos.getInfo().getLogProperties());
				
				mylib.console("Iniciando srvMonitor");
				Thread thMain = new srvMonitor(gDatos);
				thMain.setName("thSrvMonitor");
				thMain.start();
			} else {
				mylib.console(1,"No se han ingresado los parametros correctos");
				mylib.console(1,"Abortando servicios");
			}
		} catch (Exception e) {
			mylib.console(1,"Error general. Abortando servicio ("+e.getMessage()+")");
		}
    }
    
    static void parseaProperties(Properties fileConf, Map<String,String> mainParam) throws Exception {
    	Info info = new Info();
    	
    	//Parametros de identificacion del servicio
    	info.setSrvId(fileConf.getProperty("srvId"));
    	info.setSrvIp(fileConf.getProperty("srvIp"));
    	info.setSrvPort(Integer.valueOf(fileConf.getProperty("srvPort")));
    	
    	//Parametro de intercambio de key
    	info.setAuthKey(fileConf.getProperty("authKey"));
    	
    	//Parametros para recuperar agendas
    	info.setAgeGapMinute(Integer.valueOf(fileConf.getProperty("ageGapMinute")));
    	info.setAgeShowHour(Integer.valueOf(fileConf.getProperty("ageShowHour")));

    	//Parametos de tiempos de control
    	info.setTxpMain(Integer.valueOf(fileConf.getProperty("txpMain")));
    	info.setTxpSync(Integer.valueOf(fileConf.getProperty("txpSync")));
    	info.setTxpIns(Integer.valueOf(fileConf.getProperty("txpIns")));
    	info.setTxpDB(Integer.valueOf(fileConf.getProperty("txpDB")));
    	
    	//Parametros de conexion a Metadata
    	info.setDbHost(fileConf.getProperty("dbHost"));
    	info.setDbName(fileConf.getProperty("dbName"));
    	info.setDbPort(Integer.valueOf(fileConf.getProperty("dbPort")));
    	info.setDbType(fileConf.getProperty("dbType"));
    	info.setDbUser(fileConf.getProperty("dbUser"));
    	info.setDbPass(fileConf.getProperty("dbPass"));
    	info.setDbInstance(fileConf.getProperty("dbInstance"));
    	
    	//Parametros de archivo properties y log
    	info.setPathProperties(mainParam.get("pathProperties"));
    	info.setFileProperties(mainParam.get("fileProperties"));
    	info .setLogProperties(fileConf.getProperty("logProperties"));
    	
    	//Actualiza variable global GlobalArea.info
    	gDatos.setInfo(info);
    	    	
    }
    
    static private void initComponent() throws Exception{
    	Map<String,Object> srv = new HashMap<>();
    	srv.put("srvId", gDatos.getInfo().getSrvId());
    	srv.put("enable", true);
    	srv.put("srvIp", gDatos.getInfo().getSrvIp());
    	srv.put("srvPort", gDatos.getInfo().getSrvPort());
    	
    	gDatos.updateService(srv);
    	
    	//Inicializa variable global de sync metadata
    	gDatos.setSyncMetadata(false);
    	
    }
    
    static private Properties getFileConf(Map<String,String> mainParam) throws Exception {
		Properties conf = new Properties();
		conf.load(new FileInputStream(mainParam.get("pathProperties")+"/"+mainParam.get("fileProperties")));
		return conf;
    }
    
    
}
