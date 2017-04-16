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


public class ComprasDetalles implements Serializable{
	
	
	private static final long serialVersionUID = -2833197326101397234L;
	
	private Long idComprasDetalle;
	private ComprasCabecera comprasCabecera;
	private Productos producto;
	private Long cantidad;
	
	
	public ComprasDetalles() {
		super();
	}
	public ComprasDetalles(Long idComprasDetalle, Productos producto,
			Long cantidad) {
		super();
		this.idComprasDetalle = idComprasDetalle;
		this.producto = producto;
		this.cantidad = cantidad;
	}
	
	public Long getIdComprasDetalle() {
		return idComprasDetalle;
	}
	public void setIdComprasDetalle(Long idComprasDetalle) {
		this.idComprasDetalle = idComprasDetalle;
	}
	
	public ComprasCabecera getComprasCabecera() {
		return comprasCabecera;
	}
	public void setComprasCabecera(ComprasCabecera comprasCabecera) {
		this.comprasCabecera = comprasCabecera;
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
