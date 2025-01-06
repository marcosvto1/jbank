package teamdev.tech.jbank.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import teamdev.tech.jbank.controllers.dtos.*;
import teamdev.tech.jbank.entities.Wallet;
import teamdev.tech.jbank.enums.EStatementOperation;
import teamdev.tech.jbank.exceptions.DeleteWalletException;
import teamdev.tech.jbank.exceptions.StatementException;
import teamdev.tech.jbank.exceptions.WalletDataAlreadyExistsException;
import teamdev.tech.jbank.exceptions.WalletNotFoundException;
import teamdev.tech.jbank.repositories.WalletRepository;
import teamdev.tech.jbank.repositories.dto.StatementView;

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

    public StatementDto findAll(
            Integer pageNumber,
            Integer pageSize,
            UUID walletId
    ) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("wallet not found"));

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "statement_date");
        var statements = walletRepository.findStatements(walletId, pageRequest)
                .map(view -> mapToDto(walletId, view));

        return new StatementDto(
                new WalletDto(
                        wallet.getId(),
                        wallet.getCpf(),
                        wallet.getFullName(),
                        wallet.getEmail(),
                        wallet.getBalance()
                ),
                statements.getContent(),
                new ApiPagination(
                        statements.getSize(),
                        statements.getNumber(),
                        statements.getTotalElements(),
                        statements.getTotalPages()
                )
        );
    }

    private StatementItemDto mapToDto(UUID walletId, StatementView view) {

        if (view.getStatementType().equalsIgnoreCase("deposit")) {
            return mapToStatementDeposit(view);
        }

        if (view.getStatementType().equalsIgnoreCase("transfer")
                && view.getWalletSender().equalsIgnoreCase(walletId.toString())
        ) {
            return mapToStatementTransferWhenSender(walletId, view);
        }

        if (view.getStatementType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())
        ) {
            return mapToStatementTransferWhenReceiver(walletId, view);
        }

        throw new StatementException("Invalid statement type: " + view.getStatementType());
    }

    private static StatementItemDto mapToStatementTransferWhenSender(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getStatementType(),
                view.getStatementAmount(),
                "transfer amount to wallet: " + view.getWalletReceiver(),
                view.getStatementDate(),
               EStatementOperation.DEBIT
        );
    }

    private static StatementItemDto mapToStatementTransferWhenReceiver(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getStatementType(),
                view.getStatementAmount(),
                "received transfer amount from wallet: " + view.getWalletSender(),
                view.getStatementDate(),
                EStatementOperation.CREDIT
        );
    }

    private static StatementItemDto mapToStatementDeposit(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getStatementType(),
                view.getStatementAmount(),
                "money deposit",
                view.getStatementDate(),
                EStatementOperation.CREDIT
        );
    }
}
