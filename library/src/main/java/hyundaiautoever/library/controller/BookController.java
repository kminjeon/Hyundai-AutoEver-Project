package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;


    /**
     * 도서 추가
     * @param request
     * @return book_id
     */
    @PostMapping("/book/add")
    public Response addBook(@RequestBody @Valid BookRequest.AddBookRequest request) {
        return Response.ok().setData(bookService.addBook(request));
    }

    /**
     * 도서 수정
     * @param bookId
     * @param request
     * @return
     */
    @PutMapping("/book/update/{bookId}")
    public Response updateBook(@PathVariable("bookId") Long bookId, @RequestBody @Valid BookRequest.updateBookRequest request) {
        return Response.ok().setData(bookService.updateBook(bookId, request));
    }

    @DeleteMapping("/admin/book")
    public Response deleteBook(@RequestParam("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return Response.ok();
    }
}
