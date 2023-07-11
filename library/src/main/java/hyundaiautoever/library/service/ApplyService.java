package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.model.dto.ApplyDto;
import hyundaiautoever.library.model.dto.ApplyDto.ApplyListPage;
import hyundaiautoever.library.model.dto.ApplyDto.GetApplyPage;
import hyundaiautoever.library.model.dto.ApplyDto.UpdateApplyDto;
import hyundaiautoever.library.model.dto.request.ApplyRequest;
import hyundaiautoever.library.model.entity.Apply;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.ApplyRepository;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import static hyundaiautoever.library.model.dto.ApplyDto.buildGetApplyPage;
import static hyundaiautoever.library.model.dto.ApplyDto.buildUpdateApplyDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class ApplyService {


    private final UserRepository userRepository;
    private final ApplyRepository applyRepository;

    /**
     * 도서 신청 생성
     * @param request
     * @return applyId
     */
    @Transactional
    public Long createApply(ApplyRequest.CreateApplyRequest request) {
        User user = userRepository.findByPersonalId(request.getPersonalId()).orElseThrow(() -> {
            log.error("createApply Exception : [존재하지 않는 userID", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        Apply apply = Apply.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .isbn(request.getIsbn())
                .user(user)
                .build();
        try {
            applyRepository.save(apply);
        } catch (Exception e) {
            log.error("createApply Exception : {}", e.getMessage());
            throw new LibraryException.DataSaveException(ExceptionCode.DATA_SAVE_EXCEPTION);
        }
        return apply.getId();
    }

    /**
     * 사용자 도서 신청 리스트 페이지
     * @param pageable
     * @param personalId
     * @return GetApplyPage
     */
    public GetApplyPage getApplyPage(Pageable pageable, String personalId) {
        log.info("ApplyService : [getApplyPage]");

        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("GetApplyPage Exception : [존재하지 않는 userID", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        return buildGetApplyPage(applyRepository.findByUser(pageable, user));
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
    public ApplyListPage searchApplyPage(Pageable pageable, String personalId, String title, String author, String isbn, String publisher) {
        log.info("ApplyService : [searchApplyPage]");
        return ApplyDto.buildApplyListPage(applyRepository.searchApplyPage(pageable, personalId, title, author, isbn, publisher));
    }

    /**
     * 도서 신청 수정
     * @param applyId
     * @param request
     * @return UpdateApplyDto
     */
    @Transactional
    public UpdateApplyDto updateApply(Long applyId, ApplyRequest.updateApplyRequest request) {
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> {
            log.error("updateApply Exception : [존재하지 않는 applyID]");
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        apply.updateApplyTitle(request.getTitle() != null ? request.getTitle() : apply.getTitle());
        apply.updateApplyAuthor(request.getAuthor() != null ? request.getAuthor() : apply.getAuthor());
        apply.updateApplyPublisher(request.getPublisher() != null ? request.getPublisher() : apply.getPublisher());
        apply.updateApplyIsbn(request.getIsbn() != null ? request.getIsbn() : apply.getIsbn());

        return buildUpdateApplyDto(apply);
    }


    /**
     * 도서 신청 삭제
     * @param applyId
     */
    @Transactional
    public void deleteApply(Long applyId) {
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> {
           log.error("deleteApply Exception : [존재하지 않는 applyID]");
           return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        try {
            applyRepository.deleteById(applyId);
        } catch (Exception e) {
            log.error("deleteApply Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }
    }
}
