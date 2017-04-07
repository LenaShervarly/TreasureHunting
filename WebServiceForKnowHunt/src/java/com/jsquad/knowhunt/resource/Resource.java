/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsquad.knowhunt.resource;

import DatabaseHelper.QaEJB;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author elena
 */
@ManagedBean
@Path("simple/{id}")
@RequestScoped
public class Resource {

    @Context
    private UriInfo context;
    
    @EJB QaEJB bean;
    /**
     * Creates a new instance of Resource
     */
    public Resource() {
    }

    /**
     * Retrieves representation of an instance of com.jsquad.knowhunt.resource.Resource
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    public String getText(@PathParam("id")int id) {
        return "Hello from JSQUAD to " + bean.getQuestion(id);
    }

    /**
     * PUT method for updating or creating an instance of Resource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public void putText(String content) {
    }
}
