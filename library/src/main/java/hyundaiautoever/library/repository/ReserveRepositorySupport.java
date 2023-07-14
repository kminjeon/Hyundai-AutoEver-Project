package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.dto.ReserveDto.GetAdminReserveDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReserveRepositorySupport {
    Page<GetAdminReserveDto> getAdminReservePage(Pageable pageable,
                                                 String personalId,
                                                 String name,
                                                 Long bookId,
                                                 String title);
}
