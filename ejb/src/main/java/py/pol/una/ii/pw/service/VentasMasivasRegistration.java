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

import org.apache.ibatis.session.SqlSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.VentasCabeceraMapper;
import py.pol.una.ii.pw.mybatis.mappers.VentasDetallesMapper;
import py.pol.una.ii.pw.mybatis.mappers.ProductosMapper;
import py.pol.una.ii.pw.mybatis.mappers.ClientesMapper;


@Stateful
@TransactionManagement(value=TransactionManagementType.BEAN)
public class VentasMasivasRegistration {
	/*@Inject
    private Logger log;*/

	
	@Resource
	private EJBContext context;

	
	private UserTransaction trx;
	private SqlSession sqlSession;

	public void initTransaction() throws Exception{
		sqlSession = new MyBatisUtil().getSession();
		trx=context.getUserTransaction();
		trx.begin();
	}    

	public String insertVentas(String venta) throws Exception{
		Gson gson = new GsonBuilder().create();
		VentasCabecera p = gson.fromJson(venta, VentasCabecera.class);
		
		ClientesMapper clienteMapper = sqlSession.getMapper(ClientesMapper.class);
		ProductosMapper productosMapper = sqlSession.getMapper(ProductosMapper.class);
		
		VentasCabeceraMapper ventasCabeceraMapper = sqlSession.getMapper(VentasCabeceraMapper.class);
		VentasDetallesMapper ventasDetallesMapper = sqlSession.getMapper(VentasDetallesMapper.class);
		
		Clientes cliente =null;
		cliente = clienteMapper.getClienteById(p.getClientes().getIdCliente());
		
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
			producto = productosMapper.getProductoById(det.getProducto().getIdProducto());
			if(producto == null){
				rollbackTransaction();
				return "ERROR_NE";
			}else{
				det.setProducto(producto);
			}
			det.setVentasCabecera(p);
		}
		ventasCabeceraMapper.insertVentaCabecera(p);
		
		for(VentasDetalles det : p.getVentasDetalles()){
			det.setVentasCabecera(p);
			ventasDetallesMapper.insertVentaDetalle(det);
		}
		return "NEXT";
		
	}

	public void finishTransaction() throws Exception{
		sqlSession.commit();
		sqlSession.close();
		trx.commit();
	}

	public void rollbackTransaction()throws Exception{
		sqlSession.rollback();
		sqlSession.close();
		trx.rollback();
	}

}
