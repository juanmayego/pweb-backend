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

import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;


@Stateful
@TransactionManagement(value=TransactionManagementType.BEAN)
public class VentasMasivasRegistration {
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

	public String insertVentas(String venta) throws Exception{
		Gson gson = new GsonBuilder().create();
		VentasCabecera p = gson.fromJson(venta, VentasCabecera.class);
		
		Clientes cliente =null;
		cliente = em.find(Clientes.class, p.getClientes().getIdCliente());
		if(cliente == null){
			rollbackTransaction();
			return "ERROR_NE";
		}else{
			p.setClientes(cliente);
		}
		p.setFechaActualizacion(new Date());
		p.setFechaDocumento(new Date());
		p.setFechaCreacion(new Date());

		for(VentasDetalles det : p.getVentasDetalles()){
			Productos producto = null;
			producto = em.find(Productos.class, det.getProducto().getIdProducto());
			if(producto == null){
				rollbackTransaction();
				return "ERROR_NE";
			}else{
				det.setProducto(producto);
			}
			det.setVentasCabecera(p);
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
