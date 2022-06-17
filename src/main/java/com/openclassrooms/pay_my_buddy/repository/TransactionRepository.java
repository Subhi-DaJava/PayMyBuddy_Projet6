package com.openclassrooms.pay_my_buddy.repository;

import com.openclassrooms.pay_my_buddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Page<Transaction> findAll(Pageable pageable);


}
