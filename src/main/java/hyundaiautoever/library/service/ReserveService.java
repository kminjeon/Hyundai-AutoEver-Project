package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.AuthType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.dto.ReserveDto;
import hyundaiautoever.library.model.dto.request.ReserveRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static hyundaiautoever.library.model.dto.ReserveDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    private final RentRepository rentRepository;

    /**
     * 도서 예약 생성
     * @param personalId
     * @param bookId
     * @return ok
     */
    @Transactional
    public Response createReserve(String personalId, Long bookId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("createReserve Exception : [존재하지 않는 personalId]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("createReserve Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        if (rentRepository.findByUserAndBook(user, book) != null) { // 대여 중인 도서
            throw new LibraryException.AlreadyRentException(ExceptionCode.ALREADY_RENT_ERROR);
        }

        if (reserveRepository.findByUserAndBook(user, book).isPresent()) { // 이미 예약 내역 존재
            throw new LibraryException.DataDuplicateException(ExceptionCode.DATA_DUPLICATE_EXCEPTION);
        }

        Reserve reserve = Reserve.builder()
                        .book(book)
                        .user(user)
                        .waitNumber(reserveRepository.findAllByBook(book).size() + 1)
                        .build();

        // 예약 저장
        try {
            reserveRepository.save(reserve);
        } catch (Exception e) {
            log.error("createReserve Exception : {}", e.getMessage());
            throw new LibraryException.DataSaveException(ExceptionCode.DATA_SAVE_EXCEPTION);
        }

        return Response.ok().setData(reserve.getId());
    }

    /**
     * 예약 페이지 조회
     * @param pageable
     * @param personalId
     * @return GetReservePage
     */
    public GetReservePage getReservePage(Pageable pageable, String personalId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getReservePage Exception : [존재하지 않는 personalId]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        return buildReservePage(reserveRepository.findPageByUser(pageable, user));
    }

    /**
     * 관리자 예약 페이지 조회
     * @param pageable
     * @param personalId
     * @return GetReservePage
     */
    public GetAdminReservePage getAdminReservePage(Pageable pageable, String personalId, String name, Long bookId, String title) {
        return buildAdminReservePage(reserveRepository.getAdminReservePage(pageable, personalId, name, bookId, title));
    }

    @Transactional
    public void reserveRent(Long reserveId) {
        Reserve reserve = reserveRepository.findById(reserveId).orElseThrow(() -> {
            log.error("reserveRent Exception : [존재하지 않는 reserveId]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        User user = reserve.getUser();

        // 대여 횟수 최대 3회
        if (user.getRentCount() >= 3) {
            throw new LibraryException.MaxRentException(ExceptionCode.MAX_RENT_EXCEPTION);
        }

        Book book = reserve.getBook();

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

        // 책 대여 횟수 + 1
        book.updateRentCount(book.getRentCount() + 1);

        // user 현재 대여 횟수 + 1
        user.updateUserRentCount(user.getRentCount() + 1);

        // 예약 삭제
        deleteReserveForRent(reserve);
    }


    public void deleteReserveForRent(Reserve reserve) {
        // 대기 순번 뒷 번호 사용자 대기 순번 올리기
        List<Reserve> reserveList = reserveRepository.findReserveListByWaitNumber(reserve.getWaitNumber());

        if (!reserveList.isEmpty()) {
            reserveList.forEach(next -> {
                next.updateWaitNumber(next.getWaitNumber() - 1);
            });
        }

        // 예약 삭제
        try {
            reserveRepository.deleteById(reserve.getId());
        } catch (Exception e) {
            log.error("deleteReserve Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }
    }

    /**
     * 도서 예약 삭제
     * @param reserveId
     */
    @Transactional
    public void deleteReserve(Long reserveId) {
        Reserve reserve = reserveRepository.findById(reserveId).orElseThrow(() -> {
            log.error("deleteReserve Exception : [존재하지 않는 Reserve ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 대기 순번 뒷 번호 사용자 대기 순번 올리기
        List<Reserve> reserveList = reserveRepository.findReserveListByWaitNumber(reserve.getWaitNumber());

        if (!reserveList.isEmpty()) {
            reserveList.forEach(next -> {
                next.updateWaitNumber(next.getWaitNumber() - 1);
            });
        }

        // 예약 삭제
        try {
            reserveRepository.deleteById(reserveId);
        } catch (Exception e) {
            log.error("deleteReserve Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }
    }
}
