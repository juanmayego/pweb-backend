package py.pol.una.ii.pw.mybatis.mappers;

import java.util.List;

import py.pol.una.ii.pw.model.ComprasCabecera;

public interface ComprasCabeceraMapper {
	
	public void insertCompraCabecera(ComprasCabecera compra);
	
	public ComprasCabecera getCompraById(Long idComprasCabecera);
	
	public List<ComprasCabecera> getAllCompras();
	
	public void updateCompraCabecera(ComprasCabecera compra);
}
