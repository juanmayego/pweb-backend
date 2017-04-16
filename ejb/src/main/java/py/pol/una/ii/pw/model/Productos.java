package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.Date;

public class Productos implements Serializable{

	private static final long serialVersionUID = -816519122828875618L;
	
	private Long idProducto;
	private String descripcion;
	private Double precio;
	private Double cantidadDisponible;
	private Proveedor proveedor;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	
	public Productos() {
		super();
	}
	
	public Productos(Long idProducto, String descripcion, Double precio,
			Double cantidadDisponible, Proveedor proveedor, Date fechaCreacion,
			Date fechaActualizacion) {
		super();
		this.idProducto = idProducto;
		this.descripcion = descripcion;
		this.precio = precio;
		this.cantidadDisponible = cantidadDisponible;
		this.proveedor = proveedor;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}
	
	public Long getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	public Double getCantidadDisponible() {
		return cantidadDisponible;
	}
	
	public void setCantidadDisponible(Double cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}
	
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	
}
