package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.model.dto.BookDto;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회에서 성능 최적화
public class BookService {

    private final BookRepository bookRepository;

    /**
     * 도서 추가
     * @param request
     * @return 도서 ID
     */
    @Transactional // post
    public Long addBook(BookRequest.AddBookRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .isbn(request.getIsbn())
                .categoryType(request.getCategoryType())
                .description(request.getDescription())
                .img(request.getImg())
                .build();

        // Book Repository 저장
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            log.error("addBook Exception : {}", e.getMessage());
            throw new LibraryException.DataSaveException(ExceptionCode.DATA_SAVE_EXCEPTION);
        }
        return book.getId();
    }


    /**
     * 도서 수정
     * @param request
     * @return updateBookDto
     */
    @Transactional
    public BookDto.UpdateBookDto updateBook(Long bookId, BookRequest.updateBookRequest request) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("updateBook Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // request가 null이 아닌 경우에만 update
        book.updateTitle(request.getTitle() != null ? request.getTitle() : book.getTitle());
        book.updateAuthor(request.getAuthor() != null ? request.getAuthor() : book.getAuthor());
        book.updatePublisher(request.getPublisher() != null ? request.getPublisher() : book.getPublisher());
        book.updateCategoryType(request.getCategory() != null ? request.getCategory() : book.getCategoryType());
        book.updateIsbn(request.getIsbn() != null ? request.getIsbn() : book.getIsbn());
        return BookDto.buildUpdateBookDto(book);
    }


    /**
     * 도서 삭제
     * @param bookId
     */
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("deleteBook Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 도서 삭제
        try {
            bookRepository.deleteById(bookId);
        } catch (Exception e) {
            log.error("deleteBook Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }
    }

}
