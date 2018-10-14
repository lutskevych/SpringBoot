package com.vlutskevych.learningspringboot.clientproxy;

import com.vlutskevych.learningspringboot.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public interface UserResourceV1 {

    @GET
    @Produces(APPLICATION_JSON)
    List<User> fetchUsers(@QueryParam("gender") String gender);

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{userUid}")
    Response fetchUser(@PathParam("userUid") UUID userUid);

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    Response insertNewUser(User user);

    @PUT
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    Response updateUser(User user);

    @DELETE
    @Produces(APPLICATION_JSON)
    @Path("{userUid}")
    Response deleteUser(@PathParam("userUid") UUID userUid);
}
