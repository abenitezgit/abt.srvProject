package eco.wsEpcs;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import common.rutinas.Rutinas;
import eco.model.DataResponse;
import eco.services.AccionService;

@Path("getAccion")
public class AccionResource {
	Rutinas mylib = new Rutinas();
	AccionService srvAcc = new AccionService();
	DataResponse dResp = new DataResponse();
	final String proyecto = "wsEpcs";
	
	@GET
	public Response getNulo() {
		return Response.status(427).entity("Operacion no permitida por GET").build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccion(String dataInput) {
		ResponseBuilder response;
		int errCode=200;
		String errMesg="";
		try {
			boolean exitStatus = false;
			
			mylib.console(proyecto,"Iniciando AccionResource");
			mylib.console(proyecto,"Recibiendo dataInput: "+dataInput);
			
			mylib.console(proyecto,"Inicializando Componentes");
			srvAcc.initComponents(dataInput);
			
			if (srvAcc.isInitComponents()) {
				mylib.console(proyecto,"Recuperando valores");
				srvAcc.getAccion();
				
				if (srvAcc.isRowExist()) {
					mylib.console(proyecto,"Valores recuperados");
					dResp = srvAcc.getDataResponse();
					exitStatus = true;
					errCode = 200;
					errMesg = "SUCESS";
				} else {
					errCode = 428;
					errMesg = "No se encontró registro para la consulta realizada";
					mylib.console(1,proyecto,errMesg);
				}
			} else {
				errCode = 427;
				errMesg = "No pudo inicializar componentes";
				mylib.console(1,proyecto,errMesg);
			}
			
			if (exitStatus) {
				dResp.setStatus(200);
				dResp.setMessage("SUCESS");
				errCode = 200;
				errMesg =  mylib.serializeObjectToJSon(dResp, false);
			}
			
			response = Response.status(errCode).entity(errMesg);
			
			mylib.console(proyecto,"Terminando AccionResource");
			return response.build();
		} catch (Exception e) {
			mylib.console(1,proyecto,"Error modulo principal: "+e.getMessage());
			return Response.status(500).entity("Error query ("+e.getMessage()+")").build();
		}
		
	}
	
	
	
}
