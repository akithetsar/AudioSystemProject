/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import DTOs.ListeningDTO;
import entities.Listening;
import entities.User;
import java.util.HashMap;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

public class ListeningsResource extends ResourceBase {

    
    public ListeningsResource(){
        
    }
    
    public ListeningsResource(ConnectionFactory connFactory, Topic topic, Queue queue) {
        super.connFactory = connFactory;
        super.topic = topic;
        super.queue = queue;
    }
    
    
    
    //Retrieve all listenings for user
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(@PathParam("track_id") Integer trackId){
        if(trackId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("track_id not provided").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "25");
        params.put("track_id", trackId.toString());
        System.out.println("Listenings for user: " + trackId);
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.THREE);
    }
    
    
    //Creates new track listening for user
    @Path("/{track_id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrack(ListeningDTO listening, @PathParam("user_id") Integer userId, @PathParam("track_id") Integer trackId){
        if(userId == null || trackId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("track_id or user_id not provided").build();
        }
        if(listening == null) return Response.status(Response.Status.NO_CONTENT).entity("Please provide a json body").build();
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "12");
        params.put("user_id", userId.toString());
        params.put("track_id", trackId.toString());
        System.out.println("listenings");
        return queryMessager.sendMessage(listening, params, QueryMessager.Subsystem.THREE);
}

}