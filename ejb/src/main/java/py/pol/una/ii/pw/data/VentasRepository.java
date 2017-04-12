package py.pol.una.ii.pw.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.gson.Gson;

import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;
import py.pol.una.ii.pw.model.dto.VentasCabeceraDTO;
import py.pol.una.ii.pw.model.dto.VentasDetallesDTO;


public class VentasRepository {
	@Inject
    private EntityManager em;

	private Integer limite = 10;
    public VentasCabecera findById(Long id) {
        return em.find(VentasCabecera.class, id);
    }
    @SuppressWarnings("unchecked")
	public String findAllCabeceras() throws IOException {
    	Gson otro = new Gson();
    	Long totalRegistros = obtenerCantidad();
    	
    	SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhhmmss");
    	String ruta = "c:\\filesrest\\send\\archivo"+format.format(new Date()) +".txt";
    	File archivo = new File(ruta);
    	BufferedWriter bw = null;
    	
    	if (!archivo.exists()) {
    		bw = new BufferedWriter(new FileWriter(archivo));
        }
    	
    	int i=0;
    	while(totalRegistros > i*limite){
    		//, ccab.fecha_actualizacion, ccab.fecha_creacion ,ccab.fecha_documento, ccab.monto_total, ccab.id_proveedor
    		Query tq = em.createNativeQuery("select ccab.id_ventas_cab from ventas_cabecera ccab order by ccab.id_ventas_cab offset :desde limit :limite");
    		System.out.println("Pasa");
    		tq.setParameter("desde", i*limite);
    		tq.setParameter("limite", limite);

    		List<BigInteger> result = tq.getResultList();

    		for(BigInteger object : result){
    			Long id = object.longValue();
    			VentasCabecera aux = em.find(VentasCabecera.class,id);

    			VentasCabeceraDTO tmp = new VentasCabeceraDTO();

    			tmp.setIdVentasCabecera(aux.getIdVentasCabecera());
    			tmp.setClientes(aux.getClientes());
    			tmp.setFechaDocumento(aux.getFechaDocumento());
    			tmp.setMontoTotal(aux.getMontoTotal());
    			tmp.setFechaDocumento(aux.getFechaDocumento());
    			tmp.setFechaActualizacion(aux.getFechaActualizacion());

    			tmp.setDetallesVenta(obtenerDetalle(aux));

    			bw.write(otro.toJson(tmp) + "\n");
    		}
    		i++;
    	}
    	bw.close();
        return ruta;

    }
    
    
    public Long obtenerCantidad(){
    	
    	TypedQuery<Long> tq = em.createQuery(
    			"Select count(c) from VentasCabecera c",
    			Long.class);
    	return tq.getSingleResult();
    	
    }
    
    public List<VentasDetallesDTO> obtenerDetalle(VentasCabecera compra){
    	TypedQuery<VentasDetalles> tq = em.createQuery(
        		"Select c from VentasDetalles c where c.ventasCabecera.idVentasCabecera = :compra", 
        		VentasDetalles.class);
    	tq.setParameter("compra", compra.getIdVentasCabecera());
    	List<VentasDetalles> result = tq.getResultList();
    	List<VentasDetallesDTO> detalles = new ArrayList<VentasDetallesDTO>();
    	for(VentasDetalles det : result){
    		VentasDetallesDTO tmp = new VentasDetallesDTO();
    		tmp.setIdVentasDetalle(det.getIdVentasDetalle());
    		tmp.setCantidad(det.getCantidad());
    		tmp.setProducto(det.getProducto());
    		detalles.add(tmp);
    	}
    	return detalles;//tq.getResultList();
    }
}
