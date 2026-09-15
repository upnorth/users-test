package org.example.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import org.example.service.UserService;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AppHealthCheck {

    private static final Logger LOG = Logger.getLogger(AppHealthCheck.class);

    @Inject
    UserService userService;

    @Liveness
    public HealthCheck checkLiveness() {
        return () -> {
            LOG.trace("MicroProfile liveness probe evaluated: status=UP");
            return HealthCheckResponse.named("user-api-liveness")
                    .up()
                    .withData("status", "UP")
                    .build();
        };
    }

    @Readiness
    public HealthCheck checkReadiness() {
        return () -> {
            int usersCount = userService.count();
            LOG.tracef("MicroProfile readiness probe evaluated: status=UP, usersCount=%d", usersCount);
            return HealthCheckResponse.named("user-api-readiness")
                    .up()
                    .withData("status", "UP")
                    .withData("usersCount", usersCount)
                    .build();
        };
    }
}
