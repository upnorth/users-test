package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.User;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, String> {
}
