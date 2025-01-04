/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import entities.Listening;
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
@Path("/listenings")
public class ListeningsResource extends ResourceBase {
    @Path("/{track_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(@PathParam("track_id") String userId){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("columns", "*");
        params.put("tables", "listening");
        params.put("where", "track_id=" + userId);
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.THREE);
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrack(Listening listening){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("tables", "listening");
        return queryMessager.sendMessage(listening, params, QueryMessager.Subsystem.THREE);
}

}