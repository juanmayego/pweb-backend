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

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.Date;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ClientesRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Clientes> clienteEventSrc;

    public void register(Clientes cliente) throws Exception {
    	cliente.setFechaCreacion(new Date());
    	cliente.setFechaActualizacion(new Date());
        log.info("Registering " + cliente.getNombre());
        em.persist(cliente);
        clienteEventSrc.fire(cliente);
    }
    
    public void update(Clientes cliente) throws Exception{
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
    }
}
