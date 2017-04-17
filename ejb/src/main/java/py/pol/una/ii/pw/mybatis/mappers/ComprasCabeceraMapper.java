package py.pol.una.ii.pw.mybatis.mappers;

import java.util.List;

import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.util.PaginacionCompras;

public interface ComprasCabeceraMapper {
	
	public void insertCompraCabecera(ComprasCabecera compra);
	
	public ComprasCabecera getCompraById(Long idComprasCabecera);
	
	public List<ComprasCabecera> getAllCompras(PaginacionCompras paginacionCompras);
	
	public void updateCompraCabecera(ComprasCabecera compra);
	
	public Integer cantidadRegistrosDeMilCabs(Integer desde);
	
	public Integer cantidadComprasCabecera();
}
