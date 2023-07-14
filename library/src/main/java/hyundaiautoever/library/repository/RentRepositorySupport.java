package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.dto.RentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentRepositorySupport {

    Page<RentDto.GetAdminRentDto> getAdminRentPage(Pageable pageable,
                                                   String personalId,
                                                   String name,
                                                   Long bookId,
                                                   String title);

    Page<RentDto.GetAdminRentHistoryDto> getAdminRentHistoryPage(Pageable pageable,
                                                   String personalId,
                                                   String name,
                                                   Long bookId,
                                                   String title);
}
