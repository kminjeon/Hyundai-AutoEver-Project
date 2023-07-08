package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.model.dto.LoveDto;
import hyundaiautoever.library.model.dto.LoveDto.GetLoveDto;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Love;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.LoveRepository;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class LoveService {

    private final LoveRepository loveRepository;
    public final UserRepository userRepository;
    public final BookRepository bookRepository;

    /**
     * 좋아요 생성
     * @param personalId
     * @param bookId
     * @return loveId
     */
    @Transactional
    public Long createLove(String personalId, Long bookId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("createLove Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("createLove Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        Love love = Love.builder()
                    .book(book)
                    .user(user)
                    .build();

        // 좋아요가 생성될 때 해당 도서 loveCount +1
        book.updateLoveCount(book.getLoveCount() + 1);

        // Love 저장
        try {
            loveRepository.save(love);
        } catch (Exception e) {
            log.error("createLove Exception : {}", e.getMessage());
            throw new LibraryException.DataSaveException(ExceptionCode.DATA_SAVE_EXCEPTION);
        }
        return love.getId();
    }


    /**
     * 좋아요 리스트 조회
     * @param personalId
     * @return List<GetLoveDto>
     */
    public List<GetLoveDto> getLoveList(String personalId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getLoveList Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        return loveRepository.findByUser(user).stream().map(love -> new GetLoveDto(love.getBook())).collect(Collectors.toList());
    }

    /**
     * 좋아요 삭제
     * @param loveId
     */
    @Transactional
    public void deleteLove(Long loveId) {
        Love love = loveRepository.findById(loveId).orElseThrow(() -> {
            log.error("deleteLove Exception : [존재하지 않는 Love ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 좋아요가 삭제될 때 해당 도서 loveCount -1
        love.getBook().updateLoveCount(love.getBook().getLoveCount() - 1);

        // 좋아요 삭제
        try {
            loveRepository.deleteById(loveId);
        } catch (Exception e) {
            log.error("deleteLove Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }

    }
}
