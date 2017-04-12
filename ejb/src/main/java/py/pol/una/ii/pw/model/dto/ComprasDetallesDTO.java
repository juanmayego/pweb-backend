package py.pol.una.ii.pw.model.dto;

import py.pol.una.ii.pw.model.Productos;

public class ComprasDetallesDTO {
	private Long idCompraDetalle;
	private Long Cantidad;
	private Productos producto;
	
	public ComprasDetallesDTO() {
		
	}
	public ComprasDetallesDTO(Long idCompraDetalle, Long cantidad,
			Productos producto) {
		//super();
		this.idCompraDetalle = idCompraDetalle;
		Cantidad = cantidad;
		this.producto = producto;
	}
	public Long getIdCompraDetalle() {
		return idCompraDetalle;
	}
	public void setIdCompraDetalle(Long idCompraDetalle) {
		this.idCompraDetalle = idCompraDetalle;
	}
	public Long getCantidad() {
		return Cantidad;
	}
	public void setCantidad(Long cantidad) {
		Cantidad = cantidad;
	}
	public Productos getProducto() {
		return producto;
	}
	public void setProducto(Productos producto) {
		this.producto = producto;
	}
	
	
	
}
