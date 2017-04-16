package py.pol.una.ii.pw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
//import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.apache.ibatis.session.SqlSession;

import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.ComprasCabeceraMapper;
import py.pol.una.ii.pw.mybatis.mappers.ComprasDetallesMapper;
import py.pol.una.ii.pw.mybatis.mappers.ProductosMapper;
import py.pol.una.ii.pw.mybatis.mappers.ProveedorMapper;


@Stateful
@StatefulTimeout(unit=TimeUnit.MINUTES, value=30)
@TransactionManagement(TransactionManagementType.BEAN)
public class ComprasCarritoRegistration {
	
	@Inject
    private Logger log;

	@Resource
    private EJBContext context;

    private UserTransaction trx;

    private ComprasCabecera instance;
    private List<ComprasDetalles> detalles;
    private Proveedor proveedor;
    private SqlSession sqlSession;
    
    @PostConstruct
    public void start(){
    	instance = new ComprasCabecera();
    	detalles = new ArrayList<ComprasDetalles>();
    	sqlSession = new MyBatisUtil().getSession();
    }
    
    
    public void initCompra(ComprasCabecera compra) throws Exception{
    	
    	trx=context.getUserTransaction();
    	instance = compra;
    	instance.setFechaDocumento(new Date());
    	instance.setFechaCreacion(new Date());
    	instance.setFechaActualizacion(new Date());
    	trx.begin();
    	
    	ComprasCabeceraMapper comprasCabeceraMapper = sqlSession.getMapper(ComprasCabeceraMapper.class);
        comprasCabeceraMapper.insertCompraCabecera(instance);
    }
    
    
//    @Remove
    public void finish(){
    	instance = new ComprasCabecera();
    	detalles = new ArrayList<ComprasDetalles>();
    }
    
    
    
    public void agregarDetalleACarrito(ComprasDetalles detalle){
    	
    	ProductosMapper productosMapper = sqlSession.getMapper(ProductosMapper.class);
    	detalle.setProducto(productosMapper.getProductoById(detalle.getProducto().getIdProducto()));
    	
    	Boolean existe = false;
    	for(ComprasDetalles det : detalles){
    		if(det.getProducto().getIdProducto().equals(detalle.getProducto().getIdProducto())){
    			existe = true;
    		}
    	}
    	
    	if(!existe){
    		detalle.setComprasCabecera(instance);
    		ComprasDetallesMapper comprasDetallesMapper = sqlSession.getMapper(ComprasDetallesMapper.class);
    		comprasDetallesMapper.insertCompraDetalle(detalle);
    		detalles.add(detalle);
    	}
    }
    
    /*public void eliminarDetalleDeCarrito(ComprasDetalles detalle){
    	detalle.setProducto(em.find(Productos.class, detalle.getProducto().getIdProducto()));
    	int i = 0, pos = -1;
    	for(ComprasDetalles det : detalles){
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
    
    
    public void modificarDetalleCarrito(ComprasDetalles detalle){
    	detalle.setProducto(em.find(Productos.class, detalle.getProducto().getIdProducto()));
    	for(ComprasDetalles det : detalles){
    		if(det.getProducto().getIdProducto().equals(detalle.getProducto().getIdProducto())){
    			det.setCantidad(detalle.getCantidad());
    		}
    	}
    }*/
    
    public String register(){
    	Double sum = 0D;
    	
    	instance.setFechaDocumento(new Date());
    	if(detalles.isEmpty())
    		return "nodetails";
    	else
    		instance.setComprasDetalles(detalles);
		
    	for(ComprasDetalles det : instance.getComprasDetalles()){
			sum+=obtenerTotal(det.getCantidad(),det.getProducto().getIdProducto());
		}
    	
		instance.setMontoTotal(sum);
		instance.setFechaCreacion(new Date());
    	instance.setFechaActualizacion(new Date());
        log.info("Registering nueva Venta");
        
        ComprasCabeceraMapper comprasCabeceraMapper = sqlSession.getMapper(ComprasCabeceraMapper.class);
        comprasCabeceraMapper.updateCompraCabecera(instance);
        
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
    
//    private void actualizarStock(){
//    	for(ComprasDetalles det : instance.getVentasDetalles()){
//    		Productos producto = em.find(Productos.class, det.getProducto().getIdProducto());
//    		producto.setCantidadDisponible(producto.getCantidadDisponible() - det.getCantidad());
//    		producto.setFechaActualizacion(new Date());
//    		producto = em.merge(producto);
//    		em.flush();
//    	}
//    }
    
//    private void actualizarSaldoCliente(){
//    	Clientes cliente = em.find(Clientes.class, instance.getClientes().getIdCliente());
//    	cliente.setSaldoVentas(cliente.getSaldoVentas()+instance.getMontoTotal());
//    }
    public Double obtenerTotal(Long cant, Long id){
    	Productos prod = null;
    	ProductosMapper productosMapper = sqlSession.getMapper(ProductosMapper.class);
    	prod = productosMapper.getProductoById(id);
    	if(prod != null)
    		return prod.getPrecio()*cant;
    	else
    		return 0D;
    }
    public List<ComprasDetalles> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<ComprasDetalles> detalles) {
		this.detalles = detalles;
	}
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		Proveedor prov = null;
		ProveedorMapper proveedorMapper = sqlSession.getMapper(ProveedorMapper.class);
		prov = proveedorMapper.getProveedorById(proveedor.getIdProveedor());
		if(prov != null){
			this.proveedor = prov;
		}
	}
	public ComprasCabecera getInstance() {
		return instance;
	}
	public void setInstance(ComprasCabecera instance) {
		this.instance = instance;
	}
}
