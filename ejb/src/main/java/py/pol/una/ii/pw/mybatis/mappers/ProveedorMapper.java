package py.pol.una.ii.pw.mybatis.mappers;

import java.util.List;

import py.pol.una.ii.pw.model.Proveedor;
public interface ProveedorMapper {
	public void insertProveedor(Proveedor proveedor);

	public Proveedor getProveedorById(Long idProveedor);

	public List<Proveedor> getAllProveedor();
	
	public List<Proveedor> filterProveedor(String filter);

	public void updateProveedor(Proveedor proveedor);

	public void deleteProveedor(Long idProveedor);
	
	public Integer checkFkProveedor(Proveedor proveedor);

}
