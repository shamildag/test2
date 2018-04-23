package com.demo.springtask.repo;

import com.demo.springtask.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByName(String username);

//	@Query("SELECT user from User user WHERE user.email = :email")
	User findUserByEmail(String email);
}
