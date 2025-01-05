package teamdev.tech.jbank.services;

import org.springframework.stereotype.Service;
import teamdev.tech.jbank.controllers.dtos.CreateWalletDto;
import teamdev.tech.jbank.entities.Wallet;
import teamdev.tech.jbank.exceptions.DeleteWalletException;
import teamdev.tech.jbank.exceptions.WalletDataAlreadyExistsException;
import teamdev.tech.jbank.repositories.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet create(CreateWalletDto createWalletDto) {
        var oWallet = walletRepository.findByCpfOrEmail(createWalletDto.cpf(), createWalletDto.email());
        if (oWallet.isPresent()) {
            throw new WalletDataAlreadyExistsException("cpf or email already exists");
        }

        Wallet wallet = new Wallet();
        wallet.setFullName(createWalletDto.name());
        wallet.setCpf(createWalletDto.cpf());
        wallet.setEmail(createWalletDto.email());
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }

    public Optional<Wallet> findById(UUID id) {
        return this.walletRepository.findById(id);
    }

    public boolean deleteWallet(UUID id) {
       var wallet = walletRepository.findById(id);

       if (wallet.isPresent()) {
           if (wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
               throw new DeleteWalletException("the balance is not zero, the current amount is $" + wallet.get().getBalance());
           }
           walletRepository.delete(wallet.get());
       }

       return wallet.isPresent();
    }
}
