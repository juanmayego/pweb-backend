package py.pol.una.ii.pw.rest;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.service.ComprasRegistration;


@Path("/compras")
@RequestScoped
public class ComprasCabeceraResourceRESTService {
    @Inject
    ComprasRegistration registration;
    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompra(ComprasCabecera compra) {
    	
        Response.ResponseBuilder builder = null;
        try 
        {
            for(ComprasDetalles tmp: compra.getComprasDetalles()){
        		tmp.setComprasCabecera(compra);
        		
        	}
            String save;
            save=registration.register(compra);
            if(save.equals("persisted")){
            	builder = Response.ok();
            }else{
            	// Handle the unique constrain violation
                Map<String, String> responseObj = new HashMap<String, String>();
                responseObj.put("Productos", "Los productos no corresponden a un solo proveedor");
                builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
            }
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
}
