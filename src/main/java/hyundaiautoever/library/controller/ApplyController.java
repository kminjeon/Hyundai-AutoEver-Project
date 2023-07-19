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
     * @return Apply ID
     */
    @PostMapping("/apply")
    public Response createApply(@RequestBody @Valid ApplyRequest.CreateApplyRequest request) {
        log.info("ApplyController : [createApply]");
        return Response.ok().setData(applyService.createApply(request));
    }


    /**
     * 사용자 도서 신청 리스트 페이지
     * @param pageable
     * @param personalId
     * @return GetApplyPage
     */
    @GetMapping("/mypage/apply/list")
    public Response getApplyPage(@PageableDefault Pageable pageable,
                                 @RequestParam("personalId") String personalId) {
        log.info("ApplyController : [getApplyPage]");
        return Response.ok().setData(applyService.getApplyPage(pageable, personalId));
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
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) String author,
                                    @RequestParam(required = false) String isbn,
                                    @RequestParam(required = false) String publisher
                                    ) {
        log.info("ApplyController : [searchApplyPage]");
        return Response.ok().setData(applyService.searchApplyPage(pageable, personalId, name, title, author, isbn, publisher));
    }

    /**
     * 도서 신청 수정
     * @param applyId
     * @param request
     * @return UpdateApplyDto
     */
    @PutMapping("/apply/{applyId}")
    public Response updateApply(@PathVariable("applyId") Long applyId, @RequestBody @Valid ApplyRequest.UpdateApplyRequest request) {
        log.info("ApplyController : [updateApply]");
        return Response.ok().setData(applyService.updateApply(applyId, request));
    }

    /**
     * 도서 신청 삭제
     * @param applyId
     * @return
     */
    @DeleteMapping("/admin/apply/delete")
    public Response deleteApply(@RequestParam("applyId") Long applyId) {
        log.info("ApplyController : [deleteApply]");
        applyService.deleteApply(applyId);
        return Response.ok();
    }

}
