package com.openclassrooms.pay_my_buddy.service;

import com.openclassrooms.pay_my_buddy.dto.TransferBetweenBankAndPayMyBuddyDTO;
import com.openclassrooms.pay_my_buddy.model.AppUser;
import com.openclassrooms.pay_my_buddy.model.Transfer;

import java.util.List;

public interface TransferService {

    Transfer saveTransfer(Transfer transfer);

    List<TransferBetweenBankAndPayMyBuddyDTO> findAllTransfersByUser(AppUser appUser);

}
