package py.pol.una.ii.pw.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.ComprasCabeceraMapper;
import py.pol.una.ii.pw.mybatis.mappers.ComprasDetallesMapper;
import py.pol.una.ii.pw.mybatis.mappers.ProductosMapper;
import py.pol.una.ii.pw.mybatis.mappers.ProveedorMapper;


@Stateful
@TransactionManagement(value=TransactionManagementType.BEAN)
public class ComprasMasivasRegistration {
	/*@Inject
    private Logger log;*/

	/*@Inject
	private EntityManager em;*/

	@Resource
	private EJBContext context;

	private UserTransaction trx;
	
	private SqlSession sqlSession;

	public void initTransaction() throws Exception{
		sqlSession = new MyBatisUtil().getSession();
		trx=context.getUserTransaction();
		trx.begin();
		
	}    

	
	public String insertVentas(String compra) throws Exception{
		Gson gson = new GsonBuilder().create();
		ComprasCabecera p = gson.fromJson(compra, ComprasCabecera.class);

		ProveedorMapper proveedorMapper = sqlSession.getMapper(ProveedorMapper.class);
		ProductosMapper productosMapper = sqlSession.getMapper(ProductosMapper.class);
		
		ComprasCabeceraMapper comprasCabeceraMapper = sqlSession.getMapper(ComprasCabeceraMapper.class);
		ComprasDetallesMapper comprasDetallesMapper = sqlSession.getMapper(ComprasDetallesMapper.class);
		
		Proveedor proveedor =null;
		proveedor = proveedorMapper.getProveedorById(p.getProveedor().getIdProveedor());
		
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
			producto = productosMapper.getProductoById(det.getProducto().getIdProducto());
			
			if(producto == null){
				rollbackTransaction();
				return "ERROR_NE";
			}else{
				det.setProducto(producto);
			}
			
			det.setComprasCabecera(p);
		}
		
		comprasCabeceraMapper.insertCompraCabecera(p);
		
		for(ComprasDetalles det : p.getComprasDetalles()){
			det.setComprasCabecera(p);
			comprasDetallesMapper.insertCompraDetalle(det);
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
