package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.RentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 도서 대여 연장 (14일)
     * @param rentId
     * @return ok
     */
    @PutMapping("/rent/extend")
    public Response extendRent(@RequestParam("rentId") Long rentId){
        return rentService.extentRent(rentId);
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
