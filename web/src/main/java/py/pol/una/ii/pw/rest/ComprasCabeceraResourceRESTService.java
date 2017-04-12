package py.pol.una.ii.pw.rest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import py.pol.una.ii.pw.data.ComprasRepository;
import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.model.dto.ComprasCabeceraDTO;
import py.pol.una.ii.pw.service.ComprasRegistration;


@Path("/compras")
@RequestScoped
public class ComprasCabeceraResourceRESTService {
    @Inject
    ComprasRegistration registration;
    @Inject
    ComprasRepository repository;
    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    
    @SuppressWarnings("resource")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ComprasCabeceraDTO> listAllCompras(){
    	try {
    		List<ComprasCabeceraDTO> resultados = new ArrayList<ComprasCabeceraDTO>();
    		Gson gson = new GsonBuilder().create();
			String ruta =  repository.findAllCabeceras();
			FileReader fr = new FileReader(ruta);
		    BufferedReader br = new BufferedReader(fr);
		    String linea;
		    while((linea = br.readLine()) != null){
		    	ComprasCabeceraDTO compra = gson.fromJson(linea, ComprasCabeceraDTO.class);
		    	resultados.add(compra);
		    }
		    return resultados;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
    
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
