package py.pol.una.ii.pw.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.Proveedor;


@Stateful
@TransactionManagement(value=TransactionManagementType.BEAN)
public class ComprasMasivasRegistration {
	/*@Inject
    private Logger log;*/

	@Inject
	private EntityManager em;

	@Resource
	private EJBContext context;

	
	private UserTransaction trx;

	public void initTransaction() throws Exception{
		trx=context.getUserTransaction();
		trx.begin();
	}    

	public String insertVentas(String compra) throws Exception{
		Gson gson = new GsonBuilder().create();
		ComprasCabecera p = gson.fromJson(compra, ComprasCabecera.class);
		
		Proveedor proveedor =null;
		proveedor = em.find(Proveedor.class, p.getProveedor().getIdProveedor());
		if(proveedor == null){
			rollbackTransaction();
			return "ERROR_NE";
		}else{
			p.setProveedor(proveedor);
		}
		p.setFechaActualizacion(new Date());
		p.setFechaDocumento(new Date());
		p.setFechaCreacion(new Date());

		for(ComprasDetalles det : p.getComprasDetalles()){
			Productos producto = null;
			producto = em.find(Productos.class, det.getProducto().getIdProducto());
			if(producto == null){
				rollbackTransaction();
				return "ERROR_NE";
			}else{
				det.setProducto(producto);
			}
			det.setComprasCabecera(p);
		}
		em.persist(p);
		return "NEXT";
		
	}

	public void finishTransaction() throws Exception{
		trx.commit();
	}

	public void rollbackTransaction()throws Exception{
		trx.rollback();
	}

}
