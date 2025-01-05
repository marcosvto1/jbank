package teamdev.tech.jbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdev.tech.jbank.entities.Deposit;

import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {
}
