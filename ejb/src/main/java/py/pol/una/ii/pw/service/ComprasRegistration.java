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
package py.pol.una.ii.pw.service;

import py.pol.una.ii.pw.model.ComprasCabecera;
import py.pol.una.ii.pw.model.ComprasDetalles;
import py.pol.una.ii.pw.model.Productos;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ComprasRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<ComprasCabecera> comprasEventSrc;

    public String register(ComprasCabecera instance) throws Exception {
    	if(validateProductos(instance)){
    		Double sum = 0D;
    		for(ComprasDetalles det : instance.getComprasDetalles()){
    			sum+=obtenerTotal(det.getCantidad(),det.getProducto().getIdProducto());
    		}
    		instance.setMontoTotal(sum);
    		instance.setFechaCreacion(new Date());
        	instance.setFechaActualizacion(new Date());
            log.info("Registering " + instance.getFechaDocumento());
            em.persist(instance);
            comprasEventSrc.fire(instance);
            return "persisted";
    	}else{
    		return "invalid";
    	}
    	
    }
    
    /*public void update(Clientes cliente) throws Exception{
    	cliente.setFechaActualizacion(new Date());
    	
    	log.info("Se actualizo cliente " + cliente.getIdCliente());
    	em.merge(cliente);
    	em.flush();
    	clienteEventSrc.fire(cliente);
    }
    
    public void remove(Clientes cliente) throws Exception{
    	log.info("Se elimino cliente " + cliente.getIdCliente());
    	cliente = em.merge(cliente);
    	em.remove(cliente);
    	em.flush();
    	clienteEventSrc.fire(cliente);
    }*/
    
    public List<ComprasCabecera> listAll(){
    	 List<ComprasCabecera> compras; 
    	TypedQuery<ComprasCabecera> tq = em.createQuery("Select c from ComprasCabecera c"
				,ComprasCabecera.class);
    	
    	compras = tq.getResultList();
    	
    	for(ComprasCabecera compra : compras){
    		TypedQuery<ComprasDetalles> tq2 = em.createQuery("Select c from ComprasDetalles c where c.comprasCabecera =:compra"
    				,ComprasDetalles.class);
    		tq2.setParameter("compra", compra);
    		compra.setComprasDetalles(tq2.getResultList());
    	}
    	
    	return compras;
    }
    public Boolean validateProductos(ComprasCabecera venta){
    	List<Productos> productos = new ArrayList<Productos>();
    	for(ComprasDetalles det : venta.getComprasDetalles()){
    		productos.add(det.getProducto());
    	}
    	TypedQuery<Long> tq = em.createQuery("select count(pr) "+
    				" from Productos pr where pr.proveedor = :idProveedor and pr in :productos "
    				,Long.class);
    	tq.setParameter("productos", productos);
    	tq.setParameter("idProveedor", venta.getProveedor());
    	
    	Long l = tq.getSingleResult();
    	if((productos.size() - l)>0){
    		return false;
    	}else{
    		return true;
    	}
    }
    
    public ComprasCabecera findById(Long id){
    	return em.find(ComprasCabecera.class, id);
    }
    
    
    public Double obtenerTotal(Long cant, Long id){
    	Productos prod = em.find(Productos.class, id);
    	return prod.getPrecio()*cant;
    }
}
