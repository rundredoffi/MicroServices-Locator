package com.micros.msuser.repository;

import org.springframework.data.repository.CrudRepository;
import com.micros.msuser.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}