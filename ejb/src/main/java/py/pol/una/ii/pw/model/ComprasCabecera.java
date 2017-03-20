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
@Table(name="compras_cabecera")
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
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compra_cab_id_compra_cab_seq")
    @SequenceGenerator(name = "compra_cab_id_compra_cab_seq", initialValue = 1, allocationSize = 1, sequenceName = "compra_cab_id_compra_cab_seq")
	@Column(name="id_compras_cab")
	public Long getIdComprasCabecera() {
		return idComprasCabecera;
	}
	public void setIdComprasCabecera(Long idComprasCabecera) {
		this.idComprasCabecera = idComprasCabecera;
	}
	
	@ManyToOne
	@JoinColumn(name="id_proveedor")
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	
	@Column(name="monto_total")
	public Double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "comprasCabecera", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ComprasDetalles> getComprasDetalles() {
		return comprasDetalles;
	}
	public void setComprasDetalles(List<ComprasDetalles> comprasDetalles) {
		this.comprasDetalles = comprasDetalles;
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
