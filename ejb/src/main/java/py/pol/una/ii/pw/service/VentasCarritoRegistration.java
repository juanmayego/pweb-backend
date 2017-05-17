package py.pol.una.ii.pw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.apache.ibatis.session.SqlSession;

import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.VentasDetallesMapper;
import py.pol.una.ii.pw.mybatis.mappers.ComprasCabeceraMapper;
import py.pol.una.ii.pw.mybatis.mappers.ProductosMapper;
import py.pol.una.ii.pw.mybatis.mappers.ClientesMapper;
import py.pol.una.ii.pw.mybatis.mappers.VentasCabeceraMapper;

@Stateful
@StatefulTimeout(unit=TimeUnit.MINUTES, value=30)
@TransactionManagement(TransactionManagementType.BEAN)
public class VentasCarritoRegistration {
	
	@Inject
    private Logger log;

    
    @Resource
    private EJBContext context;

    private UserTransaction trx;

    private VentasCabecera instance;
    private List<VentasDetalles> detalles;
    private Clientes cliente;
    private SqlSession sqlSession;
	
    @PostConstruct
    public void start(){
    	instance = new VentasCabecera();
    	detalles = new ArrayList<VentasDetalles>();
    	sqlSession = new MyBatisUtil().getSession();
    }
    
    
//    @Remove
    public void finish(){
    	instance = new VentasCabecera();
    	detalles = new ArrayList<VentasDetalles>();
    }
    
    public void initVenta(VentasCabecera venta) throws Exception{
    	
    	trx=context.getUserTransaction();
    	instance = venta;
    	instance.setFechaDocumento(new Date());
    	instance.setFechaCreacion(new Date());
    	instance.setFechaActualizacion(new Date());
    	trx.begin();
    	VentasCabeceraMapper ventasCabeceraMapper = sqlSession.getMapper(VentasCabeceraMapper.class);
    	ventasCabeceraMapper.insertVentaCabecera(instance);
    }
    
    public void agregarDetalleACarrito(VentasDetalles detalle){
    	ProductosMapper productosMapper = sqlSession.getMapper(ProductosMapper.class);
    	detalle.setProducto(productosMapper.getProductoById(detalle.getProducto().getIdProducto()));
    	Boolean existe = false;
    	for(VentasDetalles det : detalles){
    		if(det.getProducto().getIdProducto().equals(detalle.getProducto().getIdProducto())){
    			existe = true;
    		}
    	}
    	if(!existe){
    		detalle.setVentasCabecera(instance);
    		VentasDetallesMapper ventasDetallesMapper = sqlSession.getMapper(VentasDetallesMapper.class);
    		ventasDetallesMapper.insertVentaDetalle(detalle);
    		detalles.add(detalle);
    	}
    }
    
    /*
    public void eliminarDetalleDeCarrito(VentasDetalles detalle){
    	detalle.setProducto(em.find(Productos.class, detalle.getProducto().getIdProducto()));
    	int i = 0, pos = -1;
    	for(VentasDetalles det : detalles){
    		if(det.getProducto().getIdProducto().equals(detalle.getProducto().getIdProducto())){
    			pos = i;
    			System.out.println("Se encuentra para eliminar");
    		}
    		i++;
    	}
    	if(pos!=-1){
    		detalles.remove(pos);
    	}
    }
    
    
    public void modificarDetalleCarrito(VentasDetalles detalle){
    	detalle.setProducto(em.find(Productos.class, detalle.getProducto().getIdProducto()));
    	for(VentasDetalles det : detalles){
    		if(det.getProducto().getIdProducto().equals(detalle.getProducto().getIdProducto())){
    			det.setCantidad(detalle.getCantidad());
    		}
    	}
    }
    */
    
    @Remove
    public String register() throws Exception{
    	Double sum = 0D;
    	
    	/*if(cliente!=null)
    		instance.setClientes(cliente);
    	else
    		return "noclient";
    	for(VentasDetalles det : detalles){
    		det.setVentasCabecera(instance);
    	}*/
    	instance.setEstado("PENDIENTE");
    	instance.setFechaDocumento(new Date());
    	if(detalles.isEmpty())
    		return "nodetails";
    	else
    		instance.setVentasDetalles(detalles);
		for(VentasDetalles det : instance.getVentasDetalles()){
			sum+=obtenerTotal(det.getCantidad(),det.getProducto().getIdProducto());
		}
		instance.setMontoTotal(sum);
		instance.setSaldo(sum);
    	instance.setFechaCreacion(new Date());
    	instance.setFechaActualizacion(new Date());
        log.info("Registering nueva Venta");
        VentasCabeceraMapper ventasCabeceraMapper = sqlSession.getMapper(VentasCabeceraMapper.class);
        ventasCabeceraMapper.updateVentaCabecera(instance);
        sqlSession.commit();
        try {
			trx.commit();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        //ventasEventSrc.fire(instance);
        //actualizarStock();
        //actualizarSaldoCliente();
        return "persisted";
        
    }
    
    
    public void cancelar() throws Exception{
    	sqlSession.rollback();
    	trx.rollback();
    }
    
    /*private void actualizarStock(){
    	for(VentasDetalles det : instance.getVentasDetalles()){
    		Productos producto = em.find(Productos.class, det.getProducto().getIdProducto());
    		producto.setCantidadDisponible(producto.getCantidadDisponible() - det.getCantidad());
    		producto.setFechaActualizacion(new Date());
    		producto = em.merge(producto);
    		em.flush();
    	}
    }
    
    private void actualizarSaldoCliente(){
    	Clientes cliente = em.find(Clientes.class, instance.getClientes().getIdCliente());
    	cliente.setSaldoVentas(cliente.getSaldoVentas()+instance.getMontoTotal());
    }*/
    
    
    public Double obtenerTotal(Long cant, Long id){
    	Productos prod = null;
    	ProductosMapper productosMapper = sqlSession.getMapper(ProductosMapper.class);
    	prod = productosMapper.getProductoById(id);
    	if(prod != null)
    		return prod.getPrecio()*cant;
    	else
    		return 0D;
    }
    
    
    public List<VentasDetalles> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<VentasDetalles> detalles) {
		this.detalles = detalles;
	}
	public Clientes getCliente() {
		return cliente;
	}
	public void setCliente(Clientes cliente) {
		Clientes cli = null;
		ClientesMapper clienteMapper = sqlSession.getMapper(ClientesMapper.class);
		cli = clienteMapper.getClienteById(cliente.getIdCliente());
		if(cli != null){
			this.cliente = cli;
		}
	}

	public VentasCabecera getInstance() {
		return instance;
	}

	public void setInstance(VentasCabecera instance) {
		this.instance = instance;
	}
	
    
    
    
}
