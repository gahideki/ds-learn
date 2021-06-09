package com.devsuperior.bds02.repository;

import com.devsuperior.bds02.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String name);
    boolean existsUserByEmail(String email);

}
