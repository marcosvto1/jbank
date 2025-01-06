package teamdev.tech.jbank.repositories.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StatementView {
    String getStatementId();

    String getStatementType();

    BigDecimal getStatementAmount();

    String getWalletReceiver();

    String getWalletSender();

    LocalDateTime getStatementDate();
}
