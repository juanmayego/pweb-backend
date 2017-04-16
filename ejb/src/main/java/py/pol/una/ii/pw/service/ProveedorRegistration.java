
package py.pol.una.ii.pw.service;

import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.ProveedorMapper;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.validation.ValidationException;

import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ProveedorRegistration {

    @Inject
    private Logger log;

    @Inject
    private Event<Proveedor> proveedorEventSrc;

    public void register(Proveedor proveedor){
    	proveedor.setFechaActualizacion(new Date());
    	proveedor.setFechaCreacion(new Date());
    	
    	SqlSession sqlSession = new MyBatisUtil().getSession();
        
    	try
        {
        	ProveedorMapper proveedorMapper = sqlSession.getMapper(ProveedorMapper.class);
            proveedorMapper.insertProveedor(proveedor);
            
            sqlSession.commit();
        } finally
        {
            sqlSession.close();
        }
        proveedorEventSrc.fire(proveedor);
    }
    
    
    
    public void update(Proveedor proveedor) throws Exception {
    	proveedor.setFechaActualizacion(new Date());
    	proveedor.setFechaCreacion(proveedor.getFechaCreacion());
        log.info("Se actualiza " + proveedor.getNombre());
        SqlSession sqlSession = new MyBatisUtil().getSession();
        try
        {
        	ProveedorMapper proveedorMapper = sqlSession.getMapper(ProveedorMapper.class);
        	proveedorMapper.updateProveedor(proveedor);
            sqlSession.commit();
        } finally
        {
            sqlSession.close();
        }
        proveedorEventSrc.fire(proveedor);
    }
    
    
    public String remove(Proveedor proveedor) throws ValidationException {
    	if(checkFk(proveedor)){
    		SqlSession sqlSession = new MyBatisUtil().getSession();
            try
            {
            	ProveedorMapper proveedorMapper = sqlSession.getMapper(ProveedorMapper.class);
            	proveedorMapper.deleteProveedor(proveedor.getIdProveedor());
                sqlSession.commit();
            } finally
            {
                sqlSession.close();
            }
    		proveedorEventSrc.fire(proveedor);
            return "removed";
    	}else{
    		throw new ValidationException("Proveedor en uso");
    	}
        
    }
    
    public Boolean checkFk(Proveedor proveedor){
    	Integer l=-1;
    	SqlSession sqlSession = new MyBatisUtil().getSession();
        try
        {
        	ProveedorMapper proveedorMapper = sqlSession.getMapper(ProveedorMapper.class);
        	l = proveedorMapper.checkFkProveedor(proveedor);
            
        } finally
        {
            sqlSession.close();
        }
    	if(l>0){
    		return false;
    	}else{
    		return true;
    	}
    
    }
}
