package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="ventas_cabecera")
public class VentasCabecera implements Serializable{
	
	private static final long serialVersionUID = -7036753228467616468L;
	
	private Long idVentasCabecera;
	private Clientes clientes;
	private Double montoTotal;
	private Double saldo;
	private String Estado;
	private Date fechaDocumento;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	
	private List<VentasDetalles> ventasDetalles = new ArrayList<VentasDetalles>(
			0);
	
	public VentasCabecera() {
		super();
	}
	
	public VentasCabecera(Long idVentasCabecera, Clientes clientes,
			Double montoTotal, Double saldo, String estado,
			Date fechaDocumento, Date fechaCreacion, Date fechaActualizacion) {
		super();
		this.idVentasCabecera = idVentasCabecera;
		this.clientes = clientes;
		this.montoTotal = montoTotal;
		this.saldo = saldo;
		Estado = estado;
		this.fechaDocumento = fechaDocumento;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}


	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venta_cab_id_venta_cab_seq")
    @SequenceGenerator(name = "venta_cab_id_venta_cab_seq", initialValue = 1, allocationSize = 1, sequenceName = "venta_cab_id_venta_cab_seq")
	@Column(name="id_ventas_cab")
	public Long getIdVentasCabecera() {
		return idVentasCabecera;
	}

	public void setIdVentasCabecera(Long idVentasCabecera) {
		this.idVentasCabecera = idVentasCabecera;
	}
	@ManyToOne
	@JoinColumn(name="id_cliente")
	public Clientes getClientes() {
		return clientes;
	}
	public void setClientes(Clientes clientes) {
		this.clientes = clientes;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "ventasCabecera", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<VentasDetalles> getVentasDetalles() {
		return ventasDetalles;
	}

	public void setVentasDetalles(List<VentasDetalles> ventasDetalles) {
		this.ventasDetalles = ventasDetalles;
	}

	@Column(name="monto_total")
	public Double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}
	
	@Column(name="saldo")
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	@Column(name="estado")
	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	@Column(name="fecha_documento")
	public Date getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
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
