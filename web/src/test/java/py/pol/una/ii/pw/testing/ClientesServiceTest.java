package py.pol.una.ii.pw.testing;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import py.pol.una.ii.pw.data.ClientesRepository;
import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.rest.ClientesResourceRESTService;
import py.pol.una.ii.pw.service.ClientesRegistration;

@RunWith(MockitoJUnitRunner.class)
public class ClientesServiceTest {
	
	public static final String PATH = "/clientes";
	public static InMemoryRestServer server;
	
	private static final Long idCliente = 1L;
	private static final String nombre = "Juan";
	private static final String ciNro = "12345679-1";
	private static final String  email = "jbritez@email.com";
	private static final String direccion = "Palo Santo";
	private static final Double saldoVentas = 0D;
	private static final String numero = "0961963741";
	private static final Date fechaCreacion = new Date();
	private static final Date fechaActualizacion = new Date();
	
	
	private Clientes model;
	
	@InjectMocks
	private ClientesResourceRESTService service = new ClientesResourceRESTService();
	
	@Mock
	private ClientesRegistration registration;
	
	@Mock
	private ClientesRepository repository;
	
	@Before
    public void prepare() throws Exception{
		server = InMemoryRestServer.create(service);
		
		model = new Clientes();
        
		model.setIdCliente(idCliente);
		model.setNombre(nombre);
		model.setDireccion(direccion);
		model.setCiNro(ciNro);
		model.setEmail(email);
		model.setFechaActualizacion(fechaActualizacion);
		model.setFechaCreacion(fechaCreacion);
		model.setSaldoVentas(saldoVentas);
		model.setNumero(numero);
		
        List<Clientes> allClientes = new ArrayList<Clientes>();
        allClientes.add(model);
        when(repository.findAllOrderedByName(anyString())).thenReturn(allClientes);
        when(repository.findById(1L)).thenReturn(model);
        when(repository.findById(15L)).thenReturn(null);
        
        
	}
	
	@AfterClass
    public static void afterClass() throws Exception {
        server.close();
    }
	
	@Test
    public void testGetClientesAll() throws Exception {
        List<Clientes> todosLosUsuarios = service.listAllClientes(null);
        Assert.assertNotNull(todosLosUsuarios);
        Assert.assertTrue(todosLosUsuarios.size() > 0);
        Assert.assertEquals("Juan",todosLosUsuarios.get(0).getNombre());
    }
	
	@Test
    public void testGetClientesById() throws Exception {
		Response response = server.newRequest(PATH+"/1").request().get();
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
	
	@Test
    public void testGetClientesByIdNoExist() throws Exception {
		Response response = server.newRequest(PATH+"/15").request().get();
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
	
	@Test
	public void addClientes(){
		Response response = server.newRequest(PATH).request().buildPost(Entity.json(model)).invoke();
		Assert.assertEquals("OK: 200",
				Response.Status.OK.getStatusCode(),
				response.getStatus());
	}
	
	
}
