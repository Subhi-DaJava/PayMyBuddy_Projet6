package com.openclassrooms.pay_my_buddy.repository;


import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankAccountRepository extends JpaRepository<UserBankAccount, Integer> {

    UserBankAccount findByCodeIBAN(String codeIBAN);

    UserBankAccount findByAppUser(AppUser appUser);
}
