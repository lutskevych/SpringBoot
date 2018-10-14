package com.vlutskevych.learningspringboot.resource;

import com.vlutskevych.learningspringboot.model.User;
import com.vlutskevych.learningspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("api/v1/users")
public class UserResourceResteasy {

    private UserService userService;

    @Autowired
    public UserResourceResteasy(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> fetchUsers(@QueryParam("gender") String gender) {
        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{userUid}")
    public Response fetchUser(@PathParam("userUid") UUID userUid) {
        return userService.getUser(userUid).map(user -> Response.ok(user).build())
                .orElseGet(() -> Response
                        .status(Status.NOT_FOUND)
                        .entity(new ErrorMessage("User " + userUid + " was not found"))
                        .build());
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response insertNewUser(User user) {
        int result = userService.insertUser(user);
        return getIntegerResponseEntity(result);
    }

    @PUT
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response updateUser(User user) {
        int result = userService.updateUser(user);
        return getIntegerResponseEntity(result);
    }

    @DELETE
    @Produces(APPLICATION_JSON)
    @Path("{userUid}")
    public Response deleteUser(@PathParam("userUid") UUID userUid) {
        int result = userService.removeUser(userUid);
        return getIntegerResponseEntity(result);
    }

    private Response getIntegerResponseEntity(int result) {
        if (result == 1) {
            return Response.ok().build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }
}
