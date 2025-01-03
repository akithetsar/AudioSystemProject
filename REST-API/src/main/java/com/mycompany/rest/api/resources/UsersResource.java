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

@Path("/users")
public class UsersResource {
     
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(){
        return Response.status(Response.Status.CREATED).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user){
        return Response.status(Response.Status.CREATED).build();
    }
    
    
    @Path("/{user_id}")
    @PUT()
    public Response updateUser(@QueryParam("email") String email, @QueryParam("city") String city, @PathParam("user_id") String userId){
        if(email==null){
            System.out.println("Null");
        }
        System.out.println("Email: " + email);
        System.out.println("City: " + city);
        return Response.status(Response.Status.CREATED).entity(email).build();
    }
    
}
