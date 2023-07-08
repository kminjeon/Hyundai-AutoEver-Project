package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.request.ApplyRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.ApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    /**
     * 도서 신청 생성
     * @param request
     * @return
     */
    @PostMapping("/apply")
    public Response createApply(@RequestBody @Valid ApplyRequest.CreateApplyRequest request) {
        log.info("ApplyController : [createApply]");
        return Response.ok().setData(applyService.createApply(request));
    }

    /**
     * 도서 신청 리스트 검색 페이지
     * @param pageable
     * @param personalId
     * @param title
     * @param author
     * @param isbn
     * @param publisher
     * @return ApplyListPage
     */
    @GetMapping("/admin/apply/list")
    public Response searchApplyPage(@PageableDefault(size = 10) Pageable pageable,
                                    @RequestParam(required = false) String personalId,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) String author,
                                    @RequestParam(required = false) String isbn,
                                    @RequestParam(required = false) String publisher
                                    ) {
        log.info("ApplyController : [searchApplyPage]");
        return Response.ok().setData(applyService.searchApplyPage(pageable, personalId, title, author, isbn, publisher));
    }

    /**
     * 도서 신청 수정
     * @param applyId
     * @param request
     * @return UpdateApplyDto
     */
    @PutMapping("/apply/{applyId}")
    public Response updateApply(@PathVariable("applyId") Long applyId, @RequestBody @Valid ApplyRequest.updateApplyRequest request) {
        log.info("ApplyController : [updateApply]");
        return Response.ok().setData(applyService.updateApply(applyId, request));
    }

    /**
     * 도서 신청 삭제
     * @param applyId
     * @return
     */
    @DeleteMapping("/admin/apply/{applyId}")
    public Response deleteApply(@RequestParam("applyId") Long applyId) {
        log.info("ApplyController : [deleteApply]");
        applyService.deleteApply(applyId);
        return Response.ok();
    }

}
