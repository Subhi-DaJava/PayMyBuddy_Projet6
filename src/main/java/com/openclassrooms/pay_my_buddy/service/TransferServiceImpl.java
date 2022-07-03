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
import java.util.ArrayList;
import java.util.List;

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

        TransferBetweenBankAndPayMyBuddyDTO transferBetweenBankAndPayMyBuddyDTO;
        List<TransferBetweenBankAndPayMyBuddyDTO> allTransfersList = new ArrayList<>();

        UserBankAccount userBankAccount = appUser.getUserBankAccount();

        List<Transfer> transfers = transferRepository.findByUserBankAccountOrderByTransactionDate(userBankAccount);

        for(Transfer transfer : transfers){

            transferBetweenBankAndPayMyBuddyDTO = new TransferBetweenBankAndPayMyBuddyDTO();

            transferBetweenBankAndPayMyBuddyDTO.setSourceUserBanAccountName(userBankAccount.getBankName());
            transferBetweenBankAndPayMyBuddyDTO.setDestinationPMBUserName(appUser.getFirstName()
                    + " " + appUser.getLastName());
            transferBetweenBankAndPayMyBuddyDTO.setAmount(transfer.getAmount());
            transferBetweenBankAndPayMyBuddyDTO.setDescription(transfer.getDescription());
            transferBetweenBankAndPayMyBuddyDTO.setOperationType(transfer.getOperationType());
            transferBetweenBankAndPayMyBuddyDTO.setDateTransfer(transfer.getTransactionDate());

            allTransfersList.add(transferBetweenBankAndPayMyBuddyDTO);

        }

        return allTransfersList;
    }


}
