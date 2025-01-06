package teamdev.tech.jbank.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdev.tech.jbank.controllers.dtos.*;
import teamdev.tech.jbank.entities.Wallet;
import teamdev.tech.jbank.repositories.dto.StatementView;
import teamdev.tech.jbank.services.DepositService;
import teamdev.tech.jbank.services.WalletService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;
    private final DepositService depositService;

    public WalletController(WalletService walletService, DepositService depositService) {
        this.walletService = walletService;
        this.depositService = depositService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDto body) {
        var wallet = this.walletService.create(body);

        return ResponseEntity.created(URI.create("/wallets/" + wallet.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> get(@PathVariable UUID id) {
        var wallet = this.walletService.findById(id);

        return wallet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable UUID id) {
        var deleted = this.walletService.deleteWallet(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{walletId}/deposits")
    public ResponseEntity<Void> depositAmount(
            @PathVariable("walletId") UUID walletId,
            @RequestBody @Valid DepositAmountDto body,
            HttpServletRequest request
    ) {
        var deposit = this.depositService.depositAmount(
                walletId, body,
                request.getAttribute("x-user-ip").toString()
        );

        return ResponseEntity.created(URI.create("/wallets/" + walletId + "/deposits/" + deposit.getId())).build();
    }

    @GetMapping("/{walletId}/statements")
    public ResponseEntity<StatementDto> findStatements(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @PathVariable("walletId") UUID walletId
    ) {
        var statementDto =this.walletService.findAll(
                pageNumber, pageSize, walletId
        );

        return ResponseEntity.ok(
              statementDto
        );
    }
}
