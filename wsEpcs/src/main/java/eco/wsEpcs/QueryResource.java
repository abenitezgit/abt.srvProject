package eco.wsEpcs;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import common.rutinas.Rutinas;
import eco.model.QueryResponse;
import eco.services.QueryService;

@Path("query")
public class QueryResource {
	Rutinas mylib = new Rutinas();
	QueryService srvQuery = new QueryService();
	QueryResponse qResp = new QueryResponse();
	final String proyecto = "wsEpcs/query";
	
	@GET
	public Response noData() {
		return Response.status(427).entity("Accion no permitida por GET").build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response fnSelect(String dataInput) {
		try {
			ResponseBuilder response = null;
			String strResp = "";
			
			try {
				mylib.console(proyecto,"Iniciando QueryResource");
				mylib.console(proyecto,"Recibiendo dataInput: "+dataInput);
				
				mylib.console(proyecto,"Inicializando Componentes");
				srvQuery.initComponents(dataInput);
				
				if (srvQuery.isInitComponents()) {
					mylib.console(proyecto,"Ejecutando Requerimiento");
					srvQuery.executeRequest();
					if (srvQuery.isExecuteRequest()) {
						mylib.console(proyecto,"Request ejecutado Exitosamente");
						switch(srvQuery.getMetodo()) {
						case "InsertEnt":
							qResp.setStatus(200);
							qResp.setMessage("SUCESS");
							qResp.setQuery(srvQuery.getQuery());
							qResp.setLimit(srvQuery.getRowsLimit());
							qResp.setRows(srvQuery.getRowsAfected());
							strResp = mylib.serializeObjectToJSon(qResp, false);
							break;
						case "InsertSeg":
							qResp.setStatus(200);
							qResp.setMessage("SUCESS");
							qResp.setQuery(srvQuery.getQuery());
							qResp.setLimit(srvQuery.getRowsLimit());
							qResp.setRows(srvQuery.getRowsAfected());
							strResp = mylib.serializeObjectToJSon(qResp, false);
							break;
						case "SelectEnt":
							qResp.setStatus(200);
							strResp = srvQuery.getMapRow();
							break;
						case "SelectSeg":
							qResp.setStatus(200);
							strResp = srvQuery.getMapRow();
							break;
						case "SelectCmbEnt":
							qResp.setStatus(200);
							strResp = srvQuery.getMapRow();
							break;
						case "DeleteEnt":
							qResp.setStatus(200);
							qResp.setMessage("SUCESS");
							qResp.setQuery(srvQuery.getQuery());
							qResp.setLimit(srvQuery.getRowsLimit());
							qResp.setRows(srvQuery.getRowsAfected());
							strResp = mylib.serializeObjectToJSon(qResp, false);
							break;
						case "DeleteSeg":
							qResp.setStatus(200);
							qResp.setMessage("SUCESS");
							qResp.setQuery(srvQuery.getQuery());
							qResp.setLimit(srvQuery.getRowsLimit());
							qResp.setRows(srvQuery.getRowsAfected());
							strResp = mylib.serializeObjectToJSon(qResp, false);
							break;
						case "UpdateEnt":
							qResp.setStatus(200);
							qResp.setMessage("SUCESS");
							qResp.setQuery(srvQuery.getQuery());
							qResp.setLimit(srvQuery.getRowsLimit());
							qResp.setRows(srvQuery.getRowsAfected());
							strResp = mylib.serializeObjectToJSon(qResp, false);
							break;
						case "UpdateSeg":
							qResp.setStatus(200);
							qResp.setMessage("SUCESS");
							qResp.setQuery(srvQuery.getQuery());
							qResp.setLimit(srvQuery.getRowsLimit());
							qResp.setRows(srvQuery.getRowsAfected());
							strResp = mylib.serializeObjectToJSon(qResp, false);
							break;
						}
					} else {
						mylib.console(1,proyecto,"Error ejecutando query");
						qResp.setStatus(428);
						qResp.setMessage("Error ejecutando query");
						strResp = mylib.serializeObjectToJSon(qResp, false);
					}
				} else {
					mylib.console(1,proyecto,"Error inicializando componentes");
					qResp.setStatus(429);
					qResp.setMessage("Error inicializando componentes");
					strResp = mylib.serializeObjectToJSon(qResp, false);
				}

			} catch (Exception e) {
				mylib.console(1,proyecto,"Error general de proceso "+e.getMessage());
				qResp.setStatus(430);
				qResp.setMessage("Error general de proceso "+e.getMessage());
				strResp = mylib.serializeObjectToJSon(qResp, false);
			}
			
			mylib.console(proyecto,"Terminando proceso");
			response = Response.status(qResp.getStatus()).entity(strResp);
			
			return response.build();
			//return strResp;
		} catch (Exception e) {
			mylib.console(1,proyecto,"Error general de proceso: "+e.getMessage());
			return Response.status(427).entity("Error general de proceso "+e.getMessage()).build();
			//return "Error";
		}
	}
}
