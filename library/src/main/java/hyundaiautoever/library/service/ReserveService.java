package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.model.dto.request.ReserveRequest;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Reserve;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.ReserveRepository;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    /**
     * 도서 예약 생성
     * @param personalId
     * @param bookId
     * @return reserveId
     */
    @Transactional
    public Long createReserve(String personalId, Long bookId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("createReserve Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("createReserve Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });


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

        return reserve.getId();
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
