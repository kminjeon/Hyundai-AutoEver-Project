package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.request.ReserveRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.ReserveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
     * @return ok
     */
    @PostMapping("/reserve/create")
    public Response createReserve(@RequestParam String personalId, @RequestParam Long bookId) {
        return reserveService.createReserve(personalId, bookId);
    }

    /**
     * 예약 페이지 조회
     * @param pageable
     * @param personalId
     * @return GetReservePage
     */
    @GetMapping("/mypage/reserve")
    public Response getReservePage(@PageableDefault(size = 10) Pageable pageable,
                                   @RequestParam("personalId") String personalId) {
        return Response.ok().setData(reserveService.getReservePage(pageable, personalId));
    }

    /**
     * 도서 예약 삭제
     * @param reserveId
     * @return ok
     */
    @DeleteMapping("/mypage/reserve/delete")
    public Response deleteReserve(@RequestParam("reserveId") Long reserveId) {
        reserveService.deleteReserve(reserveId);
        return Response.ok();
    }

}
