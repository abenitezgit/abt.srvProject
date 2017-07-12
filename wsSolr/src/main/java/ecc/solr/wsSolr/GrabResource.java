package ecc.solr.wsSolr;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import ecc.solr.model.DataResponse;
import ecc.solr.model.Grabacion;
import ecc.solr.services.GrabService;
import ecc.solr.utiles.GlobalArea;
import ecc.solr.utiles.Rutinas;

/**
 * 
 * @author admin
 * Post url: http://localhost:8080/wsSolr/webapi/grabaciones
 */
@Path("grabaciones")
public class GrabResource {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	DataResponse dResponse = new DataResponse();
	Map<String, Grabacion> mapGrab = new HashMap<>();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String grabaciones(String dataInput) {
		boolean flagFiltros = true;
		
		mylib.console("Iniciando extraccion de grabaciones via POST");
		mylib.console("DataInput: "+dataInput);
		
		mylib.console("Inicializando componentes...");
		gDatos.initComponents(dataInput);
		
		//Respuesta Default
		dResponse.setStatus(99);
		dResponse.setMessage("Error general");
		
		if (gDatos.isFlagInitComponents()) {
			mylib.console("Componentes inicializados!");
			
            /**
             * Resuelve el tipo de filtro de consulta
             */
            mylib.console("Analizando tipo de filtro de consulta");
            int tipoConsulta = gDatos.getTipoConsulta();
            switch (tipoConsulta) {
                case 1:
                    mylib.console("Filtro de Consulta: ConnID sin Fechas");
                    break;
                case 2:
                    mylib.console("Filtro de Consulta: ConnID con rango de fechas");
                    break;
                case 3:
                    mylib.console("Filtro de Consulta: UniqueID");
                    break;
                case 4:
                    mylib.console("Filtro de Consulta: Agente");
                    break;
                case 5:
                    mylib.console("Filtro de Consulta: ANI con rango de fechas");
                    break;
                case 6:
                    mylib.console("Filtro de Consulta: Ani sin fechas");
                    break;
                case 7:
                    mylib.console("Filtro de Consulta: Dnis con rango de fechas");
                    break;
                case 8:
                    mylib.console("Filtro de Consulta: Dnis sin fechas");
                    break;
                case 9:
                    mylib.console("Filtro de Consulta: Busqueda masiva por fechas");
                    break;
                case 97:
                    mylib.console(1, "Error Filtro de Consulta: Debe ingresar al menos un rango de fechas si es que no ha seleccionado ningun otro valor");
                    dResponse.setStatus(97);
                    dResponse.setMessage("Error Filtro de Consulta: Debe ingresar al menos un rango de fechas si es que no ha seleccionado ningun otro valor");
                    flagFiltros=false;
                    break;
                case 98:
                    mylib.console(1, "Error Filtro de Consulta: Debe ingresar al menos un SKILL");
                    dResponse.setStatus(98);
                    dResponse.setMessage("Error Filtro de Consulta: Debe ingresar al menos un SKILL");
                    flagFiltros=false;
                    break;
                case 99:
                    mylib.console(1, "Error Filtro de Consulta: Error de Ejecución");
                    dResponse.setStatus(99);
                    dResponse.setMessage("Error Filtro de Consulta: Error de Ejecución");
                    flagFiltros=false;
                    break;
                default:
                    mylib.console(1, "Error Filtro de Consulta: Error de parametros de entrada");
                    dResponse.setStatus(94);
                    dResponse.setMessage("Error Filtro de Consulta: Error de parametros de entrada");
                    flagFiltros=false;
                    break;
            }
            
            if (flagFiltros) {
            	mylib.console("Inicio Ejecucion getGrabaciones");
            	GrabService srvGrab = new GrabService(gDatos);
            	
            	mapGrab = srvGrab.getGrabaciones(tipoConsulta);
            	if (mapGrab.size()==0) {
            		dResponse.setStatus(0);
            		dResponse.setMessage("No se encontraron grabaciones");
            		dResponse.setNumFound(0);
            		mylib.console(2,"No se encontraron grabaciones");
            	} else {
	            	dResponse.setStatus(0);
					dResponse.setMessage("SUCCESS");
					dResponse.setLimit(gDatos.getDr().getLimit());
					dResponse.setNumFound(mapGrab.size());
					dResponse.setData(mapGrab);
            	}
            }
		} else {
			dResponse.setStatus(96);
			dResponse.setMessage("Error inicializando componentes");
			mylib.console(1,"Error inicializando componentes");
			//return mylib.serializeObjectToJSon(dResponse, false);
		}
		
		try {
			return mylib.serializeObjectToJSon(dResponse, false);
		} catch (Exception e) {
			JSONObject joHeader = new JSONObject();
			joHeader.append("status", 95);
			joHeader.append("message", "Error generando respuesta ("+e.getMessage()+")");
			return joHeader.toString();
		}
	}

}
