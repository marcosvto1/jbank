package teamdev.tech.jbank.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdev.tech.jbank.controllers.dtos.TransferAmountDto;
import teamdev.tech.jbank.entities.Transfer;
import teamdev.tech.jbank.services.TransferService;

import java.net.URI;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferAmountDto dto) {
        var transfer = transferService.transfer(dto);
        return ResponseEntity.created(URI.create("/transfers/" + transfer.getId())).build();
    }
}
