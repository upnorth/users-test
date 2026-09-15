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
import org.jboss.logging.Logger;

import java.util.Map;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Health API", description = "Application health status endpoint")
public class HealthAliasResource {

    private static final Logger LOG = Logger.getLogger(HealthAliasResource.class);

    @Inject
    UserService userService;

    @GET
    @Operation(summary = "Health check", description = "Returns the status of the service and in-memory store")
    public Response getHealth() {
        int count = userService.count();
        LOG.debugf("Health alias endpoint invoked: status=UP, inMemoryUsersCount=%d", count);
        return Response.ok(Map.of(
                "status", "UP",
                "service", "user-api",
                "inMemoryUsersCount", count
        )).build();
    }
}
