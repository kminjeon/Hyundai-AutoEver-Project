package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.RentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class RentController {
    @Autowired
    private RentService rentService;

    /**
     * 도서 대여 생성
     * @param personalId
     * @param bookId
     * @return rentId
     */
    @PostMapping("/rent/create")
    public Response createRent(@RequestParam("personalId") String personalId, @RequestParam("bookId") Long bookId) {
        return Response.ok().setData(rentService.createRent(personalId, bookId));
    }

    /**
     * 사용자 대여 페이지
     * @param pageable
     * @param personalId
     * @return GetRentPage
     */
    @GetMapping("/mypage/rent/getPage")
    public Response getRentPage(@PageableDefault(size = 10) Pageable pageable,
                                @RequestParam("personalId") String personalId) {
        return Response.ok().setData(rentService.getRentPage(pageable, personalId));
    }

    /**
     * 대여 기록 페이지
     * @param pageable
     * @param personalId
     * @return GetRentHistoryPage
     */
    @GetMapping("/mypage/rent/history")
    public Response getRentHistoryPage(@PageableDefault(size = 10) Pageable pageable,
                                       @RequestParam("personalId") String personalId) {
        return Response.ok().setData(rentService.getRentHistoryPage(pageable, personalId));
    }

    /**
     * 관리자 대여 페이지
     * @param pageable
     * @param personalId
     * @param name
     * @param bookId
     * @param title
     * @return GetAdminRentPage
     */
    @GetMapping("/admin/rent/getPage")
    public Response getAdminRentPage(@PageableDefault(size = 10) Pageable pageable,
                                     @RequestParam(required = false) String personalId,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) Long bookId,
                                     @RequestParam(required = false) String title) {
        return Response.ok().setData(rentService.getAdminRentPage(pageable, personalId, name, bookId, title));
    }

    /**
     * 관리자 대여 기록 페이지
     * @param pageable
     * @param personalId
     * @param name
     * @param bookId
     * @param title
     * @return GetAdminRentPage
     */
    @GetMapping("/admin/rentHistory/getPage")
    public Response getAdminRentHistoryPage(@PageableDefault(size = 10) Pageable pageable,
                                     @RequestParam(required = false) String personalId,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) Long bookId,
                                     @RequestParam(required = false) String title) {
        return Response.ok().setData(rentService.getAdminRentHistoryPage(pageable, personalId, name, bookId, title));
    }
    /**
     * 도서 대여 연장 (14일)
     * @param rentId
     * @return ok
     */
    @PutMapping("/rent/extend/{rentId}")
    public Response extendRent(@PathVariable("rentId") Long rentId){
        rentService.extentRent(rentId);
        return Response.ok();
    }

    /**
     * 도서 반납
     * @param rentId
     * @return ok
     */
    @PutMapping("/rent/return")
    public Response returnBook(@RequestParam("rentId") Long rentId) {
        rentService.returnBook(rentId);
        return Response.ok();
    }
}
