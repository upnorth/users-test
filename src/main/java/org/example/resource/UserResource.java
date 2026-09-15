package org.example.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.example.model.User;
import org.example.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/digg/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User API", description = "Operations for managing users in the in-memory store")
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @Operation(summary = "List all users", description = "Retrieves a list of all users in the system")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "List of users retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = User.class))
            )
    })
    public List<User> listUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a single user by their unique identifier")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "User found and returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(example = "{\"error\":\"User not found\"}"))
            )
    })
    public Response getUserById(@Parameter(description = "User unique ID", required = true) @PathParam("id") String id) {
        return userService.getUserById(id)
                .map(user -> Response.ok(user).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "User not found with id: " + id))
                        .build());
    }

    @POST
    @Operation(summary = "Create a new user", description = "Creates a new user object with name, address, email, and telephone")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Validation failure or invalid input data"
            )
    })
    public Response createUser(
            @RequestBody(description = "User payload to create", required = true,
                    content = @Content(schema = @Schema(implementation = User.class)))
            @Valid User user) {
        User created = userService.addUser(user);
        return Response.created(URI.create("/digg/user/" + created.getId())).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing user", description = "Updates details of an existing user identified by ID")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = User.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Validation failure or invalid input data"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public Response updateUser(
            @Parameter(description = "User unique ID", required = true) @PathParam("id") String id,
            @RequestBody(description = "Updated user payload", required = true,
                    content = @Content(schema = @Schema(implementation = User.class)))
            @Valid User user) {
        return userService.updateUser(id, user)
                .map(updated -> Response.ok(updated).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "User not found with id: " + id))
                        .build());
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a user", description = "Removes a user from the system by ID")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    public Response deleteUser(@Parameter(description = "User unique ID", required = true) @PathParam("id") String id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "User not found with id: " + id))
                    .build();
        }
    }
}
