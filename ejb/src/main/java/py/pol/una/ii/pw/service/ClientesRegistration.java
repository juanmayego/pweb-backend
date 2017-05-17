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
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.mybatis.MyBatisUtil;
import py.pol.una.ii.pw.mybatis.mappers.ClientesMapper;
import py.pol.una.ii.pw.mybatis.mappers.ProveedorMapper;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ValidationException;

import org.apache.ibatis.session.SqlSession;

import java.util.Date;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ClientesRegistration {

    @Inject
    private Logger log;

    @Inject
    private Event<Clientes> clienteEventSrc;

    public void register(Clientes cliente) throws Exception {
    	cliente.setFechaCreacion(new Date());
    	cliente.setFechaActualizacion(new Date());
        

    	SqlSession sqlSession = new MyBatisUtil().getSession();
        
    	try
        {
        	ClientesMapper clienteMapper = sqlSession.getMapper(ClientesMapper.class);
        	clienteMapper.insertCliente(cliente);
            
            sqlSession.commit();
        } finally
        {
            sqlSession.close();
        }
        clienteEventSrc.fire(cliente);
    }
    
    public void update(Clientes cliente) throws Exception{
    	cliente.setFechaActualizacion(new Date());
    	
    	log.info("Se actualizo cliente " + cliente.getIdCliente());
    	SqlSession sqlSession = new MyBatisUtil().getSession();
        try
        {
        	ClientesMapper clienteMapper = sqlSession.getMapper(ClientesMapper.class);
        	clienteMapper.updateCliente(cliente);
            sqlSession.commit();
        } finally
        {
            sqlSession.close();
        }
        clienteEventSrc.fire(cliente);
    }
    
    public String remove(Clientes cliente) throws Exception{
    	if(checkFk(cliente)){
    		SqlSession sqlSession = new MyBatisUtil().getSession();
            try
            {
            	ClientesMapper clienteMapper = sqlSession.getMapper(ClientesMapper.class);
            	clienteMapper.deleteCliente(cliente.getIdCliente());
                sqlSession.commit();
            } finally
            {
                sqlSession.close();
            }
            clienteEventSrc.fire(cliente);
            return "removed";
    	}else{
    		throw new ValidationException("Cliente en uso");
    	}
    }
    
    public Boolean checkFk(Clientes cliente){
    	Integer l=-1;
    	SqlSession sqlSession = new MyBatisUtil().getSession();
        try
        {
        	ClientesMapper clienteMapper = sqlSession.getMapper(ClientesMapper.class);
        	l = clienteMapper.checkFkCliente(cliente);
            
        } finally
        {
            sqlSession.close();
        }
    	if(l>0){
    		return false;
    	}else{
    		return true;
    	}
    
    }
}
