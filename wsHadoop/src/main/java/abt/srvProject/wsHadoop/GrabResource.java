package abt.srvProject.wsHadoop;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;

import abt.srvProject.dataAccess.HBaseDB;
import abt.srvProject.dataAccess.SolRDB;
import abt.srvProject.model.DataGrabacion;
import abt.srvProject.model.DataResponse;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Procedures;
import abt.srvProject.utiles.Rutinas;


@Path("grabData")
public class GrabResource {
	Logger logger = Logger.getLogger("wsHadoop");
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	Procedures myproc = new Procedures(gDatos);
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String grabData(String dataInput) {
		Map<String, String> mapSolrQuery = new HashMap<>();
		//Map<String, String> mapResponse = new HashMap<>();
		HashMap<String, HashMap<String, String>> mapResponse = new HashMap<>();
		Map<String, DataGrabacion> mapResponseObject = new HashMap<>();
		Map<String, String> filters = new HashMap<>();

		try {
			boolean isSuccess = true;
			int errCode=0;
			String errMesg="";
			
			if (gDatos.initComponent()) {
				//Inicializando el log de proceso
				PropertyConfigurator.configure(gDatos.getLog4Config());
				logger.info("Iniciando Servicio de Consulta Grabaciones a Hadoop");
				logger.setLevel(Level.INFO);
				logger.info("Logger SET Level: "+mylib.getLoggerLevel(logger));

				//Parsea parametros de entrada
				try {
					logger.info("Validando parametros de entrada");
					myproc.parseaInputParameters(dataInput);
				} catch (Exception e) {
					logger.error("Error con parametros de entrada: "+e.getMessage());
					errCode = 98;
					errMesg = "Error con parametros de entrada";
					isSuccess = false;
				}
				
				if (isSuccess) {
					//Obtiene string de tipo de consulta (en arreglo de int)
					logger.info("Reconociendo tipo de consulta...");
					int tipoConsulta[] = myproc.getTipoConsulta(gDatos.getDr());
					logger.info("Resultado tipoConsulta: "+tipoConsulta[6]);

					//Si la ultima posicion (6) no es cero se debe reportar error asociado al codigo de retorno
					if (tipoConsulta[6]==0) {
						
						logger.info("Generando los filtros de consulta");
						
						
						filters = myproc.buildSolrFilters(tipoConsulta);
						
						logger.info("Estableciendo conexion a Solr (indexaxion");
						SolRDB solrConn = new SolRDB();
			        	solrConn.setConfig(gDatos.getFileConfig(), gDatos.getHdpCluster());
			        	solrConn.open(gDatos.getSolrCollection());
			        	
			        	if (solrConn.isConnected()) {
			        		logger.info("Generando query hacia solR");
			        		mapSolrQuery = solrConn.getMapQuery(filters);
			        		
			        		if (mapSolrQuery.size()>0) {
				        		logger.info("Generando query hacia hbase");
				        		HBaseDB hbConn = new HBaseDB();
				        		hbConn.setConfig(gDatos.getFileConfig(), gDatos.getHdpCluster(), gDatos.getHdpTbName());
				        		
				        		//String[] family = {"f0","f1","f2","k1"};
				        		String[] family = {"f0","f1","f2","k1"};
				        		mapResponse = hbConn.getRows(mapSolrQuery, family);
				        		
				        		mapResponseObject = myproc.genMapResponseObject(mapResponse);
				        		
			        		} else {
			        			logger.info("No hay data para retornar");
			        		}
			        		
			        		solrConn.close();
			        	} else {
			        		logger.error("No es posible conectarse a cluster solR");
							errCode = 97;
							errMesg = "Error conexion cluster solR";
							isSuccess = false;
			        	}
					} else {
						logger.error("Error validando tipo de consulta");
						errCode = tipoConsulta[6];
						errMesg = "Error validando tipo de consulta";
						isSuccess = false;
					}
				}
			} else {
				mylib.console("Error inicializando componentes");
				errCode = 99;
				errMesg = "Error proceso general. Abortando sentencia";
				isSuccess = false;
			}
			
			//Genera respuesta en formato JSON
//			JSONObject jHeader = new JSONObject();
//			JSONObject jData = new JSONObject();
			
			DataResponse dResponse = new DataResponse();
			
			if (isSuccess) {
            	dResponse.setStatus(0);
				dResponse.setMessage("SUCCESS");
				dResponse.setFilters(mylib.serializeObjectToJSon(filters,false));
				dResponse.setLimit(gDatos.getDr().getLimit());
				dResponse.setNumFound(mapResponse.size());
				dResponse.setData(mapResponse);

			} else {
            	dResponse.setStatus(errCode);
				dResponse.setMessage(errMesg);
			}
			
			return mylib.serializeObjectToJSon(dResponse, false);
			
		} catch (Exception e) {
			JSONObject jHeader = new JSONObject();
			jHeader.append("status", 99);
			jHeader.append("message", "Error generando respuesta ("+e.getMessage()+")");
			return jHeader.toString();
		}
	}

}
