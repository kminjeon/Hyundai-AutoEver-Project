package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.model.dto.ApplyDto;
import hyundaiautoever.library.model.dto.request.ApplyRequest;
import hyundaiautoever.library.model.entity.Apply;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.ApplyRepository;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import static hyundaiautoever.library.model.dto.ApplyDto.buildUpdateApplyDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회에서 성능 최적화
public class ApplyService {



    private final UserRepository userRepository;
    private final ApplyRepository applyRepository;

    @Transactional
    public Long createApply(ApplyRequest.CreateApplyRequest request) {
        User user = userRepository.findByPersonalId(request.getApplyUser()).orElseThrow(() -> {
            log.error("createApply Exception : [존재하지 않는 userID", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        Apply apply = Apply.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .isbn(request.getIsbn())
                .applyUser(user)
                .build();
        try {
            applyRepository.save(apply);
        } catch (Exception e) {
            log.error("createApply Exception : {}", e.getMessage());
            throw new LibraryException.DataSaveException(ExceptionCode.DATA_SAVE_EXCEPTION);
        }
        return apply.getId();
    }


    @Transactional
    public ApplyDto.UpdateApplyDto updateApply(Long applyId, ApplyRequest.updateApplyRequest request) {
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
