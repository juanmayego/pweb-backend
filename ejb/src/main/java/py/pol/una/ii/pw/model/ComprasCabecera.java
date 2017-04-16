package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComprasCabecera implements Serializable{
	
	private static final long serialVersionUID = -7036753228467616468L;
	
	private Long idComprasCabecera;
	private Proveedor proveedor;
	private Double montoTotal;
	private Date fechaDocumento;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private List<ComprasDetalles> comprasDetalles = new ArrayList<ComprasDetalles>(
			0);
	
	
	public ComprasCabecera() {
		super();
	}
	public ComprasCabecera(Long idComprasCabecera, Proveedor proveedor,
			Date fechaDocumento, Date fechaCreacion, Date fechaActualizacion) {
		super();
		this.idComprasCabecera = idComprasCabecera;
		this.proveedor = proveedor;
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
	
	public List<ComprasDetalles> getComprasDetalles() {
		return comprasDetalles;
	}
	public void setComprasDetalles(List<ComprasDetalles> comprasDetalles) {
		this.comprasDetalles = comprasDetalles;
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
