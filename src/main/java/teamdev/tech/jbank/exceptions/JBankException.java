package teamdev.tech.jbank.exceptions;

import org.springframework.http.ProblemDetail;

public abstract class JBankException extends RuntimeException {
    public JBankException(String message) {
        super(message);
    }

    public JBankException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(500);

        pd.setTitle("JBank Internal Server Error");
        pd.setDetail("Contact JBank Team");

        return pd;
    }
}
