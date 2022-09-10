package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
