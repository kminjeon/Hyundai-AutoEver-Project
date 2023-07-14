package hyundaiautoever.library.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundaiautoever.library.model.dto.QRentDto_GetAdminRentDto;
import hyundaiautoever.library.model.dto.QReserveDto_GetAdminReserveDto;
import hyundaiautoever.library.model.dto.RentDto;
import hyundaiautoever.library.model.dto.ReserveDto;
import hyundaiautoever.library.model.dto.ReserveDto.GetAdminReserveDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static hyundaiautoever.library.model.entity.QBook.book;
import static hyundaiautoever.library.model.entity.QRent.rent;
import static hyundaiautoever.library.model.entity.QReserve.reserve;
import static hyundaiautoever.library.model.entity.QUser.user;

public class ReserveRepositorySupportImpl implements ReserveRepositorySupport{
    private final JPAQueryFactory queryFactory;

    public ReserveRepositorySupportImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<GetAdminReserveDto> getAdminReservePage(Pageable pageable, String personalId, String name, Long bookId, String title) {
        List<GetAdminReserveDto> content =  queryFactory
                .select(new QReserveDto_GetAdminReserveDto(reserve))
                .from(reserve)
                .where(personalIdContains(personalId),
                        nameContains(name),
                        bookIdContains(bookId),
                        titleContains(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(reserve.count())
                .from(reserve)
                .where(personalIdContains(personalId),
                        nameContains(name),
                        bookIdContains(bookId),
                        titleContains(title));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
    private BooleanExpression personalIdContains(String personalId) {
        return personalId == null ? null : user.personalId.contains(personalId);
    }

    private BooleanExpression nameContains(String name) {
        return name == null ? null : user.name.contains(name);
    }

    private BooleanExpression bookIdContains(Long bookId) {
        return bookId == null ? null : book.id.stringValue().contains(bookId.toString());
    }

    private BooleanExpression titleContains(String title) {
        return title == null ? null : book.title.contains(title);
    }
}
