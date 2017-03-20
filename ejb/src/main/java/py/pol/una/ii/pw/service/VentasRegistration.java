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

import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Productos;
import py.pol.una.ii.pw.model.VentasCabecera;
import py.pol.una.ii.pw.model.VentasDetalles;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.Date;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class VentasRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<VentasCabecera> ventasEventSrc;

    public void register(VentasCabecera instance) throws Exception {
    	
    	Double sum = 0D;
		for(VentasDetalles det : instance.getVentasDetalles()){
			sum+=obtenerTotal(det.getCantidad(),det.getProducto().getIdProducto());
		}
		instance.setMontoTotal(sum);
		instance.setSaldo(sum);
    	instance.setFechaCreacion(new Date());
    	instance.setFechaActualizacion(new Date());
        log.info("Registering " + instance.getFechaDocumento());
        em.persist(instance);
        ventasEventSrc.fire(instance);
        actualizarStock(instance);
        actualizarSaldoCliente(instance);
    }
    
    
    private void actualizarStock(VentasCabecera venta){
    	for(VentasDetalles det : venta.getVentasDetalles()){
    		Productos producto = em.find(Productos.class, det.getProducto().getIdProducto());
    		producto.setCantidadDisponible(producto.getCantidadDisponible() - det.getCantidad());
    		producto.setFechaActualizacion(new Date());
    		producto = em.merge(producto);
    		em.flush();
    	}
    }
    
    private void actualizarSaldoCliente(VentasCabecera venta){
    	Clientes cliente = em.find(Clientes.class, venta.getClientes().getIdCliente());
    	cliente.setSaldoVentas(cliente.getSaldoVentas()+venta.getMontoTotal());
    }
    
    
    public Double obtenerTotal(Long cant, Long id){
    	Productos prod = em.find(Productos.class, id);
    	return prod.getPrecio()*cant;
    }
    
    
}
