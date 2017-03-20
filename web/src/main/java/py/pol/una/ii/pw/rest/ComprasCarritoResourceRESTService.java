package py.pol.una.ii.pw.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.service.ComprasCarritoRegistration;

@Path("/cartcompra")
@RequestScoped
public class ComprasCarritoResourceRESTService {
	@Inject
	private Logger log;

	/*@Inject
	private ClientesRepository repository;*/

	/*@Inject
	VentasCarritoRegistration registration;*/
	
	private static final String CART_SESSION_KEY 
	    = "cartcompra";

	@POST
	@Path("/confirmarcompra")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response commitCompra(@Context HttpServletRequest request) {
		Response.ResponseBuilder builder = null;
		
		ComprasCarritoRegistration registration = (ComprasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (ComprasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/ComprasCarritoRegistration!py.pol.una.ii.pw.service.ComprasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			String respuesta = registration.register();
			if(respuesta.equals("persisted")){
				registration.finish();
				builder = Response.ok();
			}
			if(respuesta.equals("noproveedor")){
				Map<String, String> responseObj = new HashMap<String, String>();
				responseObj.put("Proveedor", "Proveedor no asignado");
				builder = Response.status(Response.Status.CONFLICT).entity(
						responseObj);
			}
			if(respuesta.equals("nodetails")){
				Map<String, String> responseObj = new HashMap<String, String>();
				responseObj.put("Detalles", "No se ha agregado ningun producto");
				builder = Response.status(Response.Status.CONFLICT).entity(
						responseObj);
			}
			
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email", "Email taken");
			builder = Response.status(Response.Status.CONFLICT).entity(
					responseObj);
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(
					responseObj);
		}
		return builder.build();
	}

	@POST
	@Path("/proveedor/asignar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProveedor(@Context HttpServletRequest request, Proveedor proveedor){
		Response.ResponseBuilder builder = null;
		ComprasCarritoRegistration registration = (ComprasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (ComprasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/ComprasCarritoRegistration!py.pol.una.ii.pw.service.ComprasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		registration.setProveedor(proveedor);
		builder = Response.ok();
		return builder.build();
	}
	
	@POST
	@Path("/detallecompra/agregar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProducto(@Context HttpServletRequest request,ComprasDetalles detalle){
		Response.ResponseBuilder builder = null;
		
		ComprasCarritoRegistration registration = (ComprasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (ComprasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/ComprasCarritoRegistration!py.pol.una.ii.pw.service.ComprasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		registration.agregarDetalleACarrito(detalle);
		System.out.println("Detalles: ");
		for(ComprasDetalles det : registration.getDetalles()){
			System.out.println("Cantidad "+det.getProducto().getDescripcion());
		}
		System.out.println("Proveedor: ");
		if(registration.getProveedor()!=null){
			System.out.println(registration.getProveedor().getNombre());
		}
		builder = Response.ok();
		return builder.build();
	}
	
	
	@POST
	@Path("/detallecompra/modificar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDetalle(@Context HttpServletRequest request,ComprasDetalles detalle){
		Response.ResponseBuilder builder = null;
		
		ComprasCarritoRegistration registration = (ComprasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (ComprasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/ComprasCarritoRegistration!py.pol.una.ii.pw.service.ComprasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		registration.modificarDetalleCarrito(detalle);
		System.out.println("Detalles: ");
		for(ComprasDetalles det : registration.getDetalles()){
			System.out.println("Cantidad "+det.getProducto().getDescripcion());
		}
		System.out.println("Proveedor: ");
		if(registration.getProveedor()!=null){
			System.out.println(registration.getProveedor().getNombre());
		}
		builder = Response.ok();
		return builder.build();
	}
	@POST
	@Path("/detallecompra/borrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDetalle(@Context HttpServletRequest request,ComprasDetalles detalle){
		Response.ResponseBuilder builder = null;
		
		ComprasCarritoRegistration registration = (ComprasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (ComprasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/ComprasCarritoRegistration!py.pol.una.ii.pw.service.ComprasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		registration.eliminarDetalleDeCarrito(detalle);
		System.out.println("Detalles: ");
		for(ComprasDetalles det : registration.getDetalles()){
			System.out.println("Cantidad "+det.getProducto().getDescripcion());
		}
		System.out.println("Proveedor: ");
		if(registration.getProveedor()!=null){
			System.out.println(registration.getProveedor().getNombre());
		}
		builder = Response.ok();
		return builder.build();
	}
	
	private Response.ResponseBuilder createViolationResponse(
			Set<ConstraintViolation<?>> violations) {
		log.fine("Validation completed. violations found: " + violations.size());

		Map<String, String> responseObj = new HashMap<String, String>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(),
					violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}
}
