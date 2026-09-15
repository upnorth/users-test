package org.example.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.example.service.UserService;

import java.util.Map;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Health API", description = "Application health status endpoint")
public class HealthAliasResource {

    @Inject
    UserService userService;

    @GET
    @Operation(summary = "Health check", description = "Returns the status of the service and in-memory store")
    public Response getHealth() {
        return Response.ok(Map.of(
                "status", "UP",
                "service", "user-api",
                "inMemoryUsersCount", userService.count()
        )).build();
    }
}
