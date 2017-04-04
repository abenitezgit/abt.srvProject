package abt.srvProject.srvServer;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import abt.srvProject.model.Info;
import abt.srvProject.model.Service;
import abt.srvProject.service.srvServer;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;

/**
 * Hello world!
 *
 */
public class AppSrv {
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

				mylib.console("Iniciando servicio srvServer a las "+mylib.getDateNow());
				mylib.console("Parametros Leidos:");
				mylib.console("Enable: "+gDatos.getService().isEnable());
				mylib.console("srvId: "+gDatos.getInfo().getSrvId());
				mylib.console("srvIp: "+gDatos.getInfo().getSrvIp());
				mylib.console("srvPort: "+gDatos.getInfo().getSrvPort());
				mylib.console("pathProperties: "+gDatos.getInfo().getPathProperties());
				mylib.console("fileProperties: "+gDatos.getInfo().getFileProperties());
				mylib.console("logProperties: "+gDatos.getInfo().getLogProperties());
				mylib.console("service-info: "+mylib.serializeObjectToJSon(gDatos.getService(), false));
				
				mylib.console("Iniciando srvServer");
				Thread thMain = new srvServer(gDatos);
				thMain.setName("srvServer");
				thMain.start();
			} else {
				mylib.console(1,"No ha ingresado la ruta del archivo de configuracion");
				mylib.console(1,"Abortando servicios");
			}
		} catch (Exception e) {
			mylib.console(1,"Error general. Abortando servicio ("+e.getMessage()+")");
		}

    	
    }
    
    static void parseaProperties(Properties fileConf, Map<String,String> mainParam) throws Exception {
    	try {
	    	Info info = new Info();
	    	
	    	//Parametros de informacion del servicio
	    	info.setSrvId(fileConf.getProperty("srvId"));
	    	info.setSrvIp(fileConf.getProperty("srvIp"));
	    	info.setSrvPort(Integer.valueOf(fileConf.getProperty("srvPort")));
	    	
	    	//Variables de identificacion del monitor (master)
	    	info.setSrvMonIp(fileConf.getProperty("srvMonIp"));
	    	info.setSrvMonPort(Integer.valueOf(fileConf.getProperty("srvMonPort")));
	    	info.setSrvSMonIp(fileConf.getProperty("srvSMonIp"));
	    	info.setSrvSMonPort(Integer.valueOf(fileConf.getProperty("srvSMonPort")));
	    	
	    	//Parametros de conrol de ciclo de schedule
	    	info.setTxpMain(Integer.valueOf(fileConf.getProperty("txpMain")));
	    	info.setTxpSync(Integer.valueOf(fileConf.getProperty("txpSync")));
	    	info.setTxpRun(Integer.valueOf(fileConf.getProperty("txpRun")));
	    	
	    	//Clave de intercambio de mensajes
	    	info.setAuthKey(fileConf.getProperty("authKey"));
	    	
	    	//Filename log4j
	    	info.setLogProperties(fileConf.getProperty("logProperties"));
	
	    	//Parametros de archivo de properties y log  -- Parametros de entrada
	    	info.setPathProperties(mainParam.get("pathProperties"));
	    	info.setFileProperties(mainParam.get("fileProperties"));
	    	
	    	//Actualiza GlobalArea.info
	    	gDatos.setInfo(info);
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    static private void initComponent() throws Exception {
    	try {
	    	Map<String,Object> srv = new HashMap<>();
	    	srv.put("srvId", gDatos.getInfo().getSrvId());
	    	srv.put("enable", true);
	    	srv.put("srvIp", gDatos.getInfo().getSrvIp());
	    	srv.put("srvPort", gDatos.getInfo().getSrvPort());
	    	srv.put("activePrimaryMonitor", true);
	    	
	    	gDatos.updateService(srv);
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    	
    }
    
    static private Properties getFileConf(Map<String,String> mainParam) throws Exception {
    	try {
			Properties conf = new Properties();
			conf.load(new FileInputStream(mainParam.get("pathProperties")+"/"+mainParam.get("fileProperties")));
			return conf;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
}
