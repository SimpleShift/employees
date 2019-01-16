package com.simpleshift.app.employees;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;
import java.io.StringReader;
import java.util.*;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("employees")
public class EmployeeResource {

    @Inject
    @DiscoverService("locations-service")
    private Optional<String> locationsUrl;

    @Inject
    private AppProperties properties;

    private Client httpClient;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }



    @Timed
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


    @POST
    @Path("logHours")
    public Response logHours(String s){

        JsonReader jsonReader = Json.createReader(new StringReader(s));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        String id = object.getJsonString("id").getString();

        String locationId = Database.getEmployee(id).getLocationId();

        if (properties.isExternalServicesEnabled() && locationsUrl.isPresent()) {
            try {
                String res = httpClient
                        .target(locationsUrl.get() + "/v1/locations/" + locationId + "/workingHours")
                        .request().get(new GenericType<String>() {
                        });

                jsonReader = Json.createReader(new StringReader(res));
                JsonObject hoursObjet = jsonReader.readObject();
                jsonReader.close();

                int openingHour = hoursObjet.getJsonNumber("openingHour").intValue();
                int closingHour = hoursObjet.getJsonNumber("closingHour").intValue();
                ArrayList<Integer> openDays = new ArrayList<>();

                Iterator it = hoursObjet.getJsonArray("openDays").iterator();
                while (it.hasNext()){
                    openDays.add(Integer.parseInt(it.next().toString()));
                }

                String start = object.getJsonString("start").getString();
                int startYear = Integer.parseInt(start.substring(0, 4));
                int startMonth = Integer.parseInt(start.substring(5, 7)) - 1;
                int startDay = Integer.parseInt(start.substring(8, 10));
                int startHour = Integer.parseInt(start.substring(11, 13));
                int startMinute = Integer.parseInt(start.substring(14, 16));
                Date startDate = new GregorianCalendar(startYear, startMonth, startDay, startHour, startMinute).getTime();

                String end = object.getJsonString("end").getString();
                int endYear = Integer.parseInt(end.substring(0, 4));
                int endMonth = Integer.parseInt(end.substring(5, 7)) - 1;
                int endDay = Integer.parseInt(end.substring(8, 10));
                int endHour = Integer.parseInt(end.substring(11, 13));
                int endMinute = Integer.parseInt(end.substring(14, 16));
                Date endDate = new GregorianCalendar(endYear, endMonth, endDay, endHour, endMinute).getTime();


                Date openingDate = new GregorianCalendar(startYear, startMonth, startDay, openingHour, 0).getTime();
                Date closingDate = new GregorianCalendar(startYear, startMonth, startDay, closingHour, 0).getTime();

                Calendar c = Calendar.getInstance();
                c.setTime(startDate);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
                if (dayOfWeek == 0) {
                    dayOfWeek = 7;
                }

                if (openDays.contains(dayOfWeek) && startDate.after(openingDate) & endDate.before(closingDate)){
                    ArrayList<Date> logData = new ArrayList<>();
                    logData.add(startDate);
                    logData.add(endDate);

                    Database.logTime(id, logData);

                    return Response.noContent().build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity("Cannot log outside of working hours!").build();
                }
            } catch (WebApplicationException | ProcessingException e) {
                throw new InternalServerErrorException(e);
            }
        }
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("External services must be enabled!").build();
    }
}