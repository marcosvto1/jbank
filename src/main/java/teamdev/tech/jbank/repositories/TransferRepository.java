package teamdev.tech.jbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdev.tech.jbank.entities.Transfer;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
