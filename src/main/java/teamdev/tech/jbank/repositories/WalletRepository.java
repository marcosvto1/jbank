package teamdev.tech.jbank.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import teamdev.tech.jbank.entities.Wallet;
import teamdev.tech.jbank.repositories.dto.StatementView;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    String SQL_STATEMENT = """
            SELECT
                BIN_TO_UUID(id) as statement_id,
                "transfer" as statement_type,
                amount as statement_amount,
                BIN_TO_UUID(wallet_receiver_id) as wallet_receiver,
                BIN_TO_UUID(wallet_sender_id) as wallet_sender,
                transfer_date as statement_date
                FROM tb_transfers
                WHERE wallet_receiver_id = ?1 OR wallet_sender_id = ?1
            UNION ALL
            SELECT
                BIN_TO_UUID(id) as statement_id,
                "deposit" as statement_type,
                deposit_amount as statement_amount,
                BIN_TO_UUID(wallet_id) as wallet_receiver,
                "" as wallet_sender,
                deposit_date as statement_date
            FROM tb_deposits
            WHERE wallet_id = ?1
            """;

    String SQL_COUNT_STATEMENT = """
            SELECT COUNT(*) FROM
            (
            """ + SQL_STATEMENT + """
            ) as total
            """;

    Optional<Wallet> findByCpfOrEmail(String cpf, String email);

    @Query(nativeQuery = true, value = SQL_STATEMENT, countQuery = SQL_COUNT_STATEMENT)
    Page<StatementView> findStatements(UUID walletId, PageRequest pageRequest);
}
