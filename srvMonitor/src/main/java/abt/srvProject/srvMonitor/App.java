package abt.srvProject.srvMonitor;

import java.io.FileInputStream;
import java.util.Properties;

import abt.srvProject.model.Servicio;
import abt.srvProject.service.srvMonitor;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;

public class App {
	static GlobalArea gDatos = new GlobalArea();
	static Rutinas mylib = new Rutinas();
	
    public static void main( String[] args ) {	
		try {
			/**
			 * Inicia Modulo Leyendo parametros de entrada
			 */
			String pathProperties = System.getProperty("properties");
			
			if (pathProperties!=null) {
				Properties fileConf = new Properties();
				fileConf = getFileConf(pathProperties);
				
				parseaProperties(fileConf);

				mylib.console("Iniciando servicio srvMonitor a las "+mylib.getDateNow());
				mylib.console("Parametros Leidos:");
				mylib.console("srvPort: "+gDatos.getServicio().getSrvPort());
				mylib.console("dbHost : "+gDatos.getServicio().getDbHost());
				mylib.console("dbName : "+gDatos.getServicio().getDbName());
				mylib.console("dbType : "+gDatos.getServicio().getDbType());
				mylib.console("dbUser : "+gDatos.getServicio().getDbUser());
				mylib.console("pathProperties: "+pathProperties);
				mylib.console("log4jName: "+gDatos.getServicio().getLog4jName());
				mylib.console("log4jPath: "+gDatos.getServicio().getLog4jPath());
				
				mylib.console("Iniciando srvMonitor");
				Thread thMain = new srvMonitor(gDatos);
				thMain.setName("srvMonitor");
				thMain.start();
			} else {
				mylib.console(1,"No ha ingresado la ruta del archivo de configuracion");
				mylib.console(1,"Abortando servicios");
			}
		} catch (Exception e) {
			mylib.console(1,"Error general. Abortando servicio ("+e.getMessage()+")");
		}
    }
    
    static void parseaProperties(Properties fileConf) throws Exception {
    	Servicio servicio = new Servicio();
    	
    	servicio.setAgeGapMinute(Integer.valueOf(fileConf.getProperty("ageGapMinute")));
    	servicio.setAgeShowHour(Integer.valueOf(fileConf.getProperty("ageShowHour")));
    	servicio.setAuthKey(fileConf.getProperty("authKey"));
    	servicio.setDbHost(fileConf.getProperty("dbHost"));
    	servicio.setDbInstance(fileConf.getProperty("dbInstance"));
    	servicio.setDbName(fileConf.getProperty("dbName"));
    	servicio.setDbPass(fileConf.getProperty("dbPass"));
    	servicio.setDbPort(Integer.valueOf(fileConf.getProperty("dbPort")));
    	servicio.setDbType(fileConf.getProperty("dbType"));
    	servicio.setDbUser(fileConf.getProperty("dbUser"));
    	servicio.setSrvPort(Integer.valueOf(fileConf.getProperty("srvPort")));
    	servicio.setTxpMain(Integer.valueOf(fileConf.getProperty("txpMain")));
    	servicio.setTxtKeep(Integer.valueOf(fileConf.getProperty("txpKeep")));
    	servicio.setLog4jName(fileConf.getProperty("log4jName"));
    	servicio.setLog4jPath(fileConf.getProperty("log4jPath"));
    	servicio.setPathProperties(System.getProperty("properties"));
    	
    	//Inicializa Variables iniciales
    	servicio.setEnable(true);  //Esta variable de control puede ser actualizada desde la metadata
    	
    	gDatos.setServicio(servicio);
    	
    }
    
    static private Properties getFileConf(String pathProperties) throws Exception {
		Properties conf = new Properties();
		conf.load(new FileInputStream(pathProperties));
		return conf;
    }
    
    
}
