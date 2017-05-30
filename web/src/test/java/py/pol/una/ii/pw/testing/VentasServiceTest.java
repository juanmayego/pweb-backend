package py.pol.una.ii.pw.testing;

import static org.mockito.Mockito.when;

import java.io.IOException;
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

import py.pol.una.ii.pw.data.VentasRepository;
import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;
import py.pol.una.ii.pw.rest.VentasCabeceraResourceRESTService;
import py.pol.una.ii.pw.service.VentasRegistration;

@RunWith(MockitoJUnitRunner.class)
public class VentasServiceTest {
	
	public static final String PATH = "/ventas";
	public static InMemoryRestServer server;
	
	private Clientes clientes;
	private VentasCabecera instance;
	private Productos productos;
	private Proveedor proveedor;
	private VentasDetalles detalle;
	private List<VentasDetalles> ventasDetalles = new ArrayList<VentasDetalles>(
			0);
	
	private static final Long idVentasCabecera = 1L;
	private static final Double montoTotal = 1500D;
	private static final Double saldo = 1500D ;
	private static final String Estado = "PENDIENTE";
	private static final Date fechaDocumento = new Date();
	private static final Date fechaCreacion = new Date();
	private static final Date fechaActualizacion = new Date();
	
	private static final Long idProveedor = 1L ;
	private static final String nombreP = "Proveedor 1";
	private static final String ruc = "45687912-1";
	private static final String direccionP = "lejos";
	
	private static final Long idProducto = 1L;
	private static final String descripcion = "Producto 1";
	private static final Double precio = 5000D;
	private static final Double cantidadDisponible = 100D;
	
	private static final Long idCliente = 1L;
	private static final String nombre = "Juan";
	private static final String ciNro = "12345679-1";
	private static final String  email = "jbritez@email.com";
	private static final String direccion = "Palo Santo";
	private static final Double saldoVentas = 0D;
	private static final String numero = "0961963741";
	
	
	@InjectMocks
	private VentasCabeceraResourceRESTService service = new VentasCabeceraResourceRESTService();
	
	@Mock
	private VentasRepository repository;
	
	@Mock
	private VentasRegistration registration;
	
	
	@Before
	public void prepare() throws IOException{
		server = InMemoryRestServer.create(service);
		
		clientes = new Clientes();
		clientes.setIdCliente(idCliente);
		clientes.setNombre(nombre);
		clientes.setDireccion(direccion);
		clientes.setEmail(email);
		clientes.setNumero(numero);
		clientes.setSaldoVentas(saldoVentas);
		clientes.setCiNro(ciNro);
		
		proveedor = new Proveedor();
        proveedor.setIdProveedor(idProveedor);;
        proveedor.setNombre(nombreP);
        proveedor.setRuc(ruc);
        proveedor.setDireccion(direccionP);
        proveedor.setFechaActualizacion(fechaActualizacion);
        proveedor.setFechaCreacion(fechaCreacion);
        
		productos = new Productos();
		productos.setIdProducto(idProducto);
		productos.setDescripcion(descripcion);
		productos.setCantidadDisponible(cantidadDisponible);
		productos.setFechaActualizacion(fechaActualizacion);
		productos.setFechaCreacion(fechaCreacion);
		productos.setProveedor(proveedor);
		productos.setPrecio(precio);
		
		instance = new VentasCabecera();
		instance.setIdVentasCabecera(idVentasCabecera);
		instance.setEstado(Estado);
		instance.setFechaActualizacion(fechaActualizacion);
		instance.setFechaCreacion(fechaCreacion);
		instance.setFechaDocumento(fechaDocumento);
		instance.setClientes(clientes);
		instance.setMontoTotal(montoTotal);
		instance.setSaldo(saldo);
		
		detalle = new VentasDetalles();
		detalle.setCantidad(5L);
		detalle.setIdVentasDetalle(1L);
		detalle.setProducto(productos);
		
		ventasDetalles.add(detalle);
		
		instance.setVentasDetalles(ventasDetalles);
		
		when(repository.findById(idVentasCabecera)).thenReturn(instance);
		
	}
	
	@After
	public void close(){
		server.close();
	}
	
	@Test
    public void testGETAllVentas() throws Exception {
		Response response = server.newRequest(PATH).request().get();
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
	
	@Test
    public void testGETVentasById() throws Exception {
		Response response = server.newRequest(PATH+"/1").request().get();
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
	
	@Test
    public void testGETVentasByIdNoExist() throws Exception {
		Response response = server.newRequest(PATH+"/15").request().get();
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
	
	@Test
	public void testPostVentas(){
		Response response = server.newRequest(PATH)
                .request().buildPost(Entity.json(instance)).invoke();
		Assert.assertEquals("OK: 200",
                Response.Status.OK.getStatusCode(),
                response.getStatus());
	}
	 

}
