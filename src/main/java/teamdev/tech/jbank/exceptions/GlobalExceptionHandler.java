package teamdev.tech.jbank.exceptions;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import teamdev.tech.jbank.exceptions.dto.InvalidParamDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JBankException.class)
    public ProblemDetail handleJBankException(JBankException ex) {
        return ex.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        var invalidParams = ex.getFieldErrors().stream()
                .map(err -> new InvalidParamDto(err.getField(), err.getDefaultMessage()))
                .toList();

        pd.setTitle("Invalid Parameters");
        pd.setDetail("There was an error processing the request");
        pd.setProperty("invalid-params", invalidParams);

        return pd;
    }
}
