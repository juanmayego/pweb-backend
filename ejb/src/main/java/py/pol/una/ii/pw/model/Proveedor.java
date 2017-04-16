package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.Date;

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
	
	public Long getIdProveedor() {
		return idProveedor;
	}
	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
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
