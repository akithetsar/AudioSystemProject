/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import DTOs.AudiotrackDTO;
import DTOs.CategoryDTO;
import entities.Audiotrack;
import entities.Category;
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
@Path("/tracks")
public class TracksResource extends ResourceBase {
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("columns", "*");
        params.put("tables", "audiotrack");
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.TWO);
    }
    
    //Create new audio track
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrack(AudiotrackDTO track){
        if(track == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide json object to add").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "6");
        return queryMessager.sendMessage(track, params, QueryMessager.Subsystem.TWO);
    }
    
    
    //Update the name of an audio track
    @Path("/{track_id}")
    @PUT()
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTrack(@QueryParam("name") String name, @PathParam("track_id") String trackId){
        if(name == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide at least one query param to change").build();
        }
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);

        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "7");
        params.put("name", name);
        params.put("track_id", trackId);
      
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.TWO);
    }
    
    //Add category to track
    @Path("/{track_id}/category")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST()
    public Response addCategoryToTrack(@PathParam("track_id") String trackId, CategoryDTO category){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "8");
        return queryMessager.sendMessage(category, params, QueryMessager.Subsystem.TWO);

    }
    
    @Path("/{track_id}/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrackCategories(@PathParam("track_id") String trackId){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("columns", "*");
        params.put("tables", "audiotrack");
        params.put("where", "track_id=" + trackId);
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.TWO);
    }
}
