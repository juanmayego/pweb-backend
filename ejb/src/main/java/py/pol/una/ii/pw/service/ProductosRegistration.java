package py.pol.una.ii.pw.service;
import py.pol.una.ii.pw.model.Productos;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.Date;
import java.util.logging.Logger;

@Stateless
public class ProductosRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Productos> productosEventSrc;

    public void register(Productos producto) throws Exception {
        producto.setFechaActualizacion(new Date());
        producto.setFechaCreacion(new Date());
    	log.info("Registering " + producto.getDescripcion());
        em.persist(producto);
        productosEventSrc.fire(producto);
    }
    
    public void update(Productos producto) throws Exception {
        producto.setFechaActualizacion(new Date());
        producto.setFechaCreacion(producto.getFechaCreacion());
        log.info("Se actualiza " + producto.getDescripcion());
        em.merge(producto);
        em.flush();
        productosEventSrc.fire(producto);
    }
    
    
    public void remove(Productos producto) throws Exception {
        producto = em.merge(producto);
        em.remove(producto);
        em.flush();
        productosEventSrc.fire(producto);
    }
}
