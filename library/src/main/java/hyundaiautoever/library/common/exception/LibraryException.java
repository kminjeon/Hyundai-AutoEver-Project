package hyundaiautoever.library.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class LibraryException {
    @Getter
    @AllArgsConstructor
    public static class DataDuplicateException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
    public static class AuthException extends RuntimeException {
        private final ExceptionCode exceptionCode;
    }

    @Getter
    @AllArgsConstructor
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

    @Getter
    @AllArgsConstructor
    public static class passwordErrorException extends RuntimeException {
        private ExceptionCode exceptionCode;
    }
}
