package py.pol.una.ii.pw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


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
