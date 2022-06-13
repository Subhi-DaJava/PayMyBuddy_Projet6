package com.openclassrooms.pay_my_buddy.controller;

import com.openclassrooms.pay_my_buddy.dto.TransactionDTO;

import com.openclassrooms.pay_my_buddy.model.Transaction;
import com.openclassrooms.pay_my_buddy.repository.TransactionRepository;
import com.openclassrooms.pay_my_buddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pay-my-buddy")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/transactions/send_money")
    public void sendMoneyToBuddy(@RequestParam int payedId, @RequestParam String userName, @RequestParam double amount, @RequestParam String description) {

        if (payedId <= 0 || userName == null || amount <= 0) {
            return;
        }
        transactionService.sendMoneyToBuddy(payedId, userName, amount, description);
    }





/*    @GetMapping("/transactions/user/{userId}")
    public String  getAllTransactionsByUser(Model model, @PathVariable("userId") Integer userId){

        List<TransactionDTO> transactionsByUser =
               transactionService.findAllTransactionByUser(userId);

        model.addAttribute("transactions", transactionsByUser);


        return "/transfer";
    }*/


    @GetMapping(path = "/listTransaction")
    public String allTransaction(Model model,
                                 @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                 @RequestParam(name = "size", defaultValue = "3", required = false) int size) {

        Page<Transaction> listTransaction = transactionRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("transactions", listTransaction.getContent());
        model.addAttribute("pages", new int[listTransaction.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "redirect:/transfer?page=" + page + "&size=" + size;

    }


}
