package com.simpleshift.app.employees;

import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("employees")
public class EmployeeResource {

    @Inject
    @DiscoverService("locations-service")
    private Optional<String> baseUrl;

    @Inject
    private AppProperties properties;

    private Client httpClient;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }




    @GET
    public Response getAllEmployees(@DefaultValue("0") @QueryParam("locationId") String locationId) {

        List<Employee> employees;

        if (locationId.equals("0")){
            employees = Database.getEmployees();
        } else{
            employees = Database.getEmployeesFrom(locationId);
        }
        return Response.ok(employees).build();
    }

    @GET
    @Path("{employeeId}")
    public Response getEmployee(@PathParam("employeeId") String employeeId) {

            Employee e = Database.getEmployee(employeeId);
            return e != null
                    ? Response.ok(e).build()
                    : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response addNewEmployee(Employee e) {
        Database.addEmployee(e);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{employeeId}")
    public Response deleteEmployee(@PathParam("employeeId") String employeeId) {
        Database.deleteEmployee(employeeId);
        return Response.noContent().build();
    }
}