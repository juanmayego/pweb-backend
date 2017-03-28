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
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;

@Stateful
@StatefulTimeout(unit=TimeUnit.MINUTES, value=30)
@TransactionManagement(TransactionManagementType.BEAN)
public class VentasCarritoRegistration {
	
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;
    
    @Resource
    private EJBContext context;

    private UserTransaction trx;

    private VentasCabecera instance;
    private List<VentasDetalles> detalles;
    private Clientes cliente;
	
    @PostConstruct
    public void start(){
    	instance = new VentasCabecera();
    	detalles = new ArrayList<VentasDetalles>();
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
    	em.persist(instance);
    }
    
    public void agregarDetalleACarrito(VentasDetalles detalle){
    	detalle.setProducto(em.find(Productos.class, detalle.getProducto().getIdProducto()));
    	Boolean existe = false;
    	for(VentasDetalles det : detalles){
    		if(det.getProducto().getIdProducto().equals(detalle.getProducto().getIdProducto())){
    			existe = true;
    		}
    	}
    	if(!existe){
    		detalle.setVentasCabecera(instance);
    		em.persist(detalle);
    		detalles.add(detalle);
    	}
    }
    
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
        em.persist(instance);
        trx.commit();
        //ventasEventSrc.fire(instance);
        //actualizarStock();
        //actualizarSaldoCliente();
        return "persisted";
        
    }
    
    
    public void cancelar() throws Exception{
    	trx.rollback();
    }
    
    private void actualizarStock(){
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
    }
    
    
    public Double obtenerTotal(Long cant, Long id){
    	Productos prod = em.find(Productos.class, id);
    	return prod.getPrecio()*cant;
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
		cliente = em.find(Clientes.class, cliente.getIdCliente());
		this.cliente = cliente;
	}

	public VentasCabecera getInstance() {
		return instance;
	}

	public void setInstance(VentasCabecera instance) {
		this.instance = instance;
	}
	
    
    
    
}
