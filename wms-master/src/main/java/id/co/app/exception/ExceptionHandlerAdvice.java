package id.co.app.exception;

import id.co.app.model.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(GeneralException.class)
    ResponseEntity<ErrorResponseDto> handleGeneralException(GeneralException generalException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(generalException.getError()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseDto> handleOtherException(Exception e) {
        log.error("Internal Server Error ", e);
        StackTraceElement[] stackTrace = e.getStackTrace();
        getErrorLocation(stackTrace);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(e.getMessage()));
    }

    private void getErrorLocation(StackTraceElement[] stackTrace) {
        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();
            if (className.startsWith("id.co.app")) {
                String errorLocation = String.format("%s.%s(%s:%d)",
                        element.getClassName(),
                        element.getMethodName(),
                        element.getFileName(),
                        element.getLineNumber());
                log.error(errorLocation);
            }
        }
    }
}
