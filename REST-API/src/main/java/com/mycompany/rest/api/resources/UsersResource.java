/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import DTOs.UserDTO;
import entities.City;
import entities.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jmsMessaging.QueryMessager;

/**
 *
 * @author akith
 */

@Path("/users")
public class UsersResource extends ResourceBase {
     
    
    //Retrieve all users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "19");
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.ONE);
    }
    
    //Create new user
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(UserDTO user){
        if(user == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide json object to add").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "2");
        return queryMessager.sendMessage(user, params, QueryMessager.Subsystem.ONE);
    }
    
    //Update email and/or city of a user with id
    @Path("/{user_id}")
    @PUT
    public Response updateUser(@QueryParam("email") String email, @QueryParam("city") Integer city, @PathParam("user_id") String userId) {
        if(email == null && city == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide at least one query param to change").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);

        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "3");
        params.put("email", email);
        params.put("city", city.toString());
        params.put("user_id", userId);
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.ONE);
    }

    
    //Delegates to subscriptions subresource
    @Path("/{user_id}/subscriptions")
    public SubscriptionsResource getSubscriptionsResource(){
        return new SubscriptionsResource(connFactory, topic, queue);
    }
    
    //Delegates to listenings subresource
    @Path("/{user_id}/listenings")
    public ListeningsResource getListeningsResource(){
        return new ListeningsResource(connFactory, topic, queue);
    }

    
    //Delegates to favorites subresource
    @Path("/{user_id}/favorites")
    public FavoritesResource getFavoritesResource(){
        return new FavoritesResource(connFactory, topic, queue);
    }
    
}
