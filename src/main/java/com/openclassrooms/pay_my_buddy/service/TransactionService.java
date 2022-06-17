package com.openclassrooms.pay_my_buddy.service;

public interface TransactionService {

    void sendMoneyToBuddy(String userEmail, String contactEmail, double amount, String description);


}
