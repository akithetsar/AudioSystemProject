/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import entities.Favorites;
import entities.User;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jmsMessaging.QueryMessager;

/**
 *
 * @author akith
 */
@Path("/favorites")
public class FavoritesResource extends ResourceBase {
    
    @Path("/{user_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(@PathParam("user_id") String userId){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("columns", "*");
        params.put("tables", "favorites");
        params.put("where", "user_id=" + userId);
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.THREE);
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrack(Favorites favorite){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("tables", "favorites");
        return queryMessager.sendMessage(favorite, params, QueryMessager.Subsystem.THREE);
    }
    
}