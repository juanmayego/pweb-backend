package py.pol.una.ii.pw.mybatis.mappers;

import java.util.List;

import py.pol.una.ii.pw.model.Productos;

public interface ProductosMapper {
	
	public void insertProductos(Productos productos);

	public Productos getProductoById(Long idProducto);

	public List<Productos> getAllProductos();
	
	public List<Productos> filterProducto(String filter);

	/*public void updateProveedor(Productos proveedor);

	public void deleteProveedor(Long idProveedor);
	
	public Integer checkFkProveedor(Productos proveedor);*/
}
