package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.dto.RentDto;
import hyundaiautoever.library.model.dto.RentDto.*;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Rent;
import hyundaiautoever.library.model.entity.Reserve;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.RentRepository;
import hyundaiautoever.library.repository.ReserveRepository;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class RentService {

    public final RentRepository rentRepository;
    public final UserRepository userRepository;
    public final BookRepository bookRepository;
    public final ReserveRepository reserveRepository;

    public final ReserveService reserveService;

    /**
     * 도서 대여 생성
     * @param personalId
     * @param bookId
     * @return rentId
     */
    @Transactional
    public Long createRent(String personalId, Long bookId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("createRent Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("createRent Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        Rent rent = Rent.builder()
                    .book(book)
                    .user(user)
                    .build();

        // Rent 저장
        try {
            rentRepository.save(rent);
        } catch (Exception e) {
            log.error("createRent Exception : {}", e.getMessage());
            throw new LibraryException.DataSaveException(ExceptionCode.DATA_SAVE_EXCEPTION);
        }

        // book 대출 불가 상태로 update
        book.updateRentType(RentType.CLOSE);
        book.updateRentCount(book.getRentCount() + 1);

        return rent.getId();
    }

    /**
     * 사용자 대여 페이지
     * @param pageable
     * @param personalId
     * @return GetRentPage
     */
    public GetRentPage getRentPage(Pageable pageable, String personalId) {
        // 대여일 순으로 정렬하기 !
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getRentPage Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        return RentDto.buildGetRentPage(rentRepository.findGetRentDtoByUser(pageable, user));
    }

    /**
     * 대여 기록 페이지
     * @param pageable
     * @param personalId
     * @return GetRentHistoryPage
     */
    public GetRentHistoryPage getRentHistoryPage(Pageable pageable, String personalId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getRentHistoryPage Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        return RentDto.buildGetRentHistoryPage(rentRepository.findGetRentHistoryDtoByUser(pageable, user));
    }

    /**
     * 관리자 대여 페이지 (검색 가능)
     * @param pageable
     * @param personalId
     * @param name
     * @param bookId
     * @param title
     * @return GetAdminRentPage
     */
    public GetAdminRentPage getAdminRentPage(Pageable pageable, String personalId, String name, Long bookId, String title) {
        return RentDto.buildGetAdminRentPage(rentRepository.getAdminRentPage(pageable, personalId, name, bookId, title));
    }


    /**
     * 관리자 대여 기록 페이지 (검색 가능)
     * @param pageable
     * @param personalId
     * @param name
     * @param bookId
     * @param title
     * @return GetAdminRentPage
     */
    public GetAdminRentHistoryPage getAdminRentHistoryPage(Pageable pageable, String personalId, String name, Long bookId, String title) {
        return RentDto.buildGetAdminRentHistoryPage(rentRepository.getAdminRentHistoryPage(pageable, personalId, name, bookId, title));
    }

    /**
     * 대출 연장
     * @param rentId
     * @return ok
     */
    @Transactional
    public Response extentRent(Long rentId) {
        Rent rent = rentRepository.findById(rentId).orElseThrow(() -> {
            log.error("extentRent Exception : [존재하지 않는 Rent ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 도서 대여 연장 1회 사용 -> 연장 불가
        if (rent.getExtensionNumber().equals(1)) {
            return Response.extensionError(ExceptionCode.EXTENSION_ERROR);
        }

        // 도서 대여 가능할 경우 : 7일 연장
        rent.updateExpectedReturnDate(rent.getExpectedReturnDate().plusDays(7));

        // 도서 대여 연장 1회 추가
        rent.updateExtensionNumber(1);
        return Response.ok();
    }


    /**
     * 도서 반납
     * @param rentId
     */
    @Transactional
    public void returnBook(Long rentId) {
        Rent rent = rentRepository.findById(rentId).orElseThrow(() -> {
            log.error("returnBook Exception : [존재하지 않는 Rent ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 반납일 업데이트
        rent.updateReturnDate(LocalDateTime.now());

        // 도서 이용 가능 상태(RentType.OPEN)로 업데이트
        rent.getBook().updateRentType(RentType.OPEN);

        Reserve firstReserve = reserveRepository.findByWaitNumber(1);

        // 반납될 도서를 예약한 사람이 있는 경우
        if (firstReserve != null) {

            // 예약 첫 번째 사람 (자동 대여 -> 추가) -> 이메일로 전송하기 '대여되었습니다'
            createRent(firstReserve.getUser().getPersonalId(), rent.getBook().getId());

            // 예약 첫 번째 사람 (예약 -> 삭제)
            reserveService.deleteReserve(firstReserve.getId());

            // 반납된 도서를 예약한 사용자 리스트
            List<Reserve> reserveList = reserveRepository.findAllByBook(rent.getBook());

            // 예약 된 사람 대기 순번 -1
            reserveList.forEach(reserve -> reserve.updateWaitNumber(reserve.getWaitNumber() - 1));
        }
    }

}
