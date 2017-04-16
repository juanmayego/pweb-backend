package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.Date;

public class Clientes implements Serializable{

	private static final long serialVersionUID = -816519122828875618L;
	
	private Long idCliente;
	private String nombre;
	private String ciNro;
	private String  email;
	private String direccion;
	private Double SaldoVentas;
	private String numero;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	
	
	
	public Clientes() {
		super();
	}
	public Clientes(Long idCliente, String nombre, String email,
			String direccion, String numero, Date fechaCreacion,
			Date fechaActualizacion, String ciNro) {
		super();
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.ciNro = ciNro;
		this.email = email;
		this.direccion = direccion;
		this.numero = numero;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}
	

    public Long getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getCiNro() {
		return ciNro;
	}
	public void setCiNro(String ciNro) {
		this.ciNro = ciNro;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public Double getSaldoVentas() {
		return SaldoVentas;
	}
	public void setSaldoVentas(Double saldoVentas) {
		SaldoVentas = saldoVentas;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
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
