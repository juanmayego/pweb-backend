package py.pol.una.ii.pw.data;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.json.Json;
import javax.json.stream.JsonGenerator;

import org.apache.ibatis.session.SqlSession;
import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.VentasCabeceraMapper;
import py.pol.una.ii.pw.util.PaginacionVentas;


public class VentasRepository {
	

	private Integer limite = 1000;
    
	public VentasCabecera findById(Long id) {
		VentasCabecera tmp = null;
        SqlSession sqlSession = new MyBatisUtil().getSession();
    	try
        {
        	VentasCabeceraMapper ventasCabeceraMapper = sqlSession.getMapper(VentasCabeceraMapper.class);
        	
        	tmp = ventasCabeceraMapper.getCompraById(id);
        	
        } finally
        {
            sqlSession.close();
        }
    	
    	return tmp;
    }
    
	/*
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
    
	*/
    
	public void streamVentas(OutputStream stream){
    	JsonGenerator generator = Json.createGenerator(stream);
    	SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    	Integer totalRegistros = obtenerCantidad();
    	SqlSession sqlSession = new MyBatisUtil().getSession();
    	PaginacionVentas page = new PaginacionVentas();
    	Integer i=0;
    	page.setDesde(0);
    	page.setLimite(obtenerCantidadRegistros(i*1000));
    	
    	if(totalRegistros!=null){
    		System.out.println("Total "+totalRegistros);
    	}else{
    		System.out.println("HAY UN ERROR EN TOTAL");
    	}
    	
    	if(page!=null && page.getDesde()!=null){
    		System.out.println("DESDE "+page.getDesde());
    	}else{
    		System.out.println("HAY UN ERROR EN DESDE");
    	}
    	
    	if(page!=null && page.getLimite()!=null){
    		System.out.println("LIMTE "+page.getLimite());
    	}else{
    		System.out.println("HAY UN ERROR EN LIMITE");
    	}
    	generator
        .writeStartArray();
    	while(page.getLimite() != null && totalRegistros >= (page.getDesde()+page.getLimite())){
    		List<VentasCabecera> listVentas = null;
        	
        	
        	VentasCabeceraMapper ventasCabeceraMapper = sqlSession.getMapper(VentasCabeceraMapper.class);
        	listVentas = ventasCabeceraMapper.getAllVentas(page);
  
        	for(VentasCabecera aux : listVentas){
				
        		generator.writeStartObject();
					generator.write("idVentasCabecera", aux.getIdVentasCabecera());
					generator.write("cliente", aux.getClientes().getNombre());
					generator.write("montoTotal", aux.getMontoTotal());
					generator.write("fechaDocumento", f.format(aux.getFechaDocumento()));
					generator.write("fechaCreacion", f.format(aux.getFechaCreacion()));
					generator.write("fechaActualizacion", f.format(aux.getFechaActualizacion()));
					generator.writeStartArray("ventasDetalles");
					for(VentasDetalles detalle : aux.getVentasDetalles()){
						generator.writeStartObject();
						generator.write("producto", detalle.getProducto().getDescripcion());
						generator.write("cantidad", detalle.getCantidad());
						generator.writeEnd();
					}
					generator.writeEnd();
				generator.writeEnd();
			}
			i++;
			page.setDesde(page.getLimite());
			page.setLimite(obtenerCantidadRegistros(i*1000));
			System.out.println(i*limite+" registros cargados");
		}
    	generator.writeEnd();
    	generator.flush();
    	generator.close();
    	sqlSession.close();
    }
	
	public Integer obtenerCantidad(){
    	Integer tmp = null;
    	SqlSession sqlSession = new MyBatisUtil().getSession();
    	try
        {
        	VentasCabeceraMapper ventasCabeceraMapper = sqlSession.getMapper(VentasCabeceraMapper.class);
        	tmp = ventasCabeceraMapper.cantidadVentasCabecera();
        } finally
        {
            sqlSession.close();
        }
    	return tmp;
    }
	
	public Integer obtenerCantidadRegistros(Integer offset){
    	Integer tmp = null;
    	SqlSession sqlSession = new MyBatisUtil().getSession();
    	try
        {
        	VentasCabeceraMapper ventasCabeceraMapper = sqlSession.getMapper(VentasCabeceraMapper.class);
        	tmp = ventasCabeceraMapper.cantidadRegistrosDeMilCabs(offset);
        	if(tmp != null){
        		System.out.println("todo ok");
        	}
        } finally
        {
            sqlSession.close();
        }
    	return tmp;
    }
    
    
//    public List<VentasDetallesDTO> obtenerDetalle(VentasCabecera compra){
//    	TypedQuery<VentasDetalles> tq = em.createQuery(
//        		"Select c from VentasDetalles c where c.ventasCabecera.idVentasCabecera = :compra", 
//        		VentasDetalles.class);
//    	tq.setParameter("compra", compra.getIdVentasCabecera());
//    	List<VentasDetalles> result = tq.getResultList();
//    	List<VentasDetallesDTO> detalles = new ArrayList<VentasDetallesDTO>();
//    	for(VentasDetalles det : result){
//    		VentasDetallesDTO tmp = new VentasDetallesDTO();
//    		tmp.setIdVentasDetalle(det.getIdVentasDetalle());
//    		tmp.setCantidad(det.getCantidad());
//    		tmp.setProducto(det.getProducto());
//    		detalles.add(tmp);
//    	}
//    	return detalles;//tq.getResultList();
//    }
}
