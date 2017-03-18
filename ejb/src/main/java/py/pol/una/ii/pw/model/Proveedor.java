package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name="proveedor")
public class Proveedor implements Serializable {

	private static final long serialVersionUID = 7287785120734255245L;
	
	
	private Long idProveedor;
	private String nombre;
	private String ruc;
	private String direccion;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	
	
	public Proveedor() {
		super();
	}


	public Proveedor(Long idProveedor, String nombre, String ruc,
			String direccion, Date fechaCreacion, Date fechaActualizacion) {
		super();
		this.idProveedor = idProveedor;
		this.nombre = nombre;
		this.ruc = ruc;
		this.direccion = direccion;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proveedor_id_proveedor_seq")
    @SequenceGenerator(name = "proveedor_id_proveedor_seq", initialValue = 1, allocationSize = 1, sequenceName = "proveedor_id_proveedor_seq")
	@Column(name="id_proveedor")
	public Long getIdProveedor() {
		return idProveedor;
	}
	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}
	
	@Column(name="nombre")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name="ruc")
	@Size(min = 10, max = 15)
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	
	@Column(name="direccion")
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
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
