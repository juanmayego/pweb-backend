/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package py.pol.una.ii.pw.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import py.pol.una.ii.pw.data.VentasRepository;
import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;
import py.pol.una.ii.pw.service.VentasRegistration;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */
@Path("/ventas")
@RequestScoped
public class VentasCabeceraResourceRESTService {
	@Inject
	private Logger log;

	@Inject
	private VentasRepository repository;

	@Inject
	VentasRegistration registration;
	
	@GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public VentasCabecera lookupCompraById(@PathParam("id") long id) {
    	VentasCabecera ventas = repository.findById(id);
        if (ventas == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return ventas;
    }
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response allCompras(){
		StreamingOutput so = new StreamingOutput() {
			@Override
			public void write(OutputStream arg0) throws IOException,
					WebApplicationException {
				repository.streamVentas(arg0);
			}
		};
    	
    	return Response.ok(so).build();
    }
	/**
	 * Creates a new member from the values provided. Performs validation, and
	 * will return a JAX-RS response with either 200 ok, or with a map of
	 * fields, and related errors.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCliente(VentasCabecera venta) {

		Response.ResponseBuilder builder = null;
		try {

			for (VentasDetalles tmp : venta.getVentasDetalles()) {
				tmp.setVentasCabecera(venta);
			}
			registration.register(venta);

			// Create an "ok" response
			builder = Response.ok();
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

	/*
	 * private void validateMember(Clientes member) throws
	 * ConstraintViolationException, ValidationException { // Create a bean
	 * validator and check for issues. Set<ConstraintViolation<Member>>
	 * violations = validator.validate(member);
	 * 
	 * if (!violations.isEmpty()) { throw new ConstraintViolationException(new
	 * HashSet<ConstraintViolation<?>>(violations)); }
	 * 
	 * // Check the uniqueness of the email address if
	 * (emailAlreadyExists(member.getEmail())) { throw new
	 * ValidationException("Unique Email Violation"); } }
	 */

	/**
	 * Creates a JAX-RS "Bad Request" response including a map of all violation
	 * fields, and their message. This can then be used by clients to show
	 * violations.
	 * 
	 * @param violations
	 *            A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
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

	/**
	 * Checks if a member with the same email address is already registered.
	 * This is the only way to easily capture the
	 * "@UniqueConstraint(columnNames = "email")" constraint from the Member
	 * class.
	 * 
	 * @param email
	 *            The email to check
	 * @return True if the email already exists, and false otherwise
	 */
	/*
	 * public boolean emailAlreadyExists(String email) { Member member = null;
	 * try { member = repository.findByEmail(email); } catch (NoResultException
	 * e) { // ignore } return member != null; }
	 */

}
