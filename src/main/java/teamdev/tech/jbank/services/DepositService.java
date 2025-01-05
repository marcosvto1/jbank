package teamdev.tech.jbank.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import teamdev.tech.jbank.controllers.dtos.DepositAmountDto;
import teamdev.tech.jbank.entities.Deposit;
import teamdev.tech.jbank.exceptions.WalletNotFoundException;
import teamdev.tech.jbank.repositories.DepositRepository;
import teamdev.tech.jbank.repositories.WalletRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DepositService {
    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public DepositService(WalletRepository walletRepository, DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
    }

    @Transactional
    public Deposit depositAmount(UUID walletId, DepositAmountDto dto, String ipAddress) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("There's no wallet with id " + walletId));

        var deposit = new Deposit();
        deposit.setWallet(wallet);
        deposit.setDepositAmount(dto.amount());
        deposit.setDepositDate(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);
        deposit = depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(deposit.getDepositAmount()));

        walletRepository.save(wallet);

        return deposit;
    }
}
