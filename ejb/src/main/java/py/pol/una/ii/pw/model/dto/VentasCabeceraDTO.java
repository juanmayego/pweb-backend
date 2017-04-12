package py.pol.una.ii.pw.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import py.pol.una.ii.pw.model.Clientes;

public class VentasCabeceraDTO {

	private Long idVentasCabecera;
	private Clientes clientes;
	private Double montoTotal;
	private Date fechaDocumento;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	
	private List<VentasDetallesDTO> detallesVenta = new ArrayList<VentasDetallesDTO>();
	
	public VentasCabeceraDTO() {
		
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
	public Double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
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
	public List<VentasDetallesDTO> getDetallesVenta() {
		return detallesVenta;
	}
	public void setDetallesVenta(List<VentasDetallesDTO> detallesVenta) {
		this.detallesVenta = detallesVenta;
	}
	
	
	
}
