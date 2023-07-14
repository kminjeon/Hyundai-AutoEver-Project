package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.dto.ApplyDto;
import hyundaiautoever.library.model.dto.ApplyDto.SearchApplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplyRepositorySupport {

    Page<SearchApplyDto> searchApplyPage(Pageable pageable,
                                         String personalId,
                                         String name,
                                         String title,
                                         String author,
                                         String isbn,
                                         String publisher);
}
