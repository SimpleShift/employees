package com.simpleshift.app.employees;

import com.kumuluz.ee.common.runtime.EeRuntime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("demo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DemoResource {
    @Inject
    private AppProperties appProperties;
    @GET
    @Path("instanceid")
    public Response getInstanceId() {
        String instanceId =
                "{\"instanceId\" : \"" + EeRuntime.getInstance().getInstanceId() + "\"}";
        return Response.ok(instanceId).build();
    }
    @POST
    @Path("healthy")
    public Response setHealth(HealthDto health) {
        appProperties.setHealthy(health.getHealthy());
        return Response.ok().build();
    }
    @POST
    @Path("load")
    public Response loadOrder(LoadDto load) {
        for (int i = 1; i <= load.getN(); i++) {
            fibonacci(i);
        }
        return Response.status(Response.Status.OK).build();
    }

    private long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2);
    }
}