
package py.pol.una.ii.pw.service;

import py.pol.una.ii.pw.model.Proveedor;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ValidationException;

import java.util.Date;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ProveedorRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Proveedor> proveedorEventSrc;

    public void register(Proveedor proveedor) throws Exception {
    	proveedor.setFechaCreacion(new Date());
    	proveedor.setFechaActualizacion(new Date());
        log.info("Registering " + proveedor.getNombre());
        em.persist(proveedor);
        proveedorEventSrc.fire(proveedor);
    }
    
    
    
    public void update(Proveedor proveedor) throws Exception {
    	proveedor.setFechaActualizacion(new Date());
    	proveedor.setFechaCreacion(proveedor.getFechaCreacion());
        log.info("Se actualiza " + proveedor.getNombre());
        em.merge(proveedor);
        em.flush();
        proveedorEventSrc.fire(proveedor);
    }
    
    
    public String remove(Proveedor proveedor) throws ValidationException {
    	if(checkFk(proveedor)){
    		proveedor = em.merge(proveedor);
    		em.remove(proveedor);
            em.flush();
            proveedorEventSrc.fire(proveedor);
            return "removed";
    	}else{
    		throw new ValidationException("Proveedor en uso");
    	}
        
    }
    
    public Boolean checkFk(Proveedor proveedor){
    	TypedQuery<Long> tq = em
    			.createQuery("Select count(p) from Productos p"
    					+ " where p.proveedor=:proveedor", Long.class);
    	tq.setParameter("proveedor", proveedor);
    	Long l = tq.getSingleResult();
    	if(l>0){
    		return false;
    	}else{
    		return true;
    	}
    }
}
