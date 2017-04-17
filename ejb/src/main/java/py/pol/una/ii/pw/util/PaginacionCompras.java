package py.pol.una.ii.pw.util;

public class PaginacionCompras {
	private Integer desde;
	private Integer limite;
	
	public PaginacionCompras() {
	}
	public PaginacionCompras(Integer desde, Integer limite) {
		this.desde = desde;
		this.limite = limite;
	}
	public Integer getDesde() {
		return desde;
	}
	public void setDesde(Integer desde) {
		this.desde = desde;
	}
	public Integer getLimite() {
		return limite;
	}
	public void setLimite(Integer limite) {
		this.limite = limite;
	}
	
	
}
