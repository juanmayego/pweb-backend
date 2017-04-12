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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.model.dto.ComprasCabeceraDTO;
import py.pol.una.ii.pw.model.dto.ComprasDetallesDTO;

@ApplicationScoped
public class ComprasRepository {

    @Inject
    private EntityManager em;

    private Integer limite = 10;
    public ComprasCabecera findById(Long id) {
        return em.find(ComprasCabecera.class, id);
    }
    public String findAllCabeceras() throws IOException {
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

    }
    
    
    public Long obtenerCantidad(){
    	
    	TypedQuery<Long> tq = em.createQuery(
    			"Select count(c) from ComprasCabecera c",
    			Long.class);
    	return tq.getSingleResult();
    	
    }
    public List<ComprasDetallesDTO> obtenerDetalle(ComprasCabecera compra){
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
    }
}
