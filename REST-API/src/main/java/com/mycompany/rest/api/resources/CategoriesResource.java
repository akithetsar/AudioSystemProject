/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import DTOs.CategoryDTO;
import entities.Category;
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

@Path("/categories")
public class CategoriesResource extends ResourceBase{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories(){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("columns", "*");
        params.put("tables", "category");
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.TWO);
    }
    
    
    //Create new category
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCategory(CategoryDTO category){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "5");
        return queryMessager.sendMessage(category, params, QueryMessager.Subsystem.TWO);
    }
}
