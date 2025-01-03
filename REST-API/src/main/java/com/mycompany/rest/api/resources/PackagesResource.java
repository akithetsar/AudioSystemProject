/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import entities.User;
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

/**
 *
 * @author akith
 */
@Path("/Packages")
public class PackagesResource {
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(){
        return Response.status(Response.Status.CREATED).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrack(User user){
        return Response.status(Response.Status.CREATED).build();
    }
    
    
    @Path("/{package_id}")
    @PUT()
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTrack(@QueryParam("price") Double price, @PathParam("package_id") String packageId){
        if(price==null){
            System.out.println("Null");
        }
        System.out.println("Name: " + price);
        
        return Response.status(Response.Status.CREATED).build();
    }
}
