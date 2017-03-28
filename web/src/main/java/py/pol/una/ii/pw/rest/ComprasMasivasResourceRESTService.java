package py.pol.una.ii.pw.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
//import java.util.logging.Logger;



import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.apache.commons.io.IOUtils;

import py.pol.una.ii.pw.service.ComprasMasivasRegistration;

@Path("/compras/masivas")
@RequestScoped
public class ComprasMasivasResourceRESTService {
	
	private final String UPLOADED_FILE_PATH = "c:\\filesrest\\compras\\";
	
	@Inject
	ComprasMasivasRegistration registration;
	
	@SuppressWarnings("resource")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(MultipartFormDataInput input) {
		String fileName = "";
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		for (InputPart inputPart : inputParts) {
		 try {
			MultivaluedMap<String, String> header = inputPart.getHeaders();
			fileName = getFileName(header);
			InputStream inputStream = inputPart.getBody(InputStream.class,null);
			byte [] bytes = IOUtils.toByteArray(inputStream);
			fileName = UPLOADED_FILE_PATH + fileName;
			writeFile(bytes,fileName);
		  } catch (IOException e) {
			e.printStackTrace();
		  }
		}
		

		try {
			  String error = "NEXT";
			  registration.initTransaction();
		      FileReader fr = new FileReader(fileName);
		      BufferedReader br = new BufferedReader(fr);
		      String linea;
		      while((linea = br.readLine()) != null && error.equals("NEXT")){
		    	error = registration.insertVentas(linea);
		      }
		      if(error == "NEXT"){
		    	  registration.finishTransaction();
		      }else if(error.equals("ERROR_NE")){
		    	  return Response.status(409)
		    			    .entity("Error: No se encuentra entidad en " + fileName).build();
		      }
		      fr.close();
		    }
		    catch(Exception e) {
		      System.out.println("Excepcion leyendo fichero "+ fileName + ": " + e);
		    }
		
		return Response.status(200)
		    .entity("Compras cargadas con exito!").build();
	}
	
	
	private String getFileName(MultivaluedMap<String, String> header) {
		 
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
 
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
 
                String[] name = filename.split("=");
 
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("not exist> " + file.getAbsolutePath());
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
    }
	
	
}
