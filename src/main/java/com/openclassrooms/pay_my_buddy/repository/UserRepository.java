package com.openclassrooms.pay_my_buddy.repository;

import com.openclassrooms.pay_my_buddy.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

    /*
    Find user by User's email
     */
    AppUser findUserByEmail(String email);

}
