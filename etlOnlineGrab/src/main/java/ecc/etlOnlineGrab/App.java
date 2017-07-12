package ecc.etlOnlineGrab;

import java.io.FileInputStream;
import java.util.Properties;

import common.rutinas.Rutinas;
import ecc.services.JobEtlGrab;
import ecc.utiles.GlobalArea;

/**
 * Hello world!
 *
 */
public class App {
	static GlobalArea gDatos = new GlobalArea();
	static Rutinas mylib = new Rutinas();

	//Parametros de control
	static String fileConfig;
	static String clusterName;
	
	public static void main( String[] args ) {
		try {
			/**
			 * Inicia Modulo Leyendo parametros de entrada
			 */
			fileConfig 	= System.getProperty("fileConfig");
			clusterName = System.getProperty("clusterName");
			
			fileConfig = "/usr/local/hadoop/conf/hadoop.properties";
			clusterName = "ecchdp1";
			
			if (!mylib.isNullOrEmpty(fileConfig) && !mylib.isNullOrEmpty(clusterName)) {
				if (initComponents()) {
					mylib.console("Parametros recuperados:");
					mylib.console("FileConfig: "+fileConfig);
					mylib.console("ClusterName: "+clusterName);
					mylib.console("Host: "+gDatos.getGrabDbHost());
					mylib.console("Port: "+gDatos.getGrabDbPort());
					mylib.console("DBName: "+gDatos.getGrabDbName());
					mylib.console("DBUser: "+gDatos.getGrabDbUser());
					mylib.console("DBPass: "+gDatos.getGrabDbPass());
					
					//Inicializando JOB
					Thread jobEtl = new JobEtlGrab(gDatos);
					jobEtl.setName("thJobEtlGrab");
					jobEtl.start();
				} else {
					mylib.console(1,"No se han podido inicializar los componentes");
					mylib.console(1,"Abortando servicio etlOnlineGrab");
				}
			} else {
				mylib.console(1,"No se han ingresado los parametros correctos");
				mylib.console(1,"Abortando servicio etlOnlineGrab");
			}
		} catch (Exception e) {
			mylib.console(1,"Error general. Abortando etlOnlineGrab ("+e.getMessage()+")");
		}
    }
	
	static private boolean initComponents() {
		boolean isExitOk = false;
		try {
			if (mylib.fileExist(fileConfig)) {
				//Abriendo archivo de configuracion
				Properties conf = new Properties();
				conf.load(new FileInputStream(fileConfig));

				//Recuperando datos de parametros
				gDatos.setGrabDbHost(conf.getProperty(clusterName+".grab.dbHost"));
				gDatos.setGrabDbPort(conf.getProperty(clusterName+".grab.dbPort"));
				gDatos.setGrabDbName(conf.getProperty(clusterName+".grab.dbName"));
				gDatos.setGrabDbUser(conf.getProperty(clusterName+".grab.dbUser"));
				gDatos.setGrabDbPass(conf.getProperty(clusterName+".grab.dbPass"));
				gDatos.setDaysBack(Integer.valueOf(conf.getProperty(clusterName+".grab.daysBack")));
				gDatos.setGrabValLoad(Integer.valueOf(conf.getProperty(clusterName+".grab.valLoad")));
				gDatos.setGrabValUpd(Integer.valueOf(conf.getProperty(clusterName+".grab.valUpd")));
				gDatos.setLog4j(conf.getProperty(clusterName+".log4j"));
				gDatos.setFileConfig(fileConfig);
				gDatos.setClusterName(clusterName);
				
				isExitOk = true;
			} else {
				mylib.console(1,"No se encuentra archivo de config");
				isExitOk = false;
			}
			
			return isExitOk;
		} catch (Exception e) {
			mylib.console(1,"Error initComponents: "+e.getMessage());
			return false;
		}
	}
}
