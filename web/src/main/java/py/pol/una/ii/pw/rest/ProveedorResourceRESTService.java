package py.pol.una.ii.pw.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.pol.una.ii.pw.data.ProveedorRepository;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.service.ProveedorRegistration;

@Path("/proveedores")
@RequestScoped
public class ProveedorResourceRESTService {
    @Inject
    private Logger log;

    @Inject
    private ProveedorRepository repository;

    @Inject
    ProveedorRegistration registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Proveedor> listAllProveedores(
    		@QueryParam("filter") String query) {
        return repository.findAllOrderedByName(query);
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Proveedor lookupMemberById(@PathParam("id") long id) {
    	Proveedor proveedor = repository.findById(id);
        if (proveedor == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return proveedor;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCliente(Proveedor proveedor) {
    	
        Response.ResponseBuilder builder = null;
        try {

            registration.register(proveedor);
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        return builder.build();
    }

    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
    
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductos(Proveedor proveedor){
    	Response.ResponseBuilder builder = null;
    	try {
			registration.update(proveedor);
			builder = Response.status(Response.Status.OK);
		} catch (Exception e) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
    	return builder.build();
    }
    
    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Proveedor removeProducto(@PathParam("id") long id) {
    	Proveedor proveedor = repository.findById(id);
    	try {
        	registration.remove(proveedor);
		} catch (Exception e) {
			// ignore
		}
    	return proveedor;
    }
    
    
    

}
