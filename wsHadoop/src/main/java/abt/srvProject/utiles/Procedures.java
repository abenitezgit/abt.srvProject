package abt.srvProject.utiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

import abt.srvProject.model.DataGrabacion;
import abt.srvProject.model.DataRequest;

public class Procedures {
	Rutinas mylib = new Rutinas();
	GlobalArea gDatos;
	
	public Procedures(GlobalArea m) {
		gDatos = m;
	}
	
	private String buildFixedQuery() {
		String filter1 = String.format("org:%s", gDatos.getDr().getOrg());
		String filter2 = String.format("fecini:[%s TO %s]", gDatos.getDr().getFechaDesde(), gDatos.getDr().getFechaHasta());
		return String.format("(%s) AND (%s)", filter1, filter2);
	}
	
	private String buildSubOrgQuery() {
		String filter = String.format("suborg:%s", gDatos.getDr().getSuborg());
		return filter;
	}
	
	private String buildTextQuery() {
		String filter1 = String.format("%s", gDatos.getDr().getQrytext());
		return filter1;
	}
	
	private String buildFnameQuery() {
		String filter1 = String.format("fname:%s", gDatos.getDr().getFname());
		return filter1;
	}

	private String buildAniQuery() {
		String filter1 = String.format("ani:%s", gDatos.getDr().getAni());
		return filter1;
	}

	private String buildDnisQuery() {
		String filter1 = String.format("dnis:%s", gDatos.getDr().getDnis());
		return filter1;
	}

	private String buildDuracionQuery() {
		String filter1 = String.format("duracion:%s", gDatos.getDr().getDuracion());
		return filter1;
	}
	
	public Map<String, String> buildSolrFilters(int[] opcionConsulta) throws Exception {
		try {
            /**
             * Se analizan las combinaciones de datos para establecer
             * el tipo de consulta a realizar
             * Setea la variable global dr (DataRequest)
             * 
             * Los tipos de resultados llenan una cadena de resultados por pisicion de la siguiente forma
             * pos 0: org
             * pos 1: suborg
             * pos 2: fechaDesde
             * pos 3: fechaHasta
             * pos 4: fname
             * pos 5: queryText
             * pos 6: codigoError
             * pos 7: ani
             * pos 8: dnis
             * pos 9: duracion
             * return 98: Error: Debe ingresar al menos un cliente y rango de fechas
             * return 99: Error de Ejecución
             */

			Map<String, String> mapFilters = new HashMap<>();
			//Genera q por default
			String q = "*:*";
			
			//fq org, fecini, fecterm
			String fqFixes = buildFixedQuery();
			
			if (opcionConsulta[5]==1) {
				//Genera los q para los textos
				q = buildTextQuery();
			}
			
			//Parmetros de consulta dinamicos
			String fq = fqFixes;
			
			if (opcionConsulta[1]==1) {
				String filter = buildSubOrgQuery();
				fq = String.format("(%s) AND (%s)", fq, filter);
				
			}
			
			if (opcionConsulta[4]==1) {
				String filter = buildFnameQuery();
				fq = String.format("(%s) AND (%s)", fq, filter);
				
			}
			
			if (opcionConsulta[7]==1) {
				String filter = buildAniQuery();
				fq = String.format("(%s) AND (%s)", fq, filter);
				
			}
			
			if (opcionConsulta[8]==1) {
				String filter = buildDnisQuery();
				fq = String.format("(%s) AND (%s)", fq, filter);
				
			}

			if (opcionConsulta[9]==1) {
				String filter = buildDuracionQuery();
				fq = String.format("(%s) AND (%s)", fq, filter);
				
			}
			
			mapFilters.put("q", q);
			mapFilters.put("fq", fq);
			mapFilters.put("rows", String.valueOf(gDatos.getDr().getLimit()));
			
			mapFilters.put("key", "id");
			
			return mapFilters;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<String, DataGrabacion> genMapResponseObject(HashMap<String, HashMap<String, String>> mapResponse) throws Exception {
		try {
			Map<String, DataGrabacion> map = new HashMap<>();
			DataGrabacion dresp = new DataGrabacion();
			
			for (Entry<String, HashMap<String, String>> entry : mapResponse.entrySet()) {
				dresp = new DataGrabacion();
				String resp = mylib.serializeObjectToJSon(entry.getValue(), false);
				dresp = (DataGrabacion) mylib.serializeJSonStringToObject(resp, DataGrabacion.class);
				map.put(entry.getKey(), dresp);
			}
			
			return map;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void parseaInputParameters(String dataInput) throws Exception {
		try {
			DataRequest dr = new DataRequest();
			dr = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
			gDatos.setDr(dr);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
    public int[] getTipoConsulta(DataRequest dr) {
    	int[] tipoConsulta= {0,0,0,0,0,0,99,0,0,0};
    	
        try {
            /**
             * Se analizan las combinaciones de datos para establecer
             * el tipo de consulta a realizar
             * Setea la variable global dr (DataRequest)
             * 
             * Los tipos de resultados llenan una cadena de resultados por pisicion de la siguiente forma
             * pos 0: org
             * pos 1: suborg
             * pos 2: fechaDesde
             * pos 3: fechaHasta
             * pos 4: fname
             * pos 5: queryText
             * pos 6: codigoError
             * pos 7: ani
             * pos 8: dnis
             * pos 9: duracion
             * return 98: Error: Debe ingresar al menos un cliente y rango de fechas
             * return 99: Error de Ejecución
             */

        	if (!mylib.isNullOrEmpty(dr.getOrg())) { tipoConsulta[0] = 1; }
        	if (!mylib.isNullOrEmpty(dr.getSuborg())) { tipoConsulta[1] = 1; }
        	if (!mylib.isNullOrEmpty(dr.getFechaDesde())) { tipoConsulta[2] = 1; }
        	if (!mylib.isNullOrEmpty(dr.getFechaHasta())) { tipoConsulta[3] = 1; }
        	if (!mylib.isNullOrEmpty(dr.getFname())) { tipoConsulta[4] = 1; }
        	if (!mylib.isNullOrEmpty(dr.getQrytext())) { tipoConsulta[5] = 1; }
        	if (!mylib.isNullOrEmpty(dr.getAni())) { tipoConsulta[7] = 1; }
        	if (!mylib.isNullOrEmpty(dr.getDnis())) { tipoConsulta[8] = 1; }
        	if (!mylib.isNullOrEmpty(dr.getDuracion())) { tipoConsulta[9] = 1; }
        	
        	if (tipoConsulta[0]==0 || tipoConsulta[2]==0 || tipoConsulta[3]==0) {
        		tipoConsulta[6] = 98;
        	} else {
        		tipoConsulta[6] = 0;
        	}
        	return tipoConsulta;
        } catch (Exception e) {
            mylib.console(1,"Error en getTipoConsulta ("+e.getMessage()+")");
            return tipoConsulta;
        }
    }

}
