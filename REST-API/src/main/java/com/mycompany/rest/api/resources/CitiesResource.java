/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import DTOs.CityDTO;
import java.util.HashMap;
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
public class CitiesResource extends ResourceBase {
    
 
    
    
    //Retrieve all cities
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCities(){
        System.out.println("cities");
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "18");
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.ONE);

    }
    
    //Create a new city
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCity(CityDTO city){
        if(city == null) return Response.status(Response.Status.NO_CONTENT).entity("Please provide a json body").build();
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "1");
        return queryMessager.sendMessage(city, params, QueryMessager.Subsystem.ONE);
        
      }
    
    
}
