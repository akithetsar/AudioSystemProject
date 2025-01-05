/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import DTOs.FavoritesDTO;
import DTOs.RatingDTO;
import entities.User;
import java.util.HashMap;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

public class RatingsResource extends ResourceBase {

    public RatingsResource(){
        
    }
    
    public RatingsResource(ConnectionFactory connFactory, Topic topic, Queue queue) {
       super.connFactory = connFactory;
       super.topic = topic;
       super.queue = queue;
    }
    
    //Retrieve all ratings for track
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@PathParam("track_id") Integer trackId){
        if(trackId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("track_id not provided").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "26");
        params.put("track_id", trackId.toString());
        System.out.println("Ratings for track: " + trackId);
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.THREE);
    }
    
    //Creates new rating on track by user
    @Path("/{user_id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUserRating(FavoritesDTO favorite, @PathParam("user_id") Integer userId, @PathParam("track_id") Integer trackId){
        if(userId == null || trackId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("track_id or user_id not provided").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "14");
        params.put("user_id", userId.toString());
        params.put("track_id", userId.toString());
        return queryMessager.sendMessage(favorite, params, QueryMessager.Subsystem.THREE);
    }
    
    
    //Change users rating for track
    @Path("/{user_id}")
    @PUT
    public Response updateUserRating(RatingDTO rating, @PathParam("track_id") Integer trackId, @PathParam("user_id") Integer userId){
        if(userId == null || trackId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("track_id or user_id not provided").build();
        }
        if(rating == null) return Response.status(Response.Status.NO_CONTENT).entity("Please provide a json body").build();
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "15");
        params.put("user_id", userId.toString());
        params.put("track_id", trackId.toString());
        
        return queryMessager.sendMessage(rating, params, QueryMessager.Subsystem.THREE);
    }
    
    //Deletes users rating for track
    @Path("/{user_id}")
    @DELETE
    public Response deleteUserRating(@PathParam("track_id") Integer trackId, @PathParam("user_id") Integer userId){
        if(userId == null || trackId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("track_id or user_id not provided").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "16");
        params.put("user_id", userId.toString());
        params.put("track_id", trackId.toString());
        
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.THREE);
    }
}
