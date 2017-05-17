package py.pol.una.ii.pw.mybatis.mappers;

import java.util.List;

import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.util.PaginacionVentas;

public interface VentasCabeceraMapper {
public void insertVentaCabecera(VentasCabecera venta);
	
	public VentasCabecera getCompraById(Long idVentasCabecera);
	
	public List<VentasCabecera> getAllVentas(PaginacionVentas paginacionVentas);
	
	public void updateVentaCabecera(VentasCabecera venta);
	
	public Integer cantidadRegistrosDeMilCabs(Integer desde);
	
	public Integer cantidadVentasCabecera();
}
