package hyundaiautoever.library.controller;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.BookService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 도서 추가
     * @param request
     * @return book_id
     */
    @PostMapping("/admin/book/add")
    public Response addBook(@RequestBody @Valid BookRequest.AddBookRequest request) {
        log.info("BookController : [addBook]");
        return Response.ok().setData(bookService.addBook(request));
    }

    /**
     * 도서 수정
     * @param bookId
     * @param request
     * @return
     */
    @PutMapping("/admin/book/update/{bookId}")
    public Response updateBook(@PathVariable("bookId") Long bookId,
                               @RequestBody @Valid BookRequest.updateBookRequest request) {
        log.info("BookController : [updateBook]");
        return Response.ok().setData(bookService.updateBook(bookId, request));
    }

    /**
     * 관리자 도서 관리 페이지
     * @param pageable
     * @param bookId
     * @param categoryType
     * @param title
     * @return SearchAdminBookPage
     */
    @GetMapping("/admin/book/search")
    public Response searchAdminBookPage(@PageableDefault(size = 10) Pageable pageable,
                                   @RequestParam(required = false) Long bookId,
                                   @RequestParam(required = false) CategoryType categoryType,
                                   @RequestParam(required = false) String title,
                                        @RequestParam(required = false) String author) {
        log.info("BookController : [searchAdminBookPage]");
        return Response.ok().setData(bookService.searchAdminBookPage(pageable, bookId, categoryType, title, author));
    }


    /**
     * 도서 삭제
     * @param bookId
     * @return ok
     */
    @DeleteMapping("/admin/book/delete")
    public Response deleteBook(@RequestParam("bookId") Long bookId) {
        log.info("BookController : [deleteBook]");
        return bookService.deleteBook(bookId);
    }

    /**
     * 카테고리 도서 페이지 조회
     * @param pageable
     * @param categoryType
     * @return SimpleBookPage
     */
    @GetMapping("/book/category/{categoryType}")
    public Response getCategoryBookPage(@PageableDefault(size = 1) Pageable pageable,
                                        @PathVariable("categoryType") CategoryType categoryType,
                                        @RequestParam("personalId") String personalId) {
        log.info("BookController : [getCategoryBookPage]");
        return Response.ok().setData(bookService.getCategoryBookPage(pageable, categoryType, personalId));
    }

    /**
     * 베스트 10 도서
     * @return List<BestBookDto>
     */
    @GetMapping("/book/best")
    public Response getBestBookList(@RequestParam("personalId") String personalId) {
        log.info("BookController : [getBestBookList]");
        return Response.ok().setData(bookService.getBestBookList(personalId));
    }

    /**
     * 도서 상세 조회
     * @param personalId
     * @param bookId
     * @return GetBookDetailDto
     */
    @GetMapping("/book/detail/{bookId}")
    public Response getBookDetail(@RequestParam("personalId") String personalId,
                                  @PathVariable("bookId") Long bookId) {
        log.info("BookController : [getBookDetail]");
        return Response.ok().setData(bookService.getBookDetail(personalId, bookId));
    }

    /**
     * 도서 검색 (제목, 저자, 출판사)
     * @param pageable
     * @param searchWord
     * @return SimpleBookPage
     */
    @GetMapping("/book/search")
    public Response getSearchBookPage(@PageableDefault(size = 1) Pageable pageable,
                                      @RequestParam String searchWord,
                                      @RequestParam String personalId) {
        log.info("BookController : [getSearchBookPage]");
        return Response.ok().setData(bookService.getSearchBookPage(pageable, searchWord, personalId));
    }

}
