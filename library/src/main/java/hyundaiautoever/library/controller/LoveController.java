package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.LoveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoveController {

    @Autowired
    private LoveService loveService;

    /**
     * 좋아요 생성
     * @param personalId
     * @param bookId
     * @return loveId
     */
    @PostMapping("/love/create")
    public Response createLove(@RequestParam("personalId") String personalId,
                               @RequestParam("bookId") Long bookId) {
        return Response.ok().setData(loveService.createLove(personalId, bookId));
    }

    /**
     * 좋아요 리스트 조회
     * @param personalId
     * @return List<GetLoveDto>
     */
    @GetMapping("/love/get")
    public Response getLoveList(@RequestParam("personalId") String personalId) {
        return Response.ok().setData(loveService.getLoveList(personalId));
    }

    /**
     * 좋아요 삭제
     * @param loveId
     * @return ok
     */
    @DeleteMapping("/love/delete")
    public Response deleteLove(@RequestParam("loveId") Long loveId) {
        loveService.deleteLove(loveId);
        return Response.ok();
    }
}
