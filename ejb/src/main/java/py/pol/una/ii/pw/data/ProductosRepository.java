package py.pol.una.ii.pw.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import py.pol.una.ii.pw.model.Productos;

@ApplicationScoped
public class ProductosRepository {

    @Inject
    private EntityManager em;

    public Productos findById(Long id) {
        return em.find(Productos.class, id);
    }

    public Productos findByNombre(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Productos> criteria = cb.createQuery(Productos.class);
        Root<Productos> productos = criteria.from(Productos.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(productos).where(cb.equal(productos.get("descripcion"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Productos> findAllOrderedByName(String descri) {
    	String query = "Select c from Productos c";
        if(descri!=null){
        	query += " where lower(c.descripcion) like lower(:valor)";
        }
        query +=" order by c.descripcion";
        TypedQuery<Productos> tq = em.
        		createQuery(query, 
        				Productos.class);
    	if(descri!=null){
    		tq.setParameter("valor", "%"+descri+"%");
    	}
    	return tq.getResultList();
    }
}
