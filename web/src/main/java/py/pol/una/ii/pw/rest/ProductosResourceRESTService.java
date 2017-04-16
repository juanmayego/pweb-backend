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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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

import py.pol.una.ii.pw.data.ProductosRepository;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.service.ProductosRegistration;


@Path("/productos")
@RequestScoped
public class ProductosResourceRESTService {
    @Inject
    private ProductosRepository repository;

    @Inject
    ProductosRegistration registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Productos> listAllProductos( 
    		@QueryParam("filter") String descri) {
        return repository.findAllOrderedByName(descri);
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Productos lookupMemberById(@PathParam("id") long id) {
    	Productos productos = repository.findById(id);
        if (productos == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return productos;
    }

    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProductos(Productos producto) {
    	
        Response.ResponseBuilder builder = null;
        try {
            // Validates member using bean validation
            //validateMember(member);

            registration.register(producto);
            builder = Response.ok();
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        return builder.build();
    }
    
    
    /*@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductos(Productos producto){
    	Response.ResponseBuilder builder = null;
    	try {
			registration.update(producto);
		} catch (Exception e) {
			// Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
    	return builder.build();
    }
    
    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Productos removeProducto(@PathParam("id") long id) {
    	Productos productos = repository.findById(id);
        try {
        	registration.remove(productos);
		} catch (Exception e) {
			// ignore
		}
    	return productos;
    }*/
    
}
