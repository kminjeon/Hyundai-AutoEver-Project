package hyundaiautoever.library.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundaiautoever.library.model.dto.BookDto;
import hyundaiautoever.library.model.dto.QBookDto_SearchAdminBookDto;
import hyundaiautoever.library.model.dto.QRentDto_GetAdminRentDto;
import hyundaiautoever.library.model.dto.RentDto;
import hyundaiautoever.library.model.dto.RentDto.GetAdminRentDto;
import hyundaiautoever.library.model.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static hyundaiautoever.library.model.entity.QBook.book;
import static hyundaiautoever.library.model.entity.QRent.rent;
import static hyundaiautoever.library.model.entity.QUser.user;

public class RentRepositorySupportImpl implements RentRepositorySupport{

    private final JPAQueryFactory queryFactory;

    public RentRepositorySupportImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<GetAdminRentDto> getAdminRentPage(Pageable pageable, String personalId, String name, Long bookId, String title) {
        List<GetAdminRentDto> content =  queryFactory
                .select(new QRentDto_GetAdminRentDto(rent))
                .from(rent)
                .where(personalIdContains(personalId),
                        nameContains(name),
                        bookIdContains(bookId),
                        titleContains(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(rent.count())
                .from(rent)
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
