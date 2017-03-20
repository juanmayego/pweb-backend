package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name = "productos")
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
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productos_id_productos_seq")
    @SequenceGenerator(name = "productos_id_productos_seq", initialValue = 1, allocationSize = 1, sequenceName = "productos_id_productos_seq")
	@Column(name="id_producto")
	public Long getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	
	
	@Column(name="descripcion")
	@Size(max=255)
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name="precio")
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	@Column(name="cantidad_disponible")
	public Double getCantidadDisponible() {
		return cantidadDisponible;
	}
	
	public void setCantidadDisponible(Double cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}
	
	@ManyToOne
	@JoinColumn(name="id_proveedor")
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	@Column(name="fecha_creacion")
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	@Column(name="fecha_actualizacion")
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	
}
