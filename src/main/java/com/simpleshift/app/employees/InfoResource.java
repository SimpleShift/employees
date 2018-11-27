package com.simpleshift.app.employees;

import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("info")
public class InfoResource {

    @GET
    public Response getInfo() {

        String jsonInfo = Json.createObjectBuilder()
                .add("clani", Json.createArrayBuilder()
                        .add("kr3037")
                        .build())
                .add("opis_projekta", "Končana aplikacija bo namenjena upravljanju z izmenami natakarjev v baru. Obsegala bo generiranje urnika na podlagi želja natakarjev, nadomeščanje izmen, izpisovanje statistike itd.")
                .add("mikrostoritve", Json.createArrayBuilder()
                        .add("http://159.122.187.27:32314/v1/employees")
                        .add("http://159.122.187.27:30780/v1/locations/")
                        .build())
                .add("github", Json.createArrayBuilder()
                        .add("https://github.com/SimpleShift/employees")
                        .add("https://github.com/SimpleShift/locations")
                        .build())

                .add("travis", Json.createArrayBuilder()
                        .add("https://travis-ci.org/SimpleShift/employees")
                        .add("https://travis-ci.org/SimpleShift/locations")
                        .build())

                .add("dockerhub", Json.createArrayBuilder()
                        .add("https://hub.docker.com/r/kr3037/simpleshift-employees/")
                        .add("https://hub.docker.com/r/kr3037/simpleshift-locations/")
                        .build())
                .build()
                .toString();


        return Response.ok(jsonInfo).build();
    }
}