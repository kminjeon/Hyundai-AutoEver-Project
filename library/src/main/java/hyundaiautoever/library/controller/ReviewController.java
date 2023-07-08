package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.request.ReviewRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.ReviewService;
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
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 리뷰 생성
     * @param bookId
     * @param request
     * @return CreateReviewDto
     */
    @PostMapping("/review/{bookId}")
    public Response createReview(@PathVariable("bookId") Long bookId, @RequestBody @Valid ReviewRequest.CreateReviewRequest request) {
        return Response.ok().setData(reviewService.createReview(bookId, request));
    }

    /**
     * 리뷰 상세
     * @param reviewId
     * @return GetReviewDetailDto
     */
    @GetMapping("/mypage/review/{reviewId}")
    public Response getReviewDetail(@PathVariable("reviewId") Long reviewId) {
        return Response.ok().setData(reviewService.getReviewDetail(reviewId));
    }

    /**
     * 리뷰 페이지 조회
     * @param pageable
     * @param personalId
     * @return GetReviewPage
     */
    @GetMapping("/mypage/review")
    public Response getReviewPage(@PageableDefault(size = 10) Pageable pageable,
                                  @RequestParam("personalId") String personalId) {
        return Response.ok().setData(reviewService.getReviewPage(pageable, personalId));
    }

    /**
     * 리뷰 수정
     * @param request
     * @return UpdateReviewDto
     */
    @PutMapping("/mypage/review")
    public Response updateReview(@RequestBody @Valid ReviewRequest.UpdateReviewRequest request) {
        return Response.ok().setData(reviewService.updateReview(request));
    }

}
