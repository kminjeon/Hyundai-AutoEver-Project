package hyundaiautoever.library.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundaiautoever.library.model.dto.QUserDto_LoginDto;
import hyundaiautoever.library.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static hyundaiautoever.library.model.entity.QUser.user;

public class UserRepositorySupportImpl implements UserRepositorySupport{
    private final JPAQueryFactory queryFactory;

    public UserRepositorySupportImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<UserDto.LoginDto> searchUserAuthPage(Pageable pageable, String personalId, String name) {

        List<UserDto.LoginDto> content = queryFactory
                                        .select(new QUserDto_LoginDto(user))
                                        .from(user)
                                        .where(personalIdEq(personalId),
                                                nameEq(name))
                                        .offset(pageable.getOffset())
                                        .limit(pageable.getPageSize())
                                        .fetch();
        JPAQuery<Long> countQuery = queryFactory
                                    .select(user.count())
                                    .from(user)
                                    .where(personalIdEq(personalId),
                                            nameEq(name));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression nameEq(String name) {
        return name == null ? null : user.name.contains(name);
    }

    private BooleanExpression personalIdEq(String personalId) {
        return personalId == null ? null : user.personalId.contains(personalId);
    }
}
