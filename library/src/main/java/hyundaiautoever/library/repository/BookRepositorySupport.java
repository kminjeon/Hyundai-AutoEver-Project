package hyundaiautoever.library.repository;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.dto.BookDto;
import hyundaiautoever.library.model.dto.BookDto.SearchAdminBookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositorySupport {

    Page<SearchAdminBookDto> searchAdminBookPage(Pageable pageable,
                                                 Long bookId,
                                                 CategoryType categorytype,
                                                 String title);

    Page<BookDto.SimpleBookDto> getSearchBookPage(Pageable pageable, String SearchWord);
}
