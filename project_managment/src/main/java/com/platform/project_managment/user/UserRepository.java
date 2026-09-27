package com.platform.project_managment.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByNameIn(List<String> usernames);

    List<User> findByEmailIn(List<String> userEmails);

    Optional<User> findByEmail(String email);
    List<User> findAllById(Iterable<Integer> ids);


    List<User> findAllByEmailIn(List<String> members);

}