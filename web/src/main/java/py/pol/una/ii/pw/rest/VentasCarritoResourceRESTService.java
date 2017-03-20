package py.pol.una.ii.pw.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
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
import javax.servlet.http.HttpServletRequest;

import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.VentasDetalles;
import py.pol.una.ii.pw.service.VentasCarritoRegistration;

@Path("/cart")
@RequestScoped
public class VentasCarritoResourceRESTService{
	@Inject
	private Logger log;

	/*@Inject
	private ClientesRepository repository;*/

	/*@Inject
	VentasCarritoRegistration registration;*/
	
	private static final String CART_SESSION_KEY 
	    = "cart";

	@POST
	@Path("/confirmar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response commitVenta(@Context HttpServletRequest request) {
		Response.ResponseBuilder builder = null;
		
		VentasCarritoRegistration registration = (VentasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (VentasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/VentasCarritoRegistration!py.pol.una.ii.pw.service.VentasCarritoRegistration");

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
			if(respuesta.equals("noclient")){
				Map<String, String> responseObj = new HashMap<String, String>();
				responseObj.put("Cliente", "Cliente no asignado");
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
	@Path("/cliente/asignar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCliente(@Context HttpServletRequest request, Clientes cliente){
		Response.ResponseBuilder builder = null;
		VentasCarritoRegistration registration = (VentasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (VentasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/VentasCarritoRegistration!py.pol.una.ii.pw.service.VentasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		registration.setCliente(cliente);
		builder = Response.ok();
		return builder.build();
	}
	
	@POST
	@Path("/detalle/agregar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProducto(@Context HttpServletRequest request,VentasDetalles detalle){
		Response.ResponseBuilder builder = null;
		
		VentasCarritoRegistration registration = (VentasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (VentasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/VentasCarritoRegistration!py.pol.una.ii.pw.service.VentasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		registration.agregarDetalleACarrito(detalle);
		System.out.println("Detalles: ");
		for(VentasDetalles det : registration.getDetalles()){
			System.out.println("Cantidad "+det.getProducto().getDescripcion());
		}
		System.out.println("Cliente: ");
		if(registration.getCliente()!=null){
			System.out.println(registration.getCliente().getNombre());
		}
		builder = Response.ok();
		return builder.build();
	}
	
	
	@POST
	@Path("/detalle/modificar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDetalle(@Context HttpServletRequest request,VentasDetalles detalle){
		Response.ResponseBuilder builder = null;
		
		VentasCarritoRegistration registration = (VentasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (VentasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/VentasCarritoRegistration!py.pol.una.ii.pw.service.VentasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		registration.modificarDetalleCarrito(detalle);
		System.out.println("Detalles: ");
		for(VentasDetalles det : registration.getDetalles()){
			System.out.println("Cantidad "+det.getProducto().getDescripcion());
		}
		System.out.println("Cliente: ");
		if(registration.getCliente()!=null){
			System.out.println(registration.getCliente().getNombre());
		}
		builder = Response.ok();
		return builder.build();
	}
	@POST
	@Path("/detalle/borrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDetalle(@Context HttpServletRequest request,VentasDetalles detalle){
		Response.ResponseBuilder builder = null;
		
		VentasCarritoRegistration registration = (VentasCarritoRegistration) 
				request.getSession().getAttribute(CART_SESSION_KEY);
		if(registration == null){
			InitialContext ic;
			try {
				ic = new InitialContext();
				registration = (VentasCarritoRegistration) 
						ic.lookup("java:global/EjbJaxRS-ear/EjbJaxRS-ejb/VentasCarritoRegistration!py.pol.una.ii.pw.service.VentasCarritoRegistration");

				request.getSession().setAttribute(
						CART_SESSION_KEY, 
						registration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		registration.eliminarDetalleDeCarrito(detalle);
		System.out.println("Detalles: ");
		for(VentasDetalles det : registration.getDetalles()){
			System.out.println("Cantidad "+det.getProducto().getDescripcion());
		}
		System.out.println("Cliente: ");
		if(registration.getCliente()!=null){
			System.out.println(registration.getCliente().getNombre());
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
