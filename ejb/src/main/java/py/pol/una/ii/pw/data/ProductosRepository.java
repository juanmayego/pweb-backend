package py.pol.una.ii.pw.data;

import javax.enterprise.context.ApplicationScoped;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.ProductosMapper;

@ApplicationScoped
public class ProductosRepository {
	
    public Productos findById(Long id) {
    	SqlSession sqlSession = new MyBatisUtil().getSession();
    	Productos tmp = null;
    	if(sqlSession != null){
    		try
            {
    			ProductosMapper productosMapper = sqlSession.getMapper(ProductosMapper.class);
            	tmp = productosMapper.getProductoById(id);
            } finally
            {
                sqlSession.close();
            }
    	}
    	return tmp;
    }
    
    public List<Productos> findAllOrderedByName(String descri) {
    	
    	SqlSession sqlSession = new MyBatisUtil().getSession();
    	List<Productos> tq = null;
    	if(sqlSession != null){
    		try
            {
            	ProductosMapper productosMapper = sqlSession.getMapper(ProductosMapper.class);
            	if(descri != null){
            		tq = productosMapper.filterProducto("%"+descri+"%");
            	}else{
            		tq = productosMapper.getAllProductos();
            	}
            } finally
            {
                sqlSession.close();
            }
    	}
    	return tq;
    }
}
