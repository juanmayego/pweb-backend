/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package py.pol.una.ii.pw.data;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.stream.JsonGenerator;

import org.apache.ibatis.session.SqlSession;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.ComprasCabeceraMapper;
import py.pol.una.ii.pw.util.PaginacionCompras;

@ApplicationScoped
public class ComprasRepository {

    /*@Inject
    private EntityManager em;*/

    private Integer limite = 1000;
    
    
    public ComprasCabecera findById(Long id) {
        ComprasCabecera tmp = null;
        SqlSession sqlSession = new MyBatisUtil().getSession();
    	try
        {
        	ComprasCabeceraMapper comprasCabeceraMapper = sqlSession.getMapper(ComprasCabeceraMapper.class);
        	
        	tmp = comprasCabeceraMapper.getCompraById(id);
        	
        } finally
        {
            sqlSession.close();
        }
    	
    	return tmp;
    }
    
    
    
    /*public String findAllCabeceras() throws IOException {
    	
    	Gson gson = new GsonBuilder().create();
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
    			
    		Query tq = em.createNativeQuery("select ccab.id_compras_cab from compras_cabecera ccab order by ccab.id_compras_cab offset :desde limit :limite");
    		tq.setParameter("desde", i*limite);
    		tq.setParameter("limite", limite);
    		@SuppressWarnings("unchecked")
    		List<BigInteger> result = tq.getResultList();
    		for(BigInteger object : result){
    			Long id = object.longValue();
    			
    			ComprasCabecera aux = em.find(ComprasCabecera.class,id);

    			ComprasCabeceraDTO tmp = new ComprasCabeceraDTO();

    			tmp.setIdComprasCabecera(aux.getIdComprasCabecera());
    			tmp.setProveedor(aux.getProveedor());
    			tmp.setMontoTotal(aux.getMontoTotal());
    			tmp.setFechaCreacion(aux.getFechaCreacion());
    			tmp.setFechaDocumento(aux.getFechaDocumento());
    			tmp.setFechaActualizacion(aux.getFechaActualizacion());

    			tmp.setComprasDetalles(obtenerDetalle(aux));

    			bw.write(gson.toJson(tmp) + "\n");
    		}
    		i++;
    	}
        
    	bw.close();
        
        return ruta;

    }*/
    
    public void streamCompras(OutputStream stream){
    	JsonGenerator generator = Json.createGenerator(stream);
    	SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    	Integer totalRegistros = obtenerCantidad();
    	SqlSession sqlSession = new MyBatisUtil().getSession();
    	PaginacionCompras page = new PaginacionCompras();
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
    		List<ComprasCabecera> listCompras = null;
        	
        	
        	ComprasCabeceraMapper comprasCabeceraMapper = sqlSession.getMapper(ComprasCabeceraMapper.class);
        	listCompras = comprasCabeceraMapper.getAllCompras(page);
  
        	for(ComprasCabecera aux : listCompras){
				
        		generator.writeStartObject();
					generator.write("idComprasCabecera", aux.getIdComprasCabecera());
					generator.write("proveedor", aux.getProveedor().getNombre());
					generator.write("montoTotal", aux.getMontoTotal());
					generator.write("fechaDocumento", f.format(aux.getFechaDocumento()));
					generator.write("fechaCreacion", f.format(aux.getFechaCreacion()));
					generator.write("fechaActualizacion", f.format(aux.getFechaActualizacion()));
					generator.writeStartArray("comprasDetalles");
					for(ComprasDetalles detalle : aux.getComprasDetalles()){
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
        	ComprasCabeceraMapper comprasCabeceraMapper = sqlSession.getMapper(ComprasCabeceraMapper.class);
        	tmp = comprasCabeceraMapper.cantidadComprasCabecera();
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
        	ComprasCabeceraMapper comprasCabeceraMapper = sqlSession.getMapper(ComprasCabeceraMapper.class);
        	tmp = comprasCabeceraMapper.cantidadRegistrosDeMilCabs(offset);
        	if(tmp != null){
        		System.out.println("todo ok");
        	}
        } finally
        {
            sqlSession.close();
        }
    	return tmp;
    }
    
    
    /*public List<ComprasDetallesDTO> obtenerDetalle(ComprasCabecera compra){
    	TypedQuery<ComprasDetalles> tq = em.createQuery(
        		"Select c from ComprasDetalles c where c.comprasCabecera = :compra", 
        		ComprasDetalles.class);
    	tq.setParameter("compra", compra);
    	List<ComprasDetalles> result = tq.getResultList();
    	List<ComprasDetallesDTO> detalles = new ArrayList<ComprasDetallesDTO>();
    	for(ComprasDetalles det : result){
    		ComprasDetallesDTO tmp = new ComprasDetallesDTO();
    		tmp.setIdCompraDetalle(det.getIdComprasDetalle());
    		tmp.setCantidad(det.getCantidad());
    		tmp.setProducto(det.getProducto());
    		detalles.add(tmp);
    	}
    	return detalles;//tq.getResultList();
    }*/
}
