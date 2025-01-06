package teamdev.tech.jbank.services;

import jakarta.persistence.Version;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import teamdev.tech.jbank.controllers.dtos.TransferAmountDto;
import teamdev.tech.jbank.entities.Transfer;
import teamdev.tech.jbank.entities.Wallet;
import teamdev.tech.jbank.exceptions.TransferException;
import teamdev.tech.jbank.exceptions.WalletNotFoundException;
import teamdev.tech.jbank.repositories.TransferRepository;
import teamdev.tech.jbank.repositories.WalletRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferService {
    private final WalletRepository walletRepository;
    private final TransferRepository transferRepository;

    public TransferService(WalletRepository walletRepository, TransferRepository transferRepository) {
        this.walletRepository = walletRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional()
    public Transfer transfer(TransferAmountDto dto) {
        var walletSender = walletRepository.findById(dto.sender()).orElseThrow(() -> new WalletNotFoundException("wallet sender not found"));
        var walletReceiver = walletRepository.findById(dto.receiver()).orElseThrow(() -> new WalletNotFoundException("wallet receiver not found"));

        if (walletSender.getBalance().compareTo(dto.amount()) < 0) {
            throw new TransferException("Insufficient balance to transfer from wallet");
        }

        updateWallets(dto, walletSender, walletReceiver);
        return persistTransfer(dto, walletSender, walletReceiver);
    }

    private void updateWallets(TransferAmountDto dto, Wallet walletSender, Wallet walletReceiver) {
        walletSender.setBalance(walletSender.getBalance().subtract(dto.amount()));
        walletReceiver.setBalance(walletReceiver.getBalance().add(dto.amount()));

        walletRepository.save(walletSender);
        walletRepository.save(walletReceiver);
    }

    private Transfer persistTransfer(TransferAmountDto dto, Wallet walletSender, Wallet walletReceiver) {
        var transfer = new Transfer();
        transfer.setSender(walletSender);
        transfer.setReceiver(walletReceiver);
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setAmount(dto.amount());
        transfer = transferRepository.save(transfer);
        return transfer;
    }
}
