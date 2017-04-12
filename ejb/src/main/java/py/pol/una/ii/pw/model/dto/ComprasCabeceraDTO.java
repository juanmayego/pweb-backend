package py.pol.una.ii.pw.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import py.pol.una.ii.pw.model.Proveedor;

public class ComprasCabeceraDTO {
	private Long idComprasCabecera;
	private Proveedor proveedor;
	private Double montoTotal;
	private Date fechaDocumento;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	
	private List<ComprasDetallesDTO> comprasDetalles = new ArrayList<ComprasDetallesDTO>(
			0);
	public ComprasCabeceraDTO() {
		
	}
	public ComprasCabeceraDTO(Long idComprasCabecera, Proveedor proveedor,
			Double montoTotal, Date fechaDocumento, Date fechaCreacion,
			Date fechaActualizacion) {
		//super();
		this.idComprasCabecera = idComprasCabecera;
		this.proveedor = proveedor;
		this.montoTotal = montoTotal;
		this.fechaDocumento = fechaDocumento;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}
	public Long getIdComprasCabecera() {
		return idComprasCabecera;
	}
	public void setIdComprasCabecera(Long idComprasCabecera) {
		this.idComprasCabecera = idComprasCabecera;
	}
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
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
	public List<ComprasDetallesDTO> getComprasDetalles() {
		return comprasDetalles;
	}
	public void setComprasDetalles(List<ComprasDetallesDTO> comprasDetalles) {
		this.comprasDetalles = comprasDetalles;
	}
	
	
}
