package py.pol.una.ii.pw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;


@Stateful
@StatefulTimeout(unit=TimeUnit.MINUTES, value=30)
public class ComprasCarritoRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    private ComprasCabecera instance;
    private List<ComprasDetalles> detalles;
    private Proveedor proveedor;
	
    @PostConstruct
    public void start(){
    	instance = new ComprasCabecera();
    	detalles = new ArrayList<ComprasDetalles>();
    }
    
    
//    @Remove
    public void finish(){
    	instance = new ComprasCabecera();
    	detalles = new ArrayList<ComprasDetalles>();
    }
    public void agregarDetalleACarrito(ComprasDetalles detalle){
    	detalle.setProducto(em.find(Productos.class, detalle.getProducto().getIdProducto()));
    	Boolean existe = false;
    	for(ComprasDetalles det : detalles){
    		if(det.getProducto().getIdProducto().equals(detalle.getProducto().getIdProducto())){
    			existe = true;
    		}
    	}
    	
    	if(!existe){
    		detalles.add(detalle);
    	}
    }
    
    public void eliminarDetalleDeCarrito(ComprasDetalles detalle){
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
    }
    
    public String register(){
    	Double sum = 0D;
    	
    	if(proveedor!=null)
    		instance.setProveedor(proveedor);
    	else
    		return "noproveedor";
    	for(ComprasDetalles det : detalles){
    		det.setComprasCabecera(instance);
    	}
    	//instance.setEstado("PENDIENTE");
    	instance.setFechaDocumento(new Date());
    	if(detalles.isEmpty())
    		return "nodetails";
    	else
    		instance.setComprasDetalles(detalles);
		for(ComprasDetalles det : instance.getComprasDetalles()){
			sum+=obtenerTotal(det.getCantidad(),det.getProducto().getIdProducto());
		}
		instance.setMontoTotal(sum);
		//instance.setSaldo(sum);
    	instance.setFechaCreacion(new Date());
    	instance.setFechaActualizacion(new Date());
        log.info("Registering nueva Venta");
        em.persist(instance);
        //ventasEventSrc.fire(instance);
        //actualizarStock();
        //actualizarSaldoCliente();
        return "persisted";
        
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
    	Productos prod = em.find(Productos.class, id);
    	return prod.getPrecio()*cant;
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
		proveedor = em.find(Proveedor.class, proveedor.getIdProveedor());
		this.proveedor = proveedor;
	}

	public ComprasCabecera getInstance() {
		return instance;
	}

	public void setInstance(ComprasCabecera instance) {
		this.instance = instance;
	}
}
