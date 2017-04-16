package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.Date;

public class Pagos implements Serializable{

	private static final long serialVersionUID = -816519122828875618L;
	
	private Long idPago;
	private Clientes clientes;
	private Date fechaPago;
	private Double monto;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	
	
	
	public Pagos() {
		super();
	}
	
	public Long getIdPago() {
		return idPago;
	}
	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}

	public Clientes getClientes() {
		return clientes;
	}
	public void setClientes(Clientes clientes) {
		this.clientes = clientes;
	}

	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
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
