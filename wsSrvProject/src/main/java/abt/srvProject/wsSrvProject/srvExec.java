package abt.srvProject.wsSrvProject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("process")
public class srvExec {
	Procedures myproc = new Procedures();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSummary() {
        return myproc.getSummary();
    }

    @Path("{status}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String status() {
    	return "OK";
    }
}
