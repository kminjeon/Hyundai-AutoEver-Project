package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.dto.BookDto;
import hyundaiautoever.library.model.dto.BookDto.*;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Love;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static hyundaiautoever.library.model.dto.BookDto.*;
import static hyundaiautoever.library.model.dto.ReviewDto.CreateReviewDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final ReserveService reserveService;
    private final LoveRepository loveRepository;
    private final ReserveRepository reserveRepository;

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
     * 관리자 도서 관리 페이지
     * @param pageable
     * @param bookId
     * @param categorytype
     * @param title
     * @return SearchAdminBookPage
     */
    public SearchAdminBookPage searchAdminBookPage(Pageable pageable, Long bookId, CategoryType categorytype, String title, String author) {
        log.info("BookService : [searchApplyPage]");
        return buildSearchAdminBookPage(bookRepository.searchAdminBookPage(pageable, bookId, categorytype, title, author));
    }

    /**
     * 카테고리 도서 페이지 조회
     * @param pageable
     * @param categoryType
     * @return SimpleBookPage
     */
    public HeartAddBookPage getCategoryBookPage(Pageable pageable, CategoryType categoryType, String personalId) {
        log.info("BookService : [getCategoryBookPage]");
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getBestBookList Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        // 해당 카테고리 도서 리스트
        Page<Book> bookPage = bookRepository.findByCategoryType(pageable, categoryType);
        List<Book> bookList = bookPage.getContent();

        // Book 좋아요 정보 조회
        List<Love> loveList = loveRepository.findByUserAndBookIn(user, bookList);
        // key : BookId, value : 좋아요 Map 생성
        Map<Long, Long> loveMap = loveList.stream().collect(Collectors.toMap(love -> love.getBook().getId(), love -> love.getId()));

        // bookDtoList 생성
        List<BestBookDto> bookDtoList = new ArrayList<>();
        for (Book book : bookList) {
            Long loveId = loveMap.get(book.getId());
            bookDtoList.add(new BestBookDto(book, loveId));
        }
        return buildHeartAddBookPage(bookPage, bookDtoList);
    }

    /**
     * 베스트 10 도서
     * @param personalId
     * @return List<BestBookDto>
     */
    public List<BestBookDto> getBestBookList(String personalId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getBestBookList Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        // 도서 베스트 10 List
        List<Book> bookList = bookRepository.findTop10ByOrderByRentCountDesc();

        // Book 좋아요 정보 조회
        List<Love> loveList = loveRepository.findByUserAndBookIn(user, bookList);
        // key : BookId, value : 좋아요 Map 생성
        Map<Long, Long> loveMap = loveList.stream().collect(Collectors.toMap(love -> love.getBook().getId(), love -> love.getId()));

        // bookDtoList 생성
        List<BestBookDto> bookDtoList = new ArrayList<>();
        for (Book book : bookList) {
            Long loveId = loveMap.get(book.getId());
            bookDtoList.add(new BestBookDto(book, loveId));
        }

        return bookDtoList;
    }

    /**
     * 도서 상세 조회
     * @param personalId
     * @param bookId
     * @return GetBookDetailDto
     */
    public GetBookDetailDto getBookDetail(String personalId, Long bookId) {
        log.info("BookService : [getBookDetail]");
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("getBookDetail Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 현재 로그인 유저
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getBookDetail Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 해당 도서가 유저가 좋아요 누른 도서인지 확인
        Optional<Love> love = loveRepository.findByUserAndBook(user, book);

        // 해당 도서 리뷰 리스트
        List<CreateReviewDto> reviewList = reviewRepository.findByBook(book).stream().map(CreateReviewDto::new).collect(Collectors.toList());
        return BookDto.buildGetBookDetailDto(book, reviewList, love.isPresent() ? love.get().getId() : null);
    }

    /**
     * 관리자 도서 상세 조회
     * @param bookId
     * @return GetBookDetailDto
     */
    public GetAdminBookDetailDto getAdminBookDetail(Long bookId) {
        log.info("BookService : [getBookDetail]");
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("getBookDetail Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 해당 도서 리뷰 리스트
        List<CreateReviewDto> reviewList = reviewRepository.findByBook(book).stream().map(CreateReviewDto::new).collect(Collectors.toList());
        return BookDto.buildAdminGetBookDetailDto(book, reviewList);
    }


    /**
     * 도서 검색
     * @param pageable
     * @param searchWord
     * @return SimpleBookPage
     */
    public HeartAddBookPage getSearchBookPage(Pageable pageable, String searchWord, String personalId) {
        log.info("BookService : [getSearchBookPage]");

        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getBestBookList Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        Page<Book> bookPage = bookRepository.getSearchBookPage(pageable, searchWord);
        List<Book> bookList = bookPage.getContent();

        // Book 좋아요 정보 조회
        List<Love> loveList = loveRepository.findByUserAndBookIn(user, bookList);
        // key : BookId, value : 좋아요 Map 생성
        Map<Long, Long> loveMap = loveList.stream().collect(Collectors.toMap(love -> love.getBook().getId(), love -> love.getId()));

        // bookDtoList 생성
        List<BestBookDto> bookDtoList = new ArrayList<>();
        for (Book book : bookList) {
            Long loveId = loveMap.get(book.getId());
            bookDtoList.add(new BestBookDto(book, loveId));
        }
        return buildHeartAddBookPage(bookPage, bookDtoList);
    }

    /**
     * 도서 수정
     * @param request
     * @return updateBookDto
     */
    @Transactional
    public UpdateBookDto updateBook(Long bookId, BookRequest.updateBookRequest request) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("updateBook Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // request가 null이 아닌 경우에만 update
        book.updateTitle(request.getTitle() != null ? request.getTitle() : book.getTitle());
        book.updateAuthor(request.getAuthor() != null ? request.getAuthor() : book.getAuthor());
        book.updatePublisher(request.getPublisher() != null ? request.getPublisher() : book.getPublisher());
        book.updateCategoryType(request.getCategoryType() != null ? request.getCategoryType() : book.getCategoryType());
        book.updateIsbn(request.getIsbn() != null ? request.getIsbn() : book.getIsbn());
        book.updateDescription(request.getDescription() != null ? request.getDescription() : book.getDescription());
        return buildUpdateBookDto(book);
    }

    /**
     * 도서 수정 페이지 조회
     * @return updateBookDto
     */
    public UpdateBookDto getUpdateBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("getUpdateBook Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        return buildUpdateBookDto(book);
    }

    /**
     * 도서 삭제
     * @param bookId
     */
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("deleteBook Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        if (book.getRentType().equals(RentType.CLOSE)) {
            // 삭제할 도서가 대여 중인 경우
            throw new LibraryException.DeleteBookException(ExceptionCode.DELETE_BOOK_ERROR);
        }

        // 좋아요
        loveRepository.deleteAllByBook(book);
        // 대여
        rentRepository.deleteAllByBook(book);
        // 예약
        reserveRepository.deleteAllByBook(book);
        // 리뷰
        reviewRepository.deleteAllByBook(book);

        // 도서 삭제
        try {
            bookRepository.deleteById(bookId);
        } catch (Exception e) {
            log.error("deleteBook Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }
    }
}
