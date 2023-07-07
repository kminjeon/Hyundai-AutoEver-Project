package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.ReserveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    /**
     * 도서 예약 생성
     * @param personalId
     * @param bookId
     * @return reserveId
     */
    @PostMapping("/reserve/create")
    public Response createReserve(@RequestParam("personalId") String personalId, @RequestParam("bookId") Long bookId) {
        return Response.ok().setData(reserveService.createReserve(personalId, bookId));
    }


    /**
     * 도서 예약 삭제
     * @param reserveId
     * @return ok
     */
    @DeleteMapping("/reserve/delete")
    public Response deleteReserve(@RequestParam("reserveId") Long reserveId) {
        reserveService.deleteReserve(reserveId);
        return Response.ok();
    }

}
