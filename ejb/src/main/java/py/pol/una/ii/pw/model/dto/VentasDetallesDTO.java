package py.pol.una.ii.pw.model.dto;

import py.pol.una.ii.pw.model.Productos;

public class VentasDetallesDTO {
	private Long idVentasDetalle;
	private Productos producto;
	private Long cantidad;
	
	public VentasDetallesDTO() {

	}
	public Long getIdVentasDetalle() {
		return idVentasDetalle;
	}
	public void setIdVentasDetalle(Long idVentasDetalle) {
		this.idVentasDetalle = idVentasDetalle;
	}
	public Productos getProducto() {
		return producto;
	}
	public void setProducto(Productos producto) {
		this.producto = producto;
	}
	public Long getCantidad() {
		return cantidad;
	}
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
	
	
}
