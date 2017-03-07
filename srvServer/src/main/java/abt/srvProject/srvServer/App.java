package abt.srvProject.srvServer;

import java.io.FileInputStream;
import java.util.Properties;

import abt.srvProject.model.Servicio;
import abt.srvProject.service.srvServer;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;

/**
 * Hello world!
 *
 */
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

				mylib.console("Iniciando servicio srvServer a las "+mylib.getDateNow());
				mylib.console("Parametros Leidos:");
				mylib.console("srvPort: "+gDatos.getServicio().getSrvPort());
				mylib.console("dbHost : "+gDatos.getServicio().getDbHost());
				mylib.console("dbName : "+gDatos.getServicio().getDbName());
				mylib.console("dbType : "+gDatos.getServicio().getDbType());
				mylib.console("dbUser : "+gDatos.getServicio().getDbUser());
				mylib.console("pathProperties: "+pathProperties);
				mylib.console("log4jName: "+gDatos.getServicio().getLog4jName());
				mylib.console("log4jPath: "+gDatos.getServicio().getLog4jPath());
				
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
    
    static void parseaProperties(Properties fileConf) throws Exception {
    	Servicio servicio = new Servicio();
    	
    	servicio.setAuthKey(fileConf.getProperty("authKey"));
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
