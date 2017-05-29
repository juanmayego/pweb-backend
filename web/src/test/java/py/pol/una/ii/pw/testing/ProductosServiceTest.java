package py.pol.una.ii.pw.testing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import py.pol.una.ii.pw.data.ProductosRepository;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.rest.ProductosResourceRESTService;
import py.pol.una.ii.pw.service.ProductosRegistration;

@RunWith(MockitoJUnitRunner.class)
public class ProductosServiceTest {
	
	public static final String PATH = "/productos";
	public static InMemoryRestServer server;
	
	private Productos model;
	
	private Proveedor proveedor;
	
	private static final Long idProducto = 1L;
	private static final String descripcion = "Producto 1";
	private static final Double precio = 5000D;
	private static final Double cantidadDisponible = 100D;
	private static final Date fechaCreacion = new Date();
	private static final Date fechaActualizacion = new Date();
	
	private static final Long idProveedor = 1L ;
	private static final String nombre = "Proveedor 1";
	private static final String ruc = "45687912-1";
	private static final String direccion = "lejos";
	
	
	
	@InjectMocks
	private ProductosResourceRESTService service = new ProductosResourceRESTService();
	
	@Mock
	private ProductosRegistration registration;
	
	@Mock
	private ProductosRepository repository;
	
	@Before
	public void prepare()throws Exception {
		server = InMemoryRestServer.create(service);
		
		proveedor = new Proveedor();
        proveedor.setIdProveedor(idProveedor);;
        proveedor.setNombre(nombre);
        proveedor.setRuc(ruc);
        proveedor.setDireccion(direccion);
        proveedor.setFechaActualizacion(fechaActualizacion);
        proveedor.setFechaCreacion(fechaCreacion);

        model = new Productos();
        
        model.setIdProducto(idProducto);
        model.setDescripcion(descripcion);
        model.setCantidadDisponible(cantidadDisponible);
        model.setPrecio(precio);
        model.setFechaActualizacion(fechaActualizacion);
        model.setFechaCreacion(fechaCreacion);
        model.setProveedor(proveedor);

        when(this.repository.findById(1L)).thenReturn(model);
        when(this.repository.findById(15L)).thenReturn(null);

        List<Productos> listaProductos = new ArrayList<Productos>();
        listaProductos.add(model);
        when(this.repository.findAllOrderedByName(anyString())).thenReturn(listaProductos);
	}
	
	@After
	public void close(){
		server.close();
	}
	
	@Test
	public void testGETAllProductos(){
		Response response = server.newRequest(PATH).request().get();
		Assert.assertEquals("OK: 200",
				Response.Status.OK.getStatusCode(),
				response.getStatus());
	}
	
	@Test
    public void testGETProductosById() throws Exception {
		Response response = server.newRequest(PATH+"/1").request().get();
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
	
	@Test
    public void testGETProductosByIdNoExist() throws Exception {
		Response response = server.newRequest(PATH+"/15").request().get();
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
	
	
	@Test
	public void TestPOSTProductos() throws Exception{
		Response response = server.newRequest(PATH)
                .request().buildPost(Entity.json(model)).invoke();
		Assert.assertEquals("OK: 200",
                Response.Status.OK.getStatusCode(),
                response.getStatus());
	}
}
