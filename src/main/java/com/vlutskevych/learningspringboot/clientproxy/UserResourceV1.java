package com.vlutskevych.learningspringboot.clientproxy;

import com.vlutskevych.learningspringboot.model.User;

import javax.ws.rs.*;
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
    User fetchUser(@PathParam("userUid") UUID userUid);

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    void insertNewUser(User user);

    @PUT
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    void updateUser(User user);

    @DELETE
    @Produces(APPLICATION_JSON)
    @Path("{userUid}")
    void deleteUser(@PathParam("userUid") UUID userUid);
}
