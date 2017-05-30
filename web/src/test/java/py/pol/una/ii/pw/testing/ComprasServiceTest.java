package py.pol.una.ii.pw.testing;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

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

import py.pol.una.ii.pw.data.ComprasRepository;
import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.rest.ComprasCabeceraResourceRESTService;
import py.pol.una.ii.pw.service.ComprasRegistration;

@RunWith(MockitoJUnitRunner.class)
public class ComprasServiceTest {
	
	public static final String PATH = "/compras";
	public static InMemoryRestServer server;
	
	
	
	private static final Long idComprasCabecera =1L;
	private static final  Double montoTotal = 15000D;
	private static final  Date fechaDocumento = new Date();;
	private static final  Date fechaCreacion = new Date();;
	private static final  Date fechaActualizacion = new Date();
	
	private static final Long idProveedor = 1L ;
	private static final String nombre = "Proveedor 1";
	private static final String ruc = "45687912-1";
	private static final String direccion = "lejos";
	
	private static final Long idProducto = 1L;
	private static final String descripcion = "Producto 1";
	private static final Double precio = 5000D;
	private static final Double cantidadDisponible = 100D;
	
	
	private ComprasCabecera instance;
	private ComprasDetalles detalles;
	private Proveedor proveedor;
	private Productos producto;
	private List<ComprasDetalles> listaDetalle = new ArrayList<ComprasDetalles>();;
	
	@InjectMocks
	private ComprasCabeceraResourceRESTService service = new ComprasCabeceraResourceRESTService();
	
	@Mock
	private ComprasRepository repository;
	
	@Mock
	private ComprasRegistration registration;
	
	@Before
	public void prepare() throws Exception {
		server = InMemoryRestServer.create(service);
		
		proveedor = new Proveedor();
		producto = new Productos();
		detalles = new ComprasDetalles();
		instance = new ComprasCabecera();
		
		proveedor.setIdProveedor(idProveedor);
		proveedor.setNombre(nombre);
		proveedor.setRuc(ruc);
		proveedor.setFechaCreacion(fechaCreacion);
		proveedor.setFechaActualizacion(fechaActualizacion);
		proveedor.setDireccion(direccion);
		
		producto.setIdProducto(idProducto);
		producto.setDescripcion(descripcion);
		producto.setCantidadDisponible(cantidadDisponible);
		producto.setFechaActualizacion(fechaActualizacion);
		producto.setFechaCreacion(fechaCreacion);
		producto.setProveedor(proveedor);
		producto.setPrecio(precio);
		
		detalles.setIdComprasDetalle(1L);
		detalles.setCantidad(5L);
		detalles.setProducto(producto);
		
		instance.setIdComprasCabecera(idComprasCabecera);
		instance.setProveedor(proveedor);
		instance.setMontoTotal(montoTotal);
		instance.setFechaDocumento(fechaDocumento);
		instance.setFechaActualizacion(fechaActualizacion);
		instance.setFechaCreacion(fechaCreacion);
		
		listaDetalle.add(detalles);
		instance.setComprasDetalles(listaDetalle);
		
		// when(service.allCompras()).thenReturn(listaDetalle);
		when(repository.findById(idComprasCabecera)).thenReturn(instance);
			
	}
	
	@After
	public void close(){
		server.close();
	}
	
	@Test
    public void testGETAllComprasById() throws Exception {
		Response response = server.newRequest(PATH).request().get();
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
	
	@Test
    public void testGETComprasById() throws Exception {
		Response response = server.newRequest(PATH+"/1").request().get();
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
	
	@Test
    public void testGETComprasByIdNoExist() throws Exception {
		Response response = server.newRequest(PATH+"/15").request().get();
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
	
	/* @Test
	public void testPOSTCompras() throws Exception {
		Response response = server.newRequest(PATH)
                .request().buildPost(Entity.json(instance)).invoke();
		Assert.assertEquals("OK: 200",
                Response.Status.OK.getStatusCode(),
                response.getStatus());
	} */
}