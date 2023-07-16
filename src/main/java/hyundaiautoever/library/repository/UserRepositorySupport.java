package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositorySupport {

    Page<UserDto.LoginDto> searchUserAuthPage(Pageable pageable,
                                              String personalId,
                                              String name);
}
