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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

@Entity
@XmlRootElement
@Table(name = "cliente", uniqueConstraints = @UniqueConstraint(columnNames = "ci_numero"))
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
	

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_id_cliente_seq")
    @SequenceGenerator(name = "cliente_id_cliente_seq", initialValue = 1, allocationSize = 1, sequenceName = "cliente_id_cliente_seq")
    @Column(name="id_cliente")
	public Long getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	
	@NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name="ci_numero")
	public String getCiNro() {
		return ciNro;
	}
	public void setCiNro(String ciNro) {
		this.ciNro = ciNro;
	}
	@Email
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="direccion")
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	@Column(name="saldo_ventas", nullable=true)
	public Double getSaldoVentas() {
		return SaldoVentas;
	}
	public void setSaldoVentas(Double saldoVentas) {
		SaldoVentas = saldoVentas;
	}
	@Column(name="numero")
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
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
