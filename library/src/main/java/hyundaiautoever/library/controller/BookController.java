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
                                   @RequestParam(required = false) String title) {
        log.info("BookController : [searchAdminBookPage]");
        return Response.ok().setData(bookService.searchAdminBookPage(pageable, bookId, categoryType, title));
    }


    /**
     * 도서 삭제
     * @param bookId
     * @return ok
     */
    @DeleteMapping("/admin/admin/book")
    public Response deleteBook(@RequestParam("bookId") Long bookId) {
        bookService.deleteBook(bookId);

        return Response.ok();
    }

    /**
     * 카테고리 도서 페이지 조회
     * @param pageable
     * @param categoryType
     * @return CategoryBookPage
     */
    @GetMapping("/book/category/{categoryType}")
    public Response getCategoryBookPage(@PageableDefault(size = 10) Pageable pageable,
                                        @PathVariable("categoryType") CategoryType categoryType) {
        log.info("BookController : [getCategoryBookPage]");
        return Response.ok().setData(bookService.getCategoryBookPage(pageable, categoryType));
    }


    /**
     * 도서 상세 조회
     * @param bookId
     * @return GetBookDetailDto
     */
    @GetMapping("/book/detail/{bookId}")
    public Response getBookDetail(@PathVariable("bookId") Long bookId) {
        log.info("BookController : [getBookDetail]");
        return Response.ok().setData(bookService.getBookDetail(bookId));
    }

    // 도서 검색
    @GetMapping("/book/search")
    public Response getSearchBookPage(@PageableDefault(size = 10) Pageable pageable,
                                      @RequestParam String searchWord) {
        log.info("BookController : [getSearchBookPage]");
        return Response.ok().setData(bookService.getSearchBookPage(pageable, searchWord));
    }

}
