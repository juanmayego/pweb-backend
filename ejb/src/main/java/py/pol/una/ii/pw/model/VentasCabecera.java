package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


	public Long getIdVentasCabecera() {
		return idVentasCabecera;
	}

	public void setIdVentasCabecera(Long idVentasCabecera) {
		this.idVentasCabecera = idVentasCabecera;
	}
	
	public Clientes getClientes() {
		return clientes;
	}
	public void setClientes(Clientes clientes) {
		this.clientes = clientes;
	}
	
	public List<VentasDetalles> getVentasDetalles() {
		return ventasDetalles;
	}

	public void setVentasDetalles(List<VentasDetalles> ventasDetalles) {
		this.ventasDetalles = ventasDetalles;
	}

	public Double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}
	
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	public Date getFechaDocumento() {
		return fechaDocumento;
	}
	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
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
