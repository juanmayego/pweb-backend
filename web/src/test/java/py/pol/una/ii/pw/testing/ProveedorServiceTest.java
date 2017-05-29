package py.pol.una.ii.pw.testing;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import py.pol.una.ii.pw.data.ProveedorRepository;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.rest.ProveedorResourceRESTService;
import py.pol.una.ii.pw.service.ProveedorRegistration;

@RunWith(MockitoJUnitRunner.class)
public class ProveedorServiceTest {
	public static final String PATH = "/proveedores";
	public static InMemoryRestServer server;
	private Proveedor model;
	
	private static final Long idProveedor = 1L ;
	private static final String nombre = "Proveedor 1";
	private static final String ruc = "45687912-1";
	private static final String direccion = "lejos";
	private static final Date fechaCreacion = new Date();
	private static final Date fechaActualizacion = new Date();
	
	@InjectMocks
	private ProveedorResourceRESTService service = new ProveedorResourceRESTService();
	
	@Mock
	private ProveedorRegistration registration;
	
	@Mock
	private ProveedorRepository repository;
	
	@Before
	public void prepare() throws Exception{
		server = InMemoryRestServer.create(service);
		
		model = new Proveedor();
		
		model.setIdProveedor(idProveedor);
		model.setNombre(nombre);
		model.setRuc(ruc);
		model.setDireccion(direccion);
		model.setFechaCreacion(fechaCreacion);
		model.setFechaActualizacion(fechaActualizacion);
		
		List<Proveedor> allProveedores = new ArrayList<Proveedor>();
		allProveedores.add(model);
        when(repository.findAllOrderedByName(anyString())).thenReturn(allProveedores);
        when(repository.findById(1L)).thenReturn(model);
        when(repository.findById(15L)).thenReturn(null);
		 
	}

	@After
    public void afterClass() throws Exception {
        server.close();
    }
	
	@Test
    public void testGETAllProveedores() throws Exception {
        List<Proveedor> allProveedores = service.listAllProveedores(null);
        Assert.assertNotNull(allProveedores);
        Assert.assertTrue(allProveedores.size() > 0);
        Assert.assertEquals(nombre ,allProveedores.get(0).getNombre());
    }
	
	@Test
    public void testGETProveedorById() throws Exception {
		Response response = server.newRequest(PATH+"/1").request().get();
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
	
	@Test
    public void testGETProveedorByIdNoExist() throws Exception {
		Response response = server.newRequest(PATH+"/15").request().get();
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
	
	@Test
	public void TestAddProveedor(){
		Response response = server.newRequest(PATH).request().buildPost(Entity.json(model)).invoke();
		Assert.assertEquals("OK: 200",
				Response.Status.OK.getStatusCode(),
				response.getStatus());
	}
	
	@Test
	public void TestPUTProveedorOK() throws Exception {
		Response response = server.newRequest(PATH+"/")
				.request().buildPut(Entity.json(model)).invoke();
		Assert.assertEquals("OK: 200",
				Response.Status.OK.getStatusCode(),
				response.getStatus());
	}

	@Test
	public void TestDELETEProveedorOK() throws Exception {
		Response response = server.newRequest(PATH+"/"+String.valueOf(model.getIdProveedor()))
				.request().buildDelete().invoke();
		Assert.assertEquals("Ok: 200",
				Response.Status.OK.getStatusCode(),
				response.getStatus());
	}
		
}
