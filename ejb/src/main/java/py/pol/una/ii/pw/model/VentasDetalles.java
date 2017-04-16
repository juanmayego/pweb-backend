package py.pol.una.ii.pw.model;

import java.io.Serializable;


public class VentasDetalles implements Serializable{
	
	
	private static final long serialVersionUID = -2833197326101397234L;
	
	private Long idVentasDetalle;
	private VentasCabecera ventasCabecera;
	private Productos producto;
	private Long cantidad;
	
	
	public VentasDetalles() {
		super();
	}
	
	
	public VentasDetalles(Long idVentasDetalle, VentasCabecera ventasCabecera,
			Productos producto, Long cantidad) {
		super();
		this.idVentasDetalle = idVentasDetalle;
		this.ventasCabecera = ventasCabecera;
		this.producto = producto;
		this.cantidad = cantidad;
	}
	public Long getIdVentasDetalle() {
		return idVentasDetalle;
	}

	public void setIdVentasDetalle(Long idVentasDetalle) {
		this.idVentasDetalle = idVentasDetalle;
	}

	public VentasCabecera getVentasCabecera() {
		return ventasCabecera;
	}

	public void setVentasCabecera(VentasCabecera ventasCabecera) {
		this.ventasCabecera = ventasCabecera;
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
