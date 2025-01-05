/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import DTOs.PackageDTO;
import DTOs.SubscriptionDTO;
import entities.Subscription;
import entities.User;
import java.util.Date;
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


public class SubscriptionsResource extends ResourceBase {
    
    public SubscriptionsResource(){
        
    }
    
    public SubscriptionsResource(ConnectionFactory connFactory, Topic topic, Queue queue){
        super.connFactory = connFactory;
        super.topic = topic;
        super.queue = queue;
    }
    
    
    //Retrive all subscriptions of user
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(@PathParam("user_id") Integer userId){
        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User ID is missing").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "24");
        params.put("user_id", userId.toString());
        System.out.println("get subs for " + userId);
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.THREE);
    }
    
    
    //Creates new subscription for user
    @Path("/{package_id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrack(SubscriptionDTO sub, @PathParam("user_id") Integer userId, @PathParam("package_id") Integer packageId){
        
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "11");
        params.put("user_id", userId.toString());
        params.put("package_id", packageId.toString());
        System.out.println("subscriptions");
        return queryMessager.sendMessage(sub, params, QueryMessager.Subsystem.THREE);
    }
}
