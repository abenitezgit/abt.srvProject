package abt.srvProject.solrClient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import abt.srvProject.model.DataResponse;
import abt.srvProject.model.Grabacion;
import abt.srvProject.service.GrabService;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Rutinas;

/**
 * Hello world!
 *
 */
public class App 
{
	static GlobalArea gDatos = new GlobalArea();
	static Rutinas mylib = new Rutinas();
	static DataResponse dResponse = new DataResponse();
    
    public static void main( String[] args )  {
    	Map<String, Grabacion> mapGrab = new HashMap<>();
    	try {
	        System.out.println( "Inicio Consulta" );
	        
	        JSONObject jo = new JSONObject();
	        JSONArray ja = new JSONArray();
	        
	        ja.put("SKILL");
	        
	        jo.put("fechaDesde", "20161101");
	        jo.put("fechaHasta", "20161102");
	        jo.put("lstSkill", ja);
	        
	        String dataInput = jo.toString();
	        
	        gDatos.initComponents(dataInput);
	        
            /**
             * Resuelve el tipo de filtro de consulta
             */
            mylib.console("Analizando tipo de filtro de consulta");
            boolean flagFiltros = true;
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


	        
	        
    	} catch (Exception e) {
    		mylib.console(1,"Error ("+e.getMessage()+")");
    	}
        
    }
    
}
