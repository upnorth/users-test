package org.example.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import org.example.service.UserService;

@ApplicationScoped
public class AppHealthCheck {

    @Inject
    UserService userService;

    @Liveness
    public HealthCheck checkLiveness() {
        return () -> HealthCheckResponse.named("user-api-liveness")
                .up()
                .withData("status", "UP")
                .build();
    }

    @Readiness
    public HealthCheck checkReadiness() {
        return () -> HealthCheckResponse.named("user-api-readiness")
                .up()
                .withData("status", "UP")
                .withData("usersCount", userService.count())
                .build();
    }
}
