package py.pol.una.ii.pw.testing;

import java.io.IOException;
import java.util.Date;

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

import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Pagos;
import py.pol.una.ii.pw.rest.PagosResourceRESTService;
import py.pol.una.ii.pw.service.PagosRegistration;

@RunWith(MockitoJUnitRunner.class)
public class PagosServiceTest {

	public static final String PATH = "/pagos";
	public static InMemoryRestServer server;
	
	private Pagos instance;
	private Clientes clientes;
	
	private static final Long idCliente = 1L;
	private static final String nombre = "Juan";
	private static final String ciNro = "12345679-1";
	private static final String  email = "jbritez@email.com";
	private static final String direccion = "Palo Santo";
	private static final Double saldoVentas = 15000D;
	
	private static final String numero = "0961963741";
	private static final Long idPago = 1L; 
	private static final Date fechaPago = new Date();
	private static final Double monto = 15000D;
	private static final Date fechaCreacion = new Date();
	private static final Date fechaActualizacion = new Date();
	
	@InjectMocks
	private PagosResourceRESTService service = new PagosResourceRESTService();
	
	@Mock
	private PagosRegistration registration;
	
	@Before
	public void prepare() throws IOException{
		server = InMemoryRestServer.create(service);
		
		clientes = new Clientes();
		clientes.setIdCliente(idCliente);
		clientes.setNombre(nombre);
		clientes.setCiNro(ciNro);
		clientes.setDireccion(direccion);
		clientes.setEmail(email);
		clientes.setSaldoVentas(saldoVentas);
		clientes.setNumero(numero);
		
		instance = new Pagos();
		instance.setIdPago(idPago);
		instance.setFechaPago(fechaPago);
		instance.setFechaActualizacion(fechaActualizacion);
		instance.setFechaCreacion(fechaCreacion);
		instance.setMonto(monto);
		
	}
	
	@After
	public void close(){
		server.close();
	}
	
	@Test
	public void testPOSTPago(){
		Response response = server.newRequest(PATH).request().buildPost(Entity.json(instance)).invoke();
		Assert.assertEquals("OK: 200",
				Response.Status.OK.getStatusCode(),
				response.getStatus());
	}
}
