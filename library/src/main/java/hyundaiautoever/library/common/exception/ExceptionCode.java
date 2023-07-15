package hyundaiautoever.library.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    SUCCESS(HttpStatus.OK.value(), "성공"),
    DATA_DUPLICATE_EXCEPTION(HttpStatus.CONFLICT.value(), "중복된 데이터가 있습니다."), // code : 409
    DATA_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND.value(), "요정하신 데이터가 없습니다."),
    DATA_UPDATE_EXCEPTION(HttpStatus.CONFLICT.value(), "데이터 수정에 실패하였습니다."),
    DATA_DELETE_EXCEPTION(HttpStatus.CONFLICT.value(), "데이터 삭제에 실패하였습니다."),
    DATA_OUT_OF_BOUNDS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터 범위가 초과하였습니다."),
    ENUM_NULL_POINTER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "입력하신 enum이 null입니다."),
    DATA_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "데이터 유효성 오류가 발생하였습니다."),
    NULL_POINTER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "알 수 없는 에러가 발생하였습니다."),
    DATA_SAVE_EXCEPTION(302, "데이터 저장에 실패하였습니다."),
    REFERENTIAL_EXCEPTION(307, "참조하는 데이터가 있습니다."),
    RENT_STATE_ERROR(-1, "대여 가능한 상태가 아닙니다"),
    PASSWORD_ERROR(-1, "비밀번호가 틀렸습니다"),
    PERSONALID_ERROR(-2, "로그인 아이디가 틀렸습니다"),
    LOGIN_SUCCESS(0, "로그인이 성공했습니다"),
    EXTENSION_ERROR(-1, "대여 연장 횟수를 초과했습니다"),

    DELETE_RENT_ERROR(304, "대여 중인 도서가 있습니다."),
    AUTH_ERROR(303, "권한이 없습니다.");

    private final int code;
    private final String message;
}

