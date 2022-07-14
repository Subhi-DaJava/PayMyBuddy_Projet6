package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.dto.TransferBetweenBankAndPayMyBuddyDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Transfer;
import com.openclassrooms.pay_my_buddy.model.UserBankAccount;
import com.openclassrooms.pay_my_buddy.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
    private TransferRepository transferRepository;

    @Override
    public Transfer saveTransfer(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Override
    public List<TransferBetweenBankAndPayMyBuddyDTO> findAllTransfersByUser(AppUser appUser) {
        logger.debug("This findAllTransfersByUser(from TransferServiceImpl) method starts here");
        UserBankAccount userBankAccount = appUser.getUserBankAccount();
        List<Transfer> transfers = transferRepository.findByUserBankAccountOrderByTransactionDate(userBankAccount);
        String userName = appUser.getFirstName() + " " + appUser.getLastName();
        /*
            stream plus efficace plus rapide et plus propre
         */
        List<TransferBetweenBankAndPayMyBuddyDTO> allTransfersList = transfers.stream()
                .map(transfer -> new TransferBetweenBankAndPayMyBuddyDTO(
                        transfer.getUserBankAccount().getBankName(), userName, transfer.getAmount(), transfer.getDescription(),
                        transfer.getOperationType(), transfer.getTransactionDate()))
                .collect(Collectors.toList());
        return allTransfersList;
    }


}
