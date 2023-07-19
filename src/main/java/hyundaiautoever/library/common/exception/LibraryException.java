package hyundaiautoever.library.common.exception;

import hyundaiautoever.library.model.dto.response.Response;
import jdk.jshell.spi.ExecutionControl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class LibraryException {
    @Getter
    @AllArgsConstructor
    public static class DataDuplicateException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }


    @Getter
    @AllArgsConstructor
    public static class WithdrawException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }
    @Getter
    @AllArgsConstructor
    public static class LoginPersonalIdException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginPasswordException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class AlreadyRentException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class RentExtensionException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class FailSendEmailException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }


    @Getter
    @AllArgsConstructor
    public static class RentStateException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class MaxRentException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class AlreadyExistBook extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class EmptyResultBook extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class AuthException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class DataNotFoundException extends RuntimeException {
        private ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class DataUpdateException extends RuntimeException {
        private ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class DataDeleteException extends RuntimeException {
        private ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class DataSaveException extends RuntimeException {
        private ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class DataOutOfBoundsException extends RuntimeException {
        private ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class EnumNullPointerException extends RuntimeException {
        private ExceptionCode exceptionCode;
    }
}
