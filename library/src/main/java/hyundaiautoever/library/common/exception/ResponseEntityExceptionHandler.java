package hyundaiautoever.library.common.exception;

import hyundaiautoever.library.model.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseEntityExceptionHandler {
    @ExceptionHandler(LibraryException.DataDuplicateException.class)
    public ResponseEntity<Response<Object>> handleDataDuplicateException(LibraryException.DataDuplicateException dataDuplicateException) {
        return new ResponseEntity<>(Response.dataDuplicateException(dataDuplicateException.getExceptionCode()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LibraryException.DataNotFoundException.class)
    public ResponseEntity<Response<Object>> handleDataNotFoundException(LibraryException.DataNotFoundException dataNotFoundException) {
        return new ResponseEntity<>(Response.dataNotFoundException(dataNotFoundException.getExceptionCode()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LibraryException.MaxRentException.class)
    public ResponseEntity<Response<Object>> handleMaxRentException(LibraryException.MaxRentException maxRentException) {
        return new ResponseEntity<>(Response.maxRentException(maxRentException.getExceptionCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibraryException.DataUpdateException.class)
    public ResponseEntity<Response<Object>> handleDataUpdateException(LibraryException.DataUpdateException dataUpdateException) {
        return new ResponseEntity<>(Response.dataUpdateException(dataUpdateException.getExceptionCode()), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(LibraryException.AlreadyRentException.class)
    public ResponseEntity<Response<Object>> handleDataUpdateException(LibraryException.AlreadyRentException alreadyRentException) {
        return new ResponseEntity<>(Response.alreadyRentException(alreadyRentException.getExceptionCode()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LibraryException.FailSendEmailException.class)
    public ResponseEntity<Response<Object>> handleDataUpdateException(LibraryException.FailSendEmailException failSendEmailException) {
        return new ResponseEntity<>(Response.alreadyRentException(failSendEmailException.getExceptionCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibraryException.RentStateException.class)
    public ResponseEntity<Response<Object>> handleRentStateException(LibraryException.RentStateException rentStateException) {
        return new ResponseEntity<>(Response.rentStateException(rentStateException.getExceptionCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibraryException.AuthException.class)
    public ResponseEntity<Response<Object>> handleAuthException(LibraryException.AuthException authException) {
        return new ResponseEntity<>(Response.authException(authException.getExceptionCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibraryException.DataDeleteException.class)
    public ResponseEntity<Response<Object>> handleDataDeleteException(LibraryException.DataDeleteException dataDeleteException) {
        return new ResponseEntity<>(Response.dataDeleteException(dataDeleteException.getExceptionCode()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LibraryException.DataOutOfBoundsException.class)
    public ResponseEntity<Response<Object>> handleDataOutOfBoundsException(LibraryException.DataOutOfBoundsException dataOutOfBoundsException) {
        return new ResponseEntity<>(Response.dataOutOfBoundsException(dataOutOfBoundsException.getExceptionCode()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LibraryException.EnumNullPointerException.class)
    public ResponseEntity<Response<Object>> handleEnumNullPointerException(LibraryException.EnumNullPointerException enumNullPointerException) {
        return new ResponseEntity<>(Response.enumNullPointerException(enumNullPointerException.getExceptionCode()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Response<Object>> handleHttpMessageNotReadableException() {
        return new ResponseEntity<>(Response.dataValidationException(ExceptionCode.DATA_VALIDATION_EXCEPTION), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Response<Object>> handleNullPointerException() {
        return new ResponseEntity<>(Response.nullPointerException(ExceptionCode.NULL_POINTER_EXCEPTION), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LibraryException.DataSaveException.class)
    public ResponseEntity<Response<Object>> handleDataSaveException(LibraryException.DataSaveException dataSaveException) {
        return new ResponseEntity<>(Response.dataSaveException(dataSaveException.getExceptionCode()), HttpStatus.CONFLICT);
    }

}
