/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import DTOs.PackageDTO;
import entities.Package;
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
@Path("/Packages")
public class PackagesResource extends ResourceBase {
    
    //Retrieve all packages
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "23");
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.THREE);
    }
    
    //Create new package
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrack(PackageDTO pack){
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);
        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "9");
        return queryMessager.sendMessage(pack, params, QueryMessager.Subsystem.THREE);
    }
    
    
    //Update monthly price for package
    @Path("/{package_id}")
    @PUT()
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTrack(@QueryParam("price") Double price, @PathParam("package_id") String packageId){
        if(price == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide at least one query param to change").build();
        }
        
        QueryMessager queryMessager = new QueryMessager(connFactory, topic, queue);

        HashMap<String, String> params = new HashMap<>();
        params.put("operation", "10");
        params.put("price", price.toString());
        params.put("package_id", packageId);
        
        return queryMessager.sendMessage(null, params, QueryMessager.Subsystem.THREE);
    }
}
