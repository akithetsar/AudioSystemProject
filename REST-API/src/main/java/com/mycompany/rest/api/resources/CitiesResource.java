/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import entities.City;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jmsMessaging.QueryMessager;

/**
 *
 * @author akith
 */
@Path("/cities")
public class CitiesResource {
    
 
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connFactory;
    
    @Resource(lookup="subsystemOneQueue")
    private Queue queue;
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCities(){
        
        QueryMessager queryMessager = new QueryMessager(connFactory, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("columns", "*");
        params.put("tables", "city");
        return queryMessager.sendMessage(null, params, QueryMessager.Operation.READ, QueryMessager.Subsystem.ONE);

   
  
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCity(City city){
        QueryMessager queryMessager = new QueryMessager(connFactory, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("tables", "city");
        return queryMessager.sendMessage(city, params, QueryMessager.Operation.READ, QueryMessager.Subsystem.ONE);
         
        
    }
    
    
}
